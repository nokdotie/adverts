package ie.nok.adverts.scraper.maherpropertyie

import ie.nok.advertisers.Advertiser
import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.adverts.{Advert, AdvertSaleStatus}
import ie.nok.adverts.services.maherpropertyie.MaherPropertyIeAdvert
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.codecs.hash.Hash
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import org.jsoup.nodes.{Document, Element}
import scala.jdk.CollectionConverters.*
import scala.util.chaining.scalaUtilChainingOps
import zio.Schedule.{fixed, recurs}
import zio.http.Client as ZioClient
import zio.stream.ZStream
import zio.{Console, ZIO, durationInt}

object Properties {
  private val getUrls: ZStream[Any, Nothing, String] =
    ZStream
      .iterate(1)(_ + 1)
      .map {
        case 1    => "https://maherproperty.ie/property-search/?type=residential"
        case page => s"https://maherproperty.ie/property-search/page/$page/?type=residential"
      }

  private def getResponse(url: String): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestBodyAsHtml(url)
      .retry(recurs(3) && fixed(1.second))

  private def selectAdvertElements(document: Document): List[Element] =
    document
      .select(".property-item")
      .asScala
      .toList

  private def selectAdvertUrls(element: Element): String =
    element.selectFirst(".detail a.more-details").attr("href")

  private def selectAdvert(url: String, document: Document): MaherPropertyIeAdvert = {
    val saleStatus = document
      .selectFirst(".status-label")
      .text
      .pipe {
        case "Residential Sales"              => AdvertSaleStatus.ForSale
        case "Residential Sales, Sale Agreed" => AdvertSaleStatus.SaleAgreed
        case "Residential Sales, Sold"        => AdvertSaleStatus.Sold
        case "Sale Agreed"                    => AdvertSaleStatus.SaleAgreed
        case "Sold"                           => AdvertSaleStatus.Sold
        case other                            => throw new Exception(s"Unknown status: $other")
      }

    val price = document
      .selectFirst(".price-and-type")
      .pipe { Option(_) }
      .map { _.text.filter(_.isDigit) }
      .flatMap { _.toIntOption }

    val description = document
      .selectFirst(".property-item .content")
      .children()
      .asScala
      .takeWhile { _.tagName != "h3" }
      .map { _.text }
      .mkString("\n")
      .trim

    val (address, eircode) = document
      .selectFirst("#property-featured-image img")
      .pipe { Option(_) }
      .fold("") { _.attr("alt") }
      .pipe { Eircode.unzip }

    val imageUrls = document
      .select("#property-detail-flexslider .flexslider img")
      .asScala
      .map { _.attr("src") }
      .toList

    val propertySizeInSqtMtr = document
      .selectFirst(".property-meta-size")
      .pipe { Option(_) }
      .fold("") { _.text }
      .filter(_.isDigit)
      .pipe { _.toIntOption }

    val bedroomsCount = document
      .selectFirst(".property-meta-bedrooms")
      .pipe { Option(_) }
      .fold("") { _.text }
      .filter(_.isDigit)
      .pipe { _.toIntOption }

    val bathroomsCount = document
      .selectFirst(".property-meta-bath")
      .pipe { Option(_) }
      .fold("") { _.text }
      .filter(_.isDigit)
      .pipe { _.toIntOption }

    MaherPropertyIeAdvert(
      url = url,
      saleStatus = saleStatus,
      priceInEur = price,
      description = description,
      address = address,
      eircode = eircode,
      imageUrls = imageUrls,
      sizeInSqtMtr = propertySizeInSqtMtr,
      bedroomsCount = bedroomsCount,
      bathroomsCount = bathroomsCount,
      createdAt = Instant.now()
    )
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    getUrls
      .mapZIOPar(5) { getResponse }
      .map { selectAdvertElements }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { selectAdvertUrls }
      .mapZIOParUnordered(5) { url =>
        getResponse(url).map { selectAdvert(url, _) }
      }
      .map { MaherPropertyIeAdvert.toAdvert }
}
