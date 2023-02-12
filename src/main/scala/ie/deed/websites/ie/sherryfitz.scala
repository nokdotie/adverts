package ie.deed.websites.ie.sherryfitz

import collection.convert.ImplicitConversions.*
import util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.stream.ZStream
import zio.json._
import zio.http.model.Headers
import zio.http.{Client, ZClient, Body, Response}
import org.jsoup.Jsoup

private case class FeatureCollection(features: List[Feature])
object FeatureCollection {
  implicit val decoder: JsonDecoder[FeatureCollection] =
    DeriveJsonDecoder.gen[FeatureCollection]
}
private case class Feature(properties: Property)
object Feature {
  implicit val decoder: JsonDecoder[Feature] = DeriveJsonDecoder.gen[Feature]
}
private case class Property(
    price: String,
    address: String,
    link: String,
    image: String,
    ber: String,
    beds: String,
    baths: String,
    size: String
)
object Property {
  implicit val decoder: JsonDecoder[Property] = DeriveJsonDecoder.gen[Property]
}

private def getPropertyList(page: Int): ZIO[Client, Object, List[Feature]] =
  val url =
    s"https://www.sherryfitz.ie/sfdev/api/properties/?type=all&sort=created_at&order=desc&page=$page"
  val refererHeader = Headers("Referer", "https://www.sherryfitz.ie/")

  Client
    .request(url, headers = refererHeader)
    .flatMap { _.body.asString }
    .flatMap { _.fromJson[FeatureCollection].pipe(ZIO.fromEither) }
    .map { _.features }

private def getProperty(path: String) =
  val url = s"https://www.sherryfitz.ie/$path"

  Client
    .request(url)
    .flatMap { _.body.asString }
    .flatMap { html => ZIO.attempt { Jsoup.parse(html) } }
    .map { doc =>
      val agentName =
        doc.select(".agent-card-details-name").text.trim.replaceAll("\\s+", " ")
      val agentPhone = doc
        .select(".agent-card-details-contact-phone a")
        .text
        .trim
        .replaceAll("\\s+", " ")
      val images =
        doc.select(".property-image-element img").map { _.attr("src") }

      (agentName, agentPhone, images)
    }

def scrape(): ZIO[ZClient[Any, Body, Throwable, Response], Object, Unit] =
  ZStream
    .iterate(1)(_ + 1)
    .mapZIOPar(5) { getPropertyList }
    .takeWhile { _.nonEmpty }
    .flattenIterables
    .mapZIOPar(5) { property =>
      getProperty(property.properties.link)
        .map { (property, _) }
    }
    .foreach(zio.Console.printLine(_))
