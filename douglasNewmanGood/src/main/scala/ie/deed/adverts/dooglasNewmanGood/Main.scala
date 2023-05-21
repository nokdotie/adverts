package ie.deed.adverts.dooglasNewmanGood

import ie.deed.adverts.Record
import ie.deed.adverts.utils.gcp.GoogleCloudStorage
import ie.deed.adverts.utils.zio.File
import java.time.Instant
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

def toRecord(
    property: Properties.ResponseDataProperty,
    negotiator: Negotiators.ResponseDataTeam
): Option[Record] =
  (
    property.price,
    property.address.postcode,
    negotiator.Phone.orElse(negotiator.Mobile_No)
  )
    .pipe {
      case (Some(price), Some(postcode), Some(phone)) =>
        Some((price, postcode, phone))
      case _ => None
    }
    .map { (price, postcode, phone) =>
      Record(
        at = Instant.now(),
        advertUrl = s"https://www.dng.ie/property-for-sale/-${property.id}",
        advertPrice = price,
        propertyEircode = postcode,
        propertyImageUrls =
          property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
            image => image.url.orElse(image.srcUrl)
          },
        contactName = negotiator.Name,
        contactPhone = phone,
        contactEmail = negotiator.Email
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
      .mapZIOPar(5) { (property, negotiatorEmail) =>
        Negotiators
          .getByEmail(negotiatorEmail)
          .map { Option(property).zip }
      }
      .collectSome
      .map { toRecord.tupled }
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
