package ie.nok.adverts.scraper.myhomeie

import ie.nok.ber.Rating
import ie.nok.adverts.Advert
import ie.nok.adverts.services.myhomeie.MyHomeIeAdvert
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}

object Properties {
  private case class Response(SearchResults: List[ResponseSearchResult])
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseSearchResult(
      PriceAsString: Option[String],
      BathString: Option[String],
      BedsString: String,
      SizeStringMeters: Option[Float],
      DisplayAddress: String,
      BrochureUrl: String,
      Photos: List[String],
      BrochureMap: Option[ResponseSearchResultBrochureMap],
      BerRating: Option[String]
  )
  private given JsonDecoder[ResponseSearchResult] =
    DeriveJsonDecoder.gen[ResponseSearchResult]

  private case class ResponseSearchResultBrochureMap(
      longitude: BigDecimal,
      latitude: BigDecimal
  )
  private given JsonDecoder[ResponseSearchResultBrochureMap] =
    DeriveJsonDecoder.gen[ResponseSearchResultBrochureMap]

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
      .requestBodyAsJson[Response](
        "https://api.myhome.ie/search",
        method = Method.POST,
        headers = contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def toMyHomeIeAdvert(
      searchResult: ResponseSearchResult
  ): MyHomeIeAdvert = {
    val url = s"https://www.myhome.ie${searchResult.BrochureUrl}"
    val price = searchResult.PriceAsString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val coordinates = searchResult.BrochureMap.map { bm =>
      Coordinates(
        latitude = bm.latitude,
        longitude = bm.longitude
      )
    }

    val size = searchResult.SizeStringMeters
      .map { BigDecimal(_) }
      .map { Area(_, AreaUnit.SquareMetres) }

    val bedroomsCount = searchResult.BedsString
      .filter(_.isDigit)
      .toIntOption

    val bathroomsCount = searchResult.BathString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val buildingEnergyRating = searchResult.BerRating
      .flatMap { Rating.tryFromString(_).toOption }

    MyHomeIeAdvert(
      url = url,
      priceInEur = price,
      address = searchResult.DisplayAddress,
      coordinates = coordinates,
      imageUrls = searchResult.Photos,
      size = size,
      bedroomsCount = bedroomsCount,
      bathroomsCount = bathroomsCount,
      buildingEnergyRating = buildingEnergyRating,
      createdAt = Instant.now
    )
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    streamApiRequestContent
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.SearchResults }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { toMyHomeIeAdvert }
      .map { MyHomeIeAdvert.toAdvert }

}
