package ie.nok.adverts.scraper.propertypalcom

import ie.nok.http.Client
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}

object Properties {
  private case class Response(pageProps: ResponsePageProps)
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponsePageProps(
      initialState: ResponsePagePropsInitialState
  )
  private given JsonDecoder[ResponsePageProps] =
    DeriveJsonDecoder.gen[ResponsePageProps]

  private case class ResponsePagePropsInitialState(
      properties: ResponsePagePropsInitialStateProperties
  )
  private given JsonDecoder[ResponsePagePropsInitialState] =
    DeriveJsonDecoder.gen[ResponsePagePropsInitialState]

  private case class ResponsePagePropsInitialStateProperties(
      data: ResponsePagePropsInitialStatePropertiesData
  )
  private given JsonDecoder[ResponsePagePropsInitialStateProperties] =
    DeriveJsonDecoder.gen[ResponsePagePropsInitialStateProperties]

  private case class ResponsePagePropsInitialStatePropertiesData(
      results: List[ResponsePagePropsInitialStatePropertiesDataResult]
  )
  private given JsonDecoder[ResponsePagePropsInitialStatePropertiesData] =
    DeriveJsonDecoder.gen[ResponsePagePropsInitialStatePropertiesData]

  private case class ResponsePagePropsInitialStatePropertiesDataResult(
      path: String
  )
  private given JsonDecoder[ResponsePagePropsInitialStatePropertiesDataResult] =
    DeriveJsonDecoder.gen[ResponsePagePropsInitialStatePropertiesDataResult]

  private def streamApiRequestUrl(buildId: String) =
    ZStream
      .iterate(1)(_ + 1)
      .map {
        case 1    => ""
        case page => s"args=page-$page"
      }
      .map { args =>
        s"https://www.propertypal.com/_next/data/$buildId/en/search.json?preset=property-for-sale&args=republic-of-ireland&$args"
      }

  private def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestBodyAsJson(url)
      .retry(recurs(3) && fixed(1.second))
      .tapError { _ =>
        println(s"Failed: $url")
        ZIO.unit
      }

  private def getIdAndAddress(
      result: ResponsePagePropsInitialStatePropertiesDataResult
  ): Option[PropertyIdAndAddress] =
    (result.path.split('/').toList match {
      case "" :: address :: id :: Nil => Option((id, address))
      case _                          => None
    })
      .map { PropertyIdAndAddress.apply.tupled }

  def stream(
      buildId: String
  ): ZStream[ZioClient, Throwable, PropertyIdAndAddress] =
    streamApiRequestUrl(buildId)
      .mapZIOPar(5) { getApiResponse }
      .map { _.pageProps.initialState.properties.data.results }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { getIdAndAddress }
      .collectSome

}
