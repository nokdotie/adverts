package ie.nok.adverts.scraperV2.daftie

import ie.nok.adverts.scraperV2.AdvertV2
import ie.nok.adverts.scraperV2.daftie.DaftPropertyResponse.{DaftResponse, Listing}
import ie.nok.adverts.scraperV2.daftie.DaftPropertyResponseJsonDecoders.given_JsonDecoder_DaftResponse
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import zio.Schedule.{fixed, recurs}
import zio.http.model.{Headers, Method}
import zio.http.{Body, Client as ZioClient}
import zio.stream.ZStream
import zio.{ZIO, durationInt}

import java.time.Instant
import scala.util.Try

object DaftProperties {

  protected[daftie] def streamApiRequestContent(pageSize: Int = 100): ZStream[Any, Nothing, String] =
    ZStream
      .iterate(0)(_ + pageSize)
      .map { from => DaftPropertyRequestBody.paginatedStr(from, pageSize) }

  protected[daftie] def getApiResponse(content: String): ZIO[ZioClient, Throwable, DaftResponse] = {

    val brandHeader       = Headers("brand", "daft")
    val platformHeader    = Headers("platform", "web")
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[DaftResponse](
        "https://gateway.daft.ie/old/v1/listings",
        method = Method.POST,
        headers = brandHeader ++ platformHeader ++ contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  protected[daftie] val httpStream: ZStream[ZioClient, Throwable, DaftResponse] =
    streamApiRequestContent()
      .mapZIOParUnordered(5) { getApiResponse }
      .takeWhile { _.listings.nonEmpty }

  protected[daftie] def toAdvert(daftResponse: DaftResponse): List[AdvertV2] =
    daftResponse.listings
      .map { _.listing }
      .map { toDaftIeAdvert }
      .map { DaftIeAdvertV2.toAdvert }

  val stream: ZStream[ZioClient, Throwable, AdvertV2] = httpStream.mapConcat(toAdvert)

  private def size(listing: Listing): Option[Area] = {
    val value = listing.floorArea
      .map { _.value }
      .map { BigDecimal(_) }
    val unit = listing.floorArea
      .map { _.unit }
      .map {
        case "METRES_SQUARED" => AreaUnit.SquareMetres
        case "ACRES"          => AreaUnit.Acres
        case other            => throw new Exception(s"Unknown unit: $other")
      }

    value.zip(unit).map { Area(_, _) }
  }

  private def ber(
      listing: Listing
  ): (Option[Rating], Option[Int], Option[BigDecimal]) =
    listing.ber
      .fold((None, None, None)) { ber =>
        val rating            = ber.rating.flatMap { Rating.tryFromString(_).toOption }
        val certificateNumber = ber.code.flatMap { _.toIntOption }
        val energyRatingInKWhPerSqtMtrPerYear = ber.epi
          .map { _.takeWhile { char => char.isDigit || char == '.' } }
          .flatMap { value => Try { BigDecimal(value) }.toOption }
        (rating, certificateNumber, energyRatingInKWhPerSqtMtrPerYear)
      }

  private def seller(listing: Listing): Seller = Seller(
    sellerId = listing.seller.sellerId,
    name = listing.seller.name,
    phone = listing.seller.phone,
    alternativePhone = listing.seller.alternativePhone,
    address = listing.seller.address,
    branch = listing.seller.branch,
    profileImage = listing.seller.profileImage,
    standardLogo = listing.seller.standardLogo,
    squareLogo = listing.seller.squareLogo,
    licenceNumber = listing.seller.licenceNumber
  )

  protected[daftie] def toDaftIeAdvert(listing: Listing): DaftIeAdvertV2 = {
    val url   = s"https://www.daft.ie${listing.seoFriendlyPath}"
    val price = listing.price.filter(_.isDigit).toIntOption

    val coordinates = Coordinates(latitude = listing.point.coordinates(1), longitude = listing.point.coordinates.head)

    val imageUrls = listing.media.images.getOrElse(List.empty).map { _.size720x480 }

    val bedroomCount = listing.numBedrooms.getOrElse("").filter(_.isDigit).toIntOption

    val bathroomCount = listing.numBathrooms.getOrElse("").filter(_.isDigit).toIntOption

    val (
      buildingEnergyRating,
      buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear
    ) = ber(listing)

    val (address, eircode) = Eircode.unzip(listing.title)

    DaftIeAdvertV2(
      url = url,
      priceInEur = price,
      address = address,
      eircode = eircode,
      coordinates = coordinates,
      imageUrls = imageUrls,
      size = size(listing),
      bedroomsCount = bedroomCount,
      bathroomsCount = bathroomCount,
      buildingEnergyRating = buildingEnergyRating,
      buildingEnergyRatingCertificateNumber = buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      seller = seller(listing),
      createdAt = Instant.now
    )
  }
}
