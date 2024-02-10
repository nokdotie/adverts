package ie.nok.adverts.scraper.propertypalcom

import ie.nok.advertisers.Advertiser
import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.adverts.services.propertypalcom.PropertyPalComAdvert
import ie.nok.adverts.{Advert, PropertyType}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import zio.Schedule.{fixed, recurs}
import zio.http.{Body, Client as ZioClient}
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.stream.ZPipeline
import zio.{ZIO, durationInt}

import java.time.Instant

object Property {
  protected[propertypalcom] case class Response(pageProps: ResponsePageProps)
  protected[propertypalcom] given JsonDecoder[Response] =
    DeriveJsonDecoder.gen[Response]

  protected[propertypalcom] case class ResponsePageProps(
      property: ResponsePagePropsProperty
  )
  protected[propertypalcom] given JsonDecoder[ResponsePageProps] =
    DeriveJsonDecoder.gen[ResponsePageProps]

  protected[propertypalcom] case class ResponsePagePropsProperty(
      displayAddress: String,
      images: Option[List[ResponsePagePropsPropertyImage]],
      keyInfo: Option[List[ResponsePagePropsPropertyKeyInfo]],
      shareURL: Option[String],
      coordinate: Option[ResponsePagePropsPropertyCoordinate],
      description: Option[String],
      ber: Option[ResponsePagePropsPropertyBer],
      account: Option[ResponsePagePropsPropertyAccount]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsProperty] =
    DeriveJsonDecoder.gen[ResponsePagePropsProperty]

  protected[propertypalcom] case class ResponsePagePropsPropertyKeyInfo(
      key: String,
      text: Option[String]
  )
  protected[propertypalcom] given JsonDecoder[
    ResponsePagePropsPropertyKeyInfo
  ] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyKeyInfo]

  protected[propertypalcom] case class ResponsePagePropsPropertyImage(
      url: String
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyImage] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyImage]

  protected[propertypalcom] case class ResponsePagePropsPropertyCoordinate(
      latitude: Option[BigDecimal],
      longitude: Option[BigDecimal]
  )
  protected[propertypalcom] given JsonDecoder[
    ResponsePagePropsPropertyCoordinate
  ] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyCoordinate]

  protected[propertypalcom] case class ResponsePagePropsPropertyBer(
      alphanumericRating: Option[String],
      energyPerformanceIndicator: Option[BigDecimal]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyBer] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyBer]

  protected[propertypalcom] case class ResponsePagePropsPropertyAccount(
      psrLicenceNumber: Option[String]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyAccount] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyAccount]

  protected[propertypalcom] def getApiRequestUrl(
      buildId: String,
      propertyIdAndAddress: PropertyIdAndAddress
  ): String =
    s"https://www.propertypal.com/_next/data/$buildId/en/property.json?address=${propertyIdAndAddress.address}&id=${propertyIdAndAddress.id}"

  protected[propertypalcom] def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestBodyAsJson(url)
      .retry(recurs(3) && fixed(1.second))

  protected[propertypalcom] def keyInfoTextToIntOption(
      property: ResponsePagePropsProperty,
      key: String
  ): Option[Int] =
    property.keyInfo
      .getOrElse(List.empty)
      .find { _.key == key }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }

  protected[propertypalcom] def size(
      property: ResponsePagePropsProperty
  ): Option[Area] =
    property.keyInfo
      .getOrElse(List.empty)
      .find { _.key == "SIZE" }
      .flatMap { _.text }
      .map { _.replaceFirst(",", "") }
      .flatMap {
        "([0-9]+\\.?[0-9]*) (sq\\. metres|sq\\. feet|acres)".r.findFirstMatchIn
      }
      .map { found => (found.group(1), found.group(2)) }
      .map {
        case (value, "sq. metres") =>
          Area(BigDecimal(value), AreaUnit.SquareMetres)
        case (value, "sq. feet") => Area(BigDecimal(value), AreaUnit.SquareFeet)
        case (value, "acres")    => Area(BigDecimal(value), AreaUnit.Acres)
        case (value, other) =>
          throw new Exception(
            s"Unknown unit: $other, ${property.shareURL}"
          )
      }

  protected[propertypalcom] def ber(
      property: ResponsePagePropsProperty
  ): (Option[Rating], Option[BigDecimal]) =
    property.ber
      .fold((None, None)) { ber =>
        (
          ber.alphanumericRating.flatMap { Rating.tryFromString(_).toOption },
          ber.energyPerformanceIndicator
        )
      }

  protected[propertypalcom] def propertyType(
      property: ResponsePagePropsProperty
  ): Option[PropertyType] =
    property.keyInfo
      .getOrElse(List.empty)
      .find { _.key == "STYLE" }
      .flatMap { _.text }
      .flatMap { text =>
        PropertyType.tryFromString(text).toOption
      }

  private def advertiser(
      response: Response
  ): ZIO[AdvertiserStore, Throwable, Option[Advertiser]] =
    response.pageProps.property.account.flatMap { _.psrLicenceNumber }.fold(ZIO.succeed(None)) {
      AdvertiserStore.getByPropertyServicesRegulatoryAuthorityLicenceNumber
    }

  protected[propertypalcom] def toPropertyPalComAdvertOption(
      response: Response,
      advertiser: Option[Advertiser]
  ): Option[PropertyPalComAdvert] = {
    val price = keyInfoTextToIntOption(response.pageProps.property, "PRICE")

    val coordinates = response.pageProps.property.coordinate
      .flatMap { coordinate =>
        coordinate.latitude.zip(coordinate.longitude)
      }
      .map { (latitude, longitude) =>
        Coordinates(
          latitude = latitude,
          longitude = longitude
        )
      }

    val imageUrls = response.pageProps.property.images.getOrElse(List.empty).map(_.url)

    val bedroomsCount  = keyInfoTextToIntOption(response.pageProps.property, "BEDROOMS")
    val bathroomsCount = keyInfoTextToIntOption(response.pageProps.property, "BATHROOMS")

    val (rating, energyRatingInKWhPerSqtMtrPerYear) = ber(response.pageProps.property)

    val (address, eircode) = Eircode.unzip(response.pageProps.property.displayAddress)

    response.pageProps.property.shareURL.map { url =>
      PropertyPalComAdvert(
        url = url,
        priceInEur = price,
        address = address,
        description = response.pageProps.property.description,
        propertyType = propertyType(response.pageProps.property),
        eircode = eircode,
        coordinates = coordinates,
        imageUrls = imageUrls,
        size = size(response.pageProps.property),
        bedroomsCount = bedroomsCount,
        bathroomsCount = bathroomsCount,
        buildingEnergyRating = rating,
        buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = energyRatingInKWhPerSqtMtrPerYear,
        advertiser = advertiser,
        createdAt = Instant.now
      )
    }
  }

  def pipeline(
      buildId: String
  ): ZPipeline[ZioClient & AdvertiserStore, Throwable, PropertyIdAndAddress, Advert] =
    ZPipeline
      .map { getApiRequestUrl(buildId, _) }
      .mapZIOParUnordered(5) { getApiResponse }
      .mapZIOParUnordered(5) { response => advertiser(response).map { (response, _) } }
      .map { toPropertyPalComAdvertOption }
      .collectSome
      .map { PropertyPalComAdvert.toAdvert }

}
