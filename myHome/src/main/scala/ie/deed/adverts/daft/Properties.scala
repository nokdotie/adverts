package ie.nok.adverts.daft

import ie.nok.adverts.Record
import ie.nok.adverts.utils.Eircode
import ie.nok.adverts.utils.zio.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant

object Properties {
  private case class Response(SearchResults: List[ResponseSearchResult])
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseSearchResult(
      PriceAsString: Option[String],
      DisplayAddress: String,
      BrochureUrl: String,
      Photos: List[String],
      Negotiator: Option[ResponseSearchResultNegotiator]
  )
  private given JsonDecoder[ResponseSearchResult] =
    DeriveJsonDecoder.gen[ResponseSearchResult]

  private case class ResponseSearchResultNegotiator(
      Name: String,
      Phone: Option[String],
      Email: Option[String]
  )
  private given JsonDecoder[ResponseSearchResultNegotiator] =
    DeriveJsonDecoder.gen[ResponseSearchResultNegotiator]

  private val streamApiRequestContent =
    ZStream
      .iterate(1)(_ + 1)
      .map { page =>
        s"""{"SearchRequest":{"IsBackendSearch":false,"SkipSearchIndex":false,"IsGroupPrivateSearch":false,"IsSaleAgreed":false,"IsSold":false,"IsAuction":false,"IsBoundsSearch":false,"UseFreeTextSearchForKeywords":false,"SearchContent":false,"PropertyIds":[],"GroupIds":[],"ChannelIds":[1],"PropertyTypeIds":[],"PropertyClassIds":[1],"PropertyStatusIds":[2,12],"SaleTypeIds":[],"FeatureTypeIds":[],"RegionId":2168,"LocalityIds":[],"LocalityNames":[],"NegotiatorIds":[],"SolicitorIds":[],"BuyerSolicitorIds":[],"VendorSolicitorIds":[],"TransferedByUserIds":[],"RowStatusIds":[2,2,2],"EnergyRatings":[],"Polygons":[],"Destinations":[],"Tags":[],"PrivateTags":[],"PreSixtyThree":false,"IsActive":true,"HasPhotos":false,"PriceFrequency":"Monthly"},"SortColumn":2,"SortDirection":2,"IsIrelandSearch":false,"IsMapView":false,"GetAuctions":false,"Url":"https://www.myhome.ie/residential/ireland/property-for-sale","Page":$page,"PageSize":20,"ApiKey":"4284149e-13da-4f12-aed7-0d644a0b7adb","CorrelationId":"4a8c7692-5697-497d-ab19-f3f707f18212","Version":1,"RequestTypeId":2,"Endpoint":"https://api.myhome.ie/search","RequestVerb":"POST"}"""
      }

  private def getApiResponse(
      content: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestJson[Response](
        "https://api.myhome.ie/search",
        method = Method.POST,
        headers = contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def toRecordOption(
      searchResul: ResponseSearchResult
  ): Option[Record] = {
    val price =
      searchResul.PriceAsString.getOrElse("").filter(_.isDigit).toIntOption
    val eircode = searchResul.DisplayAddress
      .pipe(Eircode.regex.findFirstIn)
      .map(_.filter(_.isLetterOrDigit))

    ((price, eircode, searchResul.Negotiator) match {
      case (Some(price), Some(eircode), Some(negotiator))
          if negotiator.Email.nonEmpty || negotiator.Phone.nonEmpty =>
        Some((price, eircode, negotiator))
      case _ => None
    }).map { (price, eircode, negotiator) =>
      Record(
        at = Instant.now,
        advertUrl = s"https://www.myhome.ie${searchResul.BrochureUrl}",
        advertPrice = price,
        propertyEircode = eircode,
        propertyImageUrls = searchResul.Photos,
        contactName = negotiator.Name,
        contactPhone = negotiator.Phone,
        contactEmail = negotiator.Email
      )
    }
  }

  val stream: ZStream[ZioClient, Throwable, Record] =
    streamApiRequestContent
      .mapZIOPar(5) { getApiResponse }
      .map { _.SearchResults }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { toRecordOption }
      .collectSome

}
