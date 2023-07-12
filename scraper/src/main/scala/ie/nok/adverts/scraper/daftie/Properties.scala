package ie.nok.adverts.scraper.daftie

import ie.nok.adverts.Advert
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit, Coordinates}
import scala.util.chaining.scalaUtilChainingOps
import java.time.Instant
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}

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
      point: ResponseListingListingPoint
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

  protected[daftie] val streamApiRequestContent = {
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

  protected[daftie] def toAdvert(
      listing: ResponseListingListing
  ): Advert = {
    val price = listing.price.filter(_.isDigit).toIntOption.getOrElse(0)

    val propertySizeValue =
      listing.floorArea.map { _.value }.map { BigDecimal(_) }
    val propertySizeUnit = listing.floorArea.map { _.unit }.map {
      case "METRES_SQUARED" => AreaUnit.SquareMetres
      case "ACRES"          => AreaUnit.Acres
      case other            => throw new Exception(s"Unknown unit: $other")
    }

    val propertySize =
      propertySizeValue.zip(propertySizeUnit).fold(Area.empty) { Area(_, _) }

    val bedroomCount = listing.numBedrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption
      .getOrElse(0)

    val bathroomCount = listing.numBathrooms
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption
      .getOrElse(0)

    Advert(
      advertUrl = s"https://www.daft.ie${listing.seoFriendlyPath}",
      advertPriceInEur = price,
      propertyAddress = listing.title,
      propertyCoordinates = Coordinates(
        latitude = listing.point.coordinates(1),
        longitude = listing.point.coordinates(0)
      ),
      propertyImageUrls =
        listing.media.images.getOrElse(List.empty).map { _.size720x480 },
      propertySize = propertySize,
      propertySizeInSqtMtr = Area.toSquareMetres(propertySize).value,
      propertyBedroomsCount = bedroomCount,
      propertyBathroomsCount = bathroomCount,
      createdAt = Instant.now()
    )
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    streamApiRequestContent
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.listings }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { _.listing }
      .map { toAdvert }

}
