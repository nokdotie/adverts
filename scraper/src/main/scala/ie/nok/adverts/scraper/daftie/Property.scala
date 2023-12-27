package ie.nok.adverts.scraper.daftie

import ie.nok.adverts.Advert
import ie.nok.adverts.services.daftie.DaftIeAdvert
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.http.model.Headers
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import zio.stream.ZPipeline
import scala.util.Try
import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.advertisers.Advertiser

object Property {
  protected[daftie] case class Response(pageProps: ResponsePageProps)
  protected[daftie] given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  protected[daftie] case class ResponsePageProps(
      canonicalUrl: String,
      listing: ResponsePagePropsListing
  )
  protected[daftie] given JsonDecoder[ResponsePageProps] = DeriveJsonDecoder.gen[ResponsePageProps]

  protected[daftie] case class ResponsePagePropsListing(
      title: String,
      price: String,
      numBedrooms: Option[String],
      numBathrooms: Option[String],
      media: ResponsePagePropsListingMedia,
      ber: Option[ResponsePagePropsListingBer],
      floorArea: Option[ResponsePagePropsListingFloorArea],
      point: ResponsePagePropsListingPoint,
      description: String,
      seller: ResponsePagePropsListingSeller
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListing] = DeriveJsonDecoder.gen[ResponsePagePropsListing]

  protected[daftie] case class ResponsePagePropsListingMedia(
      images: List[ResponsePagePropsListingMediaImage]
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingMedia] = DeriveJsonDecoder.gen[ResponsePagePropsListingMedia]

  protected[daftie] case class ResponsePagePropsListingMediaImage(
      size1440x960: String
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingMediaImage] = DeriveJsonDecoder.gen[ResponsePagePropsListingMediaImage]

  protected[daftie] case class ResponsePagePropsListingBer(
      rating: Option[String],
      code: Option[String],
      epi: Option[String]
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingBer] = DeriveJsonDecoder.gen[ResponsePagePropsListingBer]

  protected[daftie] case class ResponsePagePropsListingFloorArea(
      value: String,
      unit: String
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingFloorArea] = DeriveJsonDecoder.gen[ResponsePagePropsListingFloorArea]

  protected[daftie] case class ResponsePagePropsListingPoint(
      coordinates: List[BigDecimal]
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingPoint] = DeriveJsonDecoder.gen[ResponsePagePropsListingPoint]

  protected[daftie] case class ResponsePagePropsListingSeller(
      licenceNumber: Option[String]
  )
  protected[daftie] given JsonDecoder[ResponsePagePropsListingSeller] = DeriveJsonDecoder.gen[ResponsePagePropsListingSeller]

  private def getRequestUrl(buildId: String, propertyId: Int): String =
    s"https://www.daft.ie/_next/data/$buildId/property.json?id=$propertyId"

  private def getApiResponse(url: String): ZIO[ZioClient, Throwable, Response] = {
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[Response](
        url,
        headers = contentTypeHeader
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def size(response: Response): Option[Area] = {
    val value = response.pageProps.listing.floorArea.map { _.value }.map { BigDecimal(_) }
    val unit = response.pageProps.listing.floorArea.map { _.unit }.map {
      case "METRES_SQUARED" => AreaUnit.SquareMetres
      case "ACRES"          => AreaUnit.Acres
      case other            => throw new Exception(s"Unknown unit: $other")
    }

    value.zip(unit).map { Area(_, _) }
  }

  private def ber(
      response: Response
  ): (Option[Rating], Option[Int], Option[BigDecimal]) =
    response.pageProps.listing.ber
      .fold((None, None, None)) { ber =>
        val rating = ber.rating.flatMap { Rating.tryFromString(_).toOption }

        val certificateNumber = ber.code
          .flatMap { _.toIntOption }

        val energyRatingInKWhPerSqtMtrPerYear = ber.epi
          .map { _.takeWhile { char => char.isDigit || char == '.' } }
          .flatMap { value => Try { BigDecimal(value) }.toOption }

        (rating, certificateNumber, energyRatingInKWhPerSqtMtrPerYear)
      }

  private def advertiser(
      response: Response
  ): ZIO[AdvertiserStore, Throwable, Option[Advertiser]] =
    response.pageProps.listing.seller.licenceNumber.fold(ZIO.succeed(None)) { AdvertiserStore.getByPropertyServicesRegulatoryAuthorityLicenceNumber(_) }

  protected[daftie] def toDaftIeAdvert(response: Response, advertiser: Option[Advertiser]): DaftIeAdvert = {
    val price              = response.pageProps.listing.price.filter(_.isDigit).toIntOption
    val (address, eircode) = Eircode.unzip(response.pageProps.listing.title)

    val coordinates = Coordinates(
      latitude = response.pageProps.listing.point.coordinates(1),
      longitude = response.pageProps.listing.point.coordinates(0)
    )

    val bedroomsCount = response.pageProps.listing.numBedrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val bathroomsCount = response.pageProps.listing.numBathrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val (
      buildingEnergyRating,
      buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear
    ) = ber(response)

    DaftIeAdvert(
      url = response.pageProps.canonicalUrl,
      priceInEur = price,
      description = response.pageProps.listing.description,
      address = address,
      eircode = eircode,
      coordinates = coordinates,
      imageUrls = response.pageProps.listing.media.images.map { _.size1440x960 },
      size = size(response),
      bedroomsCount = bedroomsCount,
      bathroomsCount = bathroomsCount,
      buildingEnergyRating = buildingEnergyRating,
      buildingEnergyRatingCertificateNumber = buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      advertiser = advertiser,
      createdAt = Instant.now
    )
  }

  def pipeline(apiKey: String): ZPipeline[ZioClient & AdvertiserStore, Throwable, Int, Advert] =
    ZPipeline
      .map { getRequestUrl(apiKey, _) }
      .mapZIO { getApiResponse }
      .mapZIO { listing => advertiser(listing).map { (listing, _) } }
      .map { toDaftIeAdvert }
      .map { DaftIeAdvert.toAdvert }

}
