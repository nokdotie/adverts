package ie.nok.adverts.scraper.daftie

import ie.nok.adverts.services.daftie.DaftIeAdvert
import ie.nok.adverts.{Advert, Seller}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import zio.Schedule.{fixed, recurs}
import zio.http.model.{Headers, Method}
import zio.http.{Body, Client as ZioClient}
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.stream.ZStream
import zio.{ZIO, durationInt}
import ie.nok.hash.Hasher

import java.time.Instant
import java.util.UUID
import scala.util.Try

object Properties {
  protected[daftie] case class Response(listings: List[ResponseListing])
  protected[daftie] given JsonDecoder[Response] =
    DeriveJsonDecoder.gen[Response]

  protected[daftie] case class ResponseListing(listing: ResponseListingListing)
  protected[daftie] given JsonDecoder[ResponseListing] =
    DeriveJsonDecoder.gen[ResponseListing]

  protected[daftie] case class ResponseListingListing(
      floorArea: Option[ResponseListingListingFloorArea],
      media: ResponseListingListingMedia,
      numBathrooms: Option[String],
      numBedrooms: Option[String],
      price: String,
      seoFriendlyPath: String,
      title: String,
      point: ResponseListingListingPoint,
      ber: Option[ResponseListingListingBer],
      seller: Option[ResponseListingListingSeller]
  )
  protected[daftie] given JsonDecoder[ResponseListingListing] =
    DeriveJsonDecoder.gen[ResponseListingListing]

  protected[daftie] case class ResponseListingListingFloorArea(
      unit: String,
      value: String
  )
  protected[daftie] given JsonDecoder[ResponseListingListingFloorArea] =
    DeriveJsonDecoder.gen[ResponseListingListingFloorArea]

  protected[daftie] case class ResponseListingListingMedia(
      images: Option[List[ResponseListingListingMediaImage]]
  )
  protected[daftie] given JsonDecoder[ResponseListingListingMedia] =
    DeriveJsonDecoder.gen[ResponseListingListingMedia]

  protected[daftie] case class ResponseListingListingMediaImage(
      size720x480: String
  )
  protected[daftie] given JsonDecoder[ResponseListingListingMediaImage] =
    DeriveJsonDecoder.gen[ResponseListingListingMediaImage]

  protected[daftie] case class ResponseListingListingPoint(
      coordinates: List[BigDecimal]
  )
  protected[daftie] given JsonDecoder[ResponseListingListingPoint] =
    DeriveJsonDecoder.gen[ResponseListingListingPoint]
  protected[daftie] case class ResponseListingListingSeller(
      sellerId: Int,
      name: String,
      phone: Option[String],
      alternativePhone: Option[String],
      address: Option[String],
      branch: Option[String],
      profileImage: Option[String],
      standardLogo: Option[String],
      squareLogo: Option[String],
      licenceNumber: Option[String]
  )

  protected[daftie] given JsonDecoder[ResponseListingListingSeller] =
    DeriveJsonDecoder.gen[ResponseListingListingSeller]

  protected[daftie] case class ResponseListingListingBer(
      rating: Option[String],
      code: Option[String],
      epi: Option[String]
  )

  protected[daftie] given JsonDecoder[ResponseListingListingBer] =
    DeriveJsonDecoder.gen[ResponseListingListingBer]

  protected[daftie] val streamApiRequestContent
      : ZStream[Any, Nothing, String] = {
    val pageSize = 100
    ZStream
      .iterate(0)(_ + pageSize)
      .map { from =>
        s"""{"section":"residential-for-sale","filters":[{"name":"adState","values":["published"]}],"andFilters":[],"ranges":[],"paging":{"from":"$from","pageSize":"$pageSize"},"geoFilter":{},"terms":""}"""
      }
  }

  protected[daftie] def getApiResponse(
      content: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val brandHeader = Headers("brand", "daft")
    val platformHeader = Headers("platform", "web")
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[Response](
        "https://gateway.daft.ie/old/v1/listings",
        method = Method.POST,
        headers = brandHeader ++ platformHeader ++ contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  protected[daftie] def size(listing: ResponseListingListing): Option[Area] = {
    val value = listing.floorArea.map { _.value }.map { BigDecimal(_) }
    val unit = listing.floorArea.map { _.unit }.map {
      case "METRES_SQUARED" => AreaUnit.SquareMetres
      case "ACRES"          => AreaUnit.Acres
      case other            => throw new Exception(s"Unknown unit: $other")
    }

    value.zip(unit).map { Area(_, _) }
  }

  protected[daftie] def ber(
      listing: ResponseListingListing
  ): (Option[Rating], Option[Int], Option[BigDecimal]) =
    listing.ber
      .fold((None, None, None)) { ber =>
        val rating = ber.rating.flatMap { Rating.tryFromString(_).toOption }

        val certificateNumber = ber.code
          .flatMap { _.toIntOption }

        val energyRatingInKWhPerSqtMtrPerYear = ber.epi
          .map { _.takeWhile { char => char.isDigit || char == '.' } }
          .flatMap { value => Try { BigDecimal(value) }.toOption }

        (rating, certificateNumber, energyRatingInKWhPerSqtMtrPerYear)
      }

  protected[daftie] def seller(
      listing: ResponseListingListing
  ): Option[Seller] = {
    listing.seller.map { seller =>
      Seller(
        sellerId = Hasher.hash(seller.licenceNumber.getOrElse(UUID.randomUUID().toString)),
        name = seller.name,
        phone = seller.phone,
        alternativePhone = seller.alternativePhone,
        address = seller.address,
        licenceNumber = seller.licenceNumber
      )
    }
  }

  protected[daftie] def toDaftIeAdvert(
      listing: ResponseListingListing
  ): DaftIeAdvert = {
    val url = s"https://www.daft.ie${listing.seoFriendlyPath}"
    val price = listing.price.filter(_.isDigit).toIntOption

    val coordinates = Coordinates(
      latitude = listing.point.coordinates(1),
      longitude = listing.point.coordinates.head
    )

    val imageUrls =
      listing.media.images.getOrElse(List.empty).map { _.size720x480 }

    val bedroomCount = listing.numBedrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val bathroomCount = listing.numBathrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val (
      buildingEnergyRating,
      buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear
    ) = ber(listing)

    val (address, eircode) = Eircode.unzip(listing.title)

    DaftIeAdvert(
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
      buildingEnergyRatingCertificateNumber =
        buildingEnergyRatingCertificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      seller = seller(listing),
      createdAt = Instant.now
    )
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    streamApiRequestContent
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.listings }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { _.listing }
      .map { toDaftIeAdvert }
      .map { DaftIeAdvert.toAdvert }

}
