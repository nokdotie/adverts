package ie.nok.adverts.scraper.daftie

import ie.nok.adverts.Advert
import ie.nok.http.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant
import ie.nok.unit.{Area, AreaUnit}

object Properties {
  private case class Response(listings: List[ResponseListing])
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseListing(listing: ResponseListingListing)
  private given JsonDecoder[ResponseListing] =
    DeriveJsonDecoder.gen[ResponseListing]

  private case class ResponseListingListing(
      floorArea: Option[ResponseListingListingFloorArea],
      media: ResponseListingListingMedia,
      numBathrooms: Option[String],
      numBedrooms: Option[String],
      price: String,
      seoFriendlyPath: String,
      title: String
  )
  private given JsonDecoder[ResponseListingListing] =
    DeriveJsonDecoder.gen[ResponseListingListing]

  private case class ResponseListingListingFloorArea(
      unit: String,
      value: String
  )
  private given JsonDecoder[ResponseListingListingFloorArea] =
    DeriveJsonDecoder.gen[ResponseListingListingFloorArea]

  private case class ResponseListingListingMedia(
      images: Option[List[ResponseListingListingMediaImage]]
  )
  private given JsonDecoder[ResponseListingListingMedia] =
    DeriveJsonDecoder.gen[ResponseListingListingMedia]

  private case class ResponseListingListingMediaImage(
      size720x480: String
  )
  private given JsonDecoder[ResponseListingListingMediaImage] =
    DeriveJsonDecoder.gen[ResponseListingListingMediaImage]

  private val streamApiRequestContent = {
    val pageSize = 100
    ZStream
      .iterate(0)(_ + pageSize)
      .map { from =>
        s"""{"section":"residential-for-sale","filters":[{"name":"adState","values":["published"]}],"andFilters":[],"ranges":[],"paging":{"from":"$from","pageSize":"$pageSize"},"geoFilter":{},"terms":""}"""
      }
  }

  private def getApiResponse(
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

  private def toAdvert(
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
