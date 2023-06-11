package ie.nok.adverts.dooglasNewmanGood

import ie.nok.adverts.Advert
import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import java.time.Instant
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

def toAdvert(
    property: Properties.ResponseDataProperty,
    negotiator: Negotiators.ResponseDataTeam
): Option[Advert] =
  property.price
    .zip(property.address.postcode)
    .map { (price, postcode) =>
      Advert(
        at = Instant.now(),
        advertUrl = s"https://www.dng.ie/property-for-sale/-${property.id}",
        advertPrice = price,
        propertyEircode = postcode,
        propertyImageUrls =
          property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
            image => image.url.orElse(image.srcUrl)
          },
        contactName = negotiator.Name,
        contactPhone = negotiator.Phone.orElse(negotiator.Mobile_No),
        contactEmail = Option(negotiator.Email)
      )
    }

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .map { property =>
        Option(property)
          .zip(Properties.getNegotiatorEmail(property))
      }
      .collectSome
      .mapZIOParUnordered(5) { (property, negotiatorEmail) =>
        Negotiators
          .getByEmail(negotiatorEmail)
          .map { Option(property).zip }
      }
      .collectSome
      .map { toAdvert.tupled }
      .collectSome
      .debug("Property")
      .pipe { File.createTempJsonLinesFile(_) }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("dng.ie", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(ClientConfig.default, Client.fromConfig)
}
