package ie.nok.adverts.scraper.maherpropertyie

import ie.nok.adverts.Advert
import ie.nok.advertisers.Advertiser
import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.ecad.Eircode
import ie.nok.hash.Hasher
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import org.jsoup.nodes.{Document, Element}
import scala.jdk.CollectionConverters._
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, Console, ZIO}
import zio.http.{Client => ZioClient}
import zio.Schedule.{recurs, fixed}
import zio.stream.ZStream

object Properties {
    private val getUrls: ZStream[Any, Nothing, String] =
        ZStream
            .iterate(1)(_ + 1)
            .map {
                case 1    => "https://maherproperty.ie/property-search/"
                case page => s"https://maherproperty.ie/property-search/page/$page/"
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

    private def selectAdvertUrls(element: Element): Option[String] = {
        val figCation = element.selectFirst("figure figcaption").text
        val url = element.selectFirst(".detail a.more-details").attr("href")

        Option.when(figCation == "Residential Sales")(url)
    }

    private def selectAdvert(url: String, document: Document, advertiser: Option[Advertiser]): Advert = {
        println("Processing advert: " + url)

        val price = document
            .selectFirst(".price-and-type")
            .pipe { Option(_) }
            .map { _.text.filter(_.isDigit) }
            .flatMap { _.toIntOption }
            .getOrElse(0)

        val (address, eircode) = document
            .selectFirst("#property-featured-image img")
            .pipe { Option(_) }
            .fold("") { _.attr("alt") }
            .pipe { Eircode.unzip }

        val imageUrls = document
            .select(".property-detail-flexslider .flexslider img")
            .asScala
            .map { _.attr("src") }
            .toList

        val propertySizeInSqtMtr = document
            .selectFirst(".property-meta-size")
            .pipe { Option(_) }
            .fold("") { _.text }
            .filter(_.isDigit)
            .pipe { _.toIntOption }
            .getOrElse(0)

        val bedroomsCount = document
            .selectFirst(".property-meta-bedrooms")
            .pipe { Option(_) }
            .fold("") { _.text }
            .filter(_.isDigit)
            .pipe { _.toIntOption }
            .getOrElse(0)

        val bathroomsCount = document
            .selectFirst(".property-meta-bath")
            .pipe { Option(_) }
            .fold("") { _.text }
            .filter(_.isDigit)
            .pipe { _.toIntOption }
            .getOrElse(0)

        Advert(
            advertUrl= url,
            advertPriceInEur = price,
            propertyIdentifier = Hasher.hash(address),
            propertyAddress= address,
            propertyEircode= eircode,
            propertyCoordinates = Coordinates.zero,
            propertyImageUrls = imageUrls,
            propertySize = Area(propertySizeInSqtMtr, AreaUnit.SquareMetres),
            propertySizeInSqtMtr = propertySizeInSqtMtr,
            propertyBedroomsCount = bedroomsCount,
            propertyBathroomsCount = bathroomsCount,
            propertyBuildingEnergyRating = Option.empty,
            propertyBuildingEnergyRatingCertificateNumber = Option.empty,
            propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Option.empty,
            sources = List.empty,
            advertiser = advertiser,
            createdAt = Instant.now()
        )
    }

    val advertiser: ZIO[AdvertiserStore, Throwable, Option[Advertiser]] =
        AdvertiserStore.getByPropertyServicesRegulatoryAuthorityLicenceNumber("002231")

    val stream: ZStream[ZioClient & AdvertiserStore, Throwable, Advert] =
        ZStream.fromZIO(advertiser)
            .flatMap { advertiser =>
                getUrls
                    .mapZIOPar(5) { getResponse }
                    .map { selectAdvertElements }
                    .takeWhile { _.nonEmpty }
                    .flattenIterables
                    .map { selectAdvertUrls }
                    .collectSome
                    .mapZIOPar(5) { url =>
                        getResponse(url).map { selectAdvert(url, _, advertiser) }
                    }
            }
}
