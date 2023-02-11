package ie.deed.websites.ie.dng

import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.stream.ZStream
import zio.http.Client
import zio.json._
import zio._
import zio.http.model.Headers
import zio.http.*
import org.jsoup.Jsoup
import collection.convert.ImplicitConversions.*
import scala.util.Using
import util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.stream.ZStream
import zio.http.Client
import zio.json._
import zio._
import zio.http.model.Headers
import zio.http.netty.client.{NettyClientDriver, NettyConnectionPool}
import zio.http.*
import org.jsoup.Jsoup

case class PropertyList(hits: List[PropertyListHit])
object PropertyList {
  implicit val decoder: JsonDecoder[PropertyList] =
    DeriveJsonDecoder.gen[PropertyList]
}
case class PropertyListHit(
    price: Option[Int],
    display_address: String,
    images: List[PropertyListHitImage],
    negotiator_details: Option[PropertyListHitNegotiatorDetails]
)
object PropertyListHit {
  implicit val decoder: JsonDecoder[PropertyListHit] =
    DeriveJsonDecoder.gen[PropertyListHit]
}
case class PropertyListHitImage(
    `440x320`: String,
    `352x264`: String,
    `336x244`: String,
    `1600x1066`: String
)
object PropertyListHitImage {
  implicit val decoder: JsonDecoder[PropertyListHitImage] =
    DeriveJsonDecoder.gen[PropertyListHitImage]
}
case class PropertyListHitNegotiatorDetails(
    name: String,
    email: Option[String]
)
object PropertyListHitNegotiatorDetails {
  implicit val decoder: JsonDecoder[PropertyListHitNegotiatorDetails] =
    DeriveJsonDecoder.gen[PropertyListHitNegotiatorDetails]
}

private def getPropertyList(
    page: Int
): ZIO[Client, Object, List[PropertyListHit]] =
  val url =
    s"https://cqbonqqrvd-dsn.algolia.net/1/indexes/prod_properties?x-algolia-api-key=ea067c87d4903eb77190e3189057a7a5&x-algolia-application-id=CQBONQQRVD&attributesToRetrieve=price,display_address,images,negotiator_details&page=$page"
  val originHeader = Headers("Origin", "https://www.dng.ie/")

  Client
    .request(url, headers = originHeader)
    .retryN(3)
    .flatMap { _.body.asString }
    .flatMap { _.fromJson[PropertyList].pipe(ZIO.fromEither) }
    .map { _.hits }

def scrape() =
  val foo = ZStream
    .iterate(0)(_ + 1)
    .mapZIOPar(5) { getPropertyList }
    .takeWhile { _.nonEmpty }
    .flattenIterables
    .foreach(zio.Console.printLine(_))

  zio.Unsafe.unsafe { implicit unsafe =>
    val config = ClientConfig.empty.requestDecompression(true)
    val run = foo.provide(ClientConfig.live(config), Client.fromConfig)

    zio.Runtime.default.unsafe.run(run).pipe(println)
  }
