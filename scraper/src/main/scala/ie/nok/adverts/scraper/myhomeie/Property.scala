package ie.nok.adverts.scraper.myhomeie

import ie.nok.advertisers.Advertiser
import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.adverts.{Advert, AdvertSaleStatus}
import ie.nok.adverts.scraper.propertypalcom.Property.ResponsePagePropsProperty
import ie.nok.adverts.services.myhomeie.MyHomeIeAdvert
import ie.nok.ber.Rating
import ie.nok.geographic.Coordinates
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import zio.Schedule.{fixed, recurs}
import zio.http.Client as ZioClient
import zio.http.model.Headers
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.stream.ZPipeline
import zio.{ZIO, durationInt}

import java.time.Instant

object Property {
  protected[myhomeie] case class Response(Brochure: ResponseBrochure)
  protected[myhomeie] given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  protected[myhomeie] case class ResponseBrochure(
      Group: ResponseBrochureGroup,
      Property: ResponseBrochureProperty
  )
  protected[myhomeie] given JsonDecoder[ResponseBrochure] = DeriveJsonDecoder.gen[ResponseBrochure]

  protected[myhomeie] case class ResponseBrochureGroup(
      SalesLicense: Option[String]
  )
  protected[myhomeie] given JsonDecoder[ResponseBrochureGroup] = DeriveJsonDecoder.gen[ResponseBrochureGroup]

  protected[myhomeie] case class ResponseBrochureProperty(
      PropertyId: Int,
      DisplayAddress: String,
      Eircode: String,
      Photos: List[String],
      BathString: Option[String],
      BedsString: Option[String],
      PriceAsString: String,
      SizeStringMeters: Option[BigDecimal],
      BerRating: Option[String],
      BrochureContent: List[ResponseBrochurePropertyBrochureContent],
      BrochureMap: Option[ResponseBrochurePropertyBrochureMap],
      PropertyType: Option[String],
      AgreedOn: Option[String]
  )
  protected[myhomeie] given JsonDecoder[ResponseBrochureProperty] = DeriveJsonDecoder.gen[ResponseBrochureProperty]

  protected[myhomeie] case class ResponseBrochurePropertyBrochureContent(
      ContentType: String,
      Content: String
  )
  protected[myhomeie] given JsonDecoder[ResponseBrochurePropertyBrochureContent] = DeriveJsonDecoder.gen[ResponseBrochurePropertyBrochureContent]

  protected[myhomeie] case class ResponseBrochurePropertyBrochureMap(
      longitude: BigDecimal,
      latitude: BigDecimal
  )
  protected[myhomeie] given JsonDecoder[ResponseBrochurePropertyBrochureMap] = DeriveJsonDecoder.gen[ResponseBrochurePropertyBrochureMap]

  private def getRequestUrl(apiKey: String, propertyId: Int): String =
    s"https://api.myhome.ie/brochure/$propertyId?ApiKey=$apiKey&format=json"

  private def getApiResponse(url: String): ZIO[ZioClient, Throwable, Response] = {
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[Response](
        url,
        headers = contentTypeHeader
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def advertiser(
      response: Response
  ): ZIO[AdvertiserStore, Throwable, Option[Advertiser]] =
    response.Brochure.Group.SalesLicense.fold(ZIO.succeed(None)) { AdvertiserStore.getByPropertyServicesRegulatoryAuthorityLicenceNumber }

  protected[myhomeie] def toMyHomeIeAdvert(response: Response, advertiser: Option[Advertiser]): MyHomeIeAdvert = {
    val url = s"https://www.myhome.ie/${response.Brochure.Property.PropertyId}"
    val price = response.Brochure.Property.PriceAsString
      .filter(_.isDigit)
      .toIntOption

    val description = response.Brochure.Property.BrochureContent
      .find(_.ContentType == "Description")
      .map {
        _.Content.linesIterator
          .mkString("\n")
          .trim
      }

    val coordinates = response.Brochure.Property.BrochureMap.map { bm =>
      Coordinates(
        latitude = bm.latitude,
        longitude = bm.longitude
      )
    }

    val size = response.Brochure.Property.SizeStringMeters
      .map { Area(_, AreaUnit.SquareMetres) }

    val bedroomsCount = response.Brochure.Property.BedsString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val bathroomsCount = response.Brochure.Property.BathString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val buildingEnergyRating = response.Brochure.Property.BerRating
      .flatMap { Rating.tryFromString(_).toOption }

    val propertyType: Option[ie.nok.adverts.PropertyType] =
      response.Brochure.Property.PropertyType
        .flatMap { propertyType =>
          ie.nok.adverts.PropertyType.tryFromString(propertyType).toOption
        }

    val saleStatus = response.Brochure.Property.AgreedOn.fold(AdvertSaleStatus.ForSale)(_ => AdvertSaleStatus.SaleAgreed)

    MyHomeIeAdvert(
      url = url,
      saleStatus = saleStatus,
      priceInEur = price,
      description = description,
      address = response.Brochure.Property.DisplayAddress,
      propertyType = propertyType,
      coordinates = coordinates,
      imageUrls = response.Brochure.Property.Photos,
      size = size,
      bedroomsCount = bedroomsCount,
      bathroomsCount = bathroomsCount,
      buildingEnergyRating = buildingEnergyRating,
      advertiser = advertiser,
      createdAt = Instant.now
    )
  }

  def pipeline(apiKey: String): ZPipeline[ZioClient & AdvertiserStore, Throwable, Int, Advert] =
    ZPipeline
      .map { getRequestUrl(apiKey, _) }
      .mapZIOParUnordered(5) {
        getApiResponse(_)
          .fold(
            throwable => {
              println(s"Failure: ${throwable.getMessage}")
              throwable.printStackTrace()

              None
            },
            Option.apply
          )
      }
      .collectSome
      .mapZIOParUnordered(5) { listing => advertiser(listing).map { (listing, _) } }
      .map { toMyHomeIeAdvert }
      .map { MyHomeIeAdvert.toAdvert }

}
