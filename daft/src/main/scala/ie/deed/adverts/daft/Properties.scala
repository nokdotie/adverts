package ie.deed.adverts.daft

import ie.deed.adverts.Record
import ie.deed.adverts.utils.Eircode
import ie.deed.adverts.utils.zio.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant

object Properties {
  case class Response(listings: List[ResponseListing])
  given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  case class ResponseListing(listing: ResponseListingListing)
  given JsonDecoder[ResponseListing] = DeriveJsonDecoder.gen[ResponseListing]

  case class ResponseListingListing(
      media: ResponseListingListingMedia,
      price: String,
      seller: ResponseListingListingSeller,
      seoFriendlyPath: String,
      title: String
  )
  given JsonDecoder[ResponseListingListing] =
    DeriveJsonDecoder.gen[ResponseListingListing]

  case class ResponseListingListingMedia(
      images: Option[List[ResponseListingListingMediaImage]]
  )
  given JsonDecoder[ResponseListingListingMedia] =
    DeriveJsonDecoder.gen[ResponseListingListingMedia]

  case class ResponseListingListingMediaImage(
      size720x480: String
  )
  given JsonDecoder[ResponseListingListingMediaImage] =
    DeriveJsonDecoder.gen[ResponseListingListingMediaImage]

  case class ResponseListingListingSeller(
      name: String,
      phone: Option[String]
  )
  given JsonDecoder[ResponseListingListingSeller] =
    DeriveJsonDecoder.gen[ResponseListingListingSeller]

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
      .requestJson[Response](
        "https://gateway.daft.ie/old/v1/listings",
        method = Method.POST,
        headers = brandHeader ++ platformHeader ++ contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def toRecordOption(
      listing: ResponseListingListing
  ): Option[Record] = {
    val price = listing.price.filter(_.isDigit).toIntOption
    val eircode = listing.title
      .pipe(Eircode.regex.findFirstIn)
      .map(_.filter(_.isLetterOrDigit))
    val phone = listing.seller.phone

    ((price, eircode, phone) match {
      case (Some(price), Some(eircode), Some(phone)) =>
        Some((price, eircode, phone))
      case _ => None
    }).map { (price, eircode, phone) =>
      Record(
        at = Instant.now,
        advertUrl = s"https://www.daft.ie${listing.seoFriendlyPath}",
        advertPrice = price,
        propertyEircode = eircode,
        propertyImageUrls =
          listing.media.images.getOrElse(List.empty).map { _.size720x480 },
        contactName = listing.seller.name,
        contactPhone = Option(phone),
        contactEmail = Option.empty
      )
    }
  }

  val stream: ZStream[ZioClient, Throwable, Record] =
    streamApiRequestContent
      .mapZIOPar(5) { getApiResponse }
      .map { _.listings }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { _.listing }
      .map { toRecordOption }
      .collectSome

}
