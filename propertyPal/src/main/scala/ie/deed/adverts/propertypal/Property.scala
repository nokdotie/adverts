
package ie.nok.adverts.propertypal

import ie.nok.adverts.Record
import ie.nok.adverts.utils.Eircode
import ie.nok.adverts.utils.zio.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant
import zio.stream.ZPipeline

object Property {
  private case class Response(pageProps: ResponsePageProps)
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponsePageProps(
      property: ResponsePagePropsProperty
  )
  private given JsonDecoder[ResponsePageProps] = DeriveJsonDecoder.gen[ResponsePageProps]

  private case class ResponsePagePropsProperty(
      shareURL: String,
      price: ResponsePagePropsPropertyPrice,
      postcode: String,
      images: List[ResponsePagePropsPropertyImage],
      contacts: ResponsePagePropsPropertyContacts,
  )
  private given JsonDecoder[ResponsePagePropsProperty] = DeriveJsonDecoder.gen[ResponsePagePropsProperty]

  private case class ResponsePagePropsPropertyPrice(price: Option[Int])
  private given JsonDecoder[ResponsePagePropsPropertyPrice] = DeriveJsonDecoder.gen[ResponsePagePropsPropertyPrice]

  private case class ResponsePagePropsPropertyImage(url: String)
  private given JsonDecoder[ResponsePagePropsPropertyImage] = DeriveJsonDecoder.gen[ResponsePagePropsPropertyImage]

  private case class ResponsePagePropsPropertyContacts(
    default: ResponsePagePropsPropertyContactsDefault,
  )
  private given JsonDecoder[ResponsePagePropsPropertyContacts] = DeriveJsonDecoder.gen[ResponsePagePropsPropertyContacts]

  private case class ResponsePagePropsPropertyContactsDefault(
      organisation: String,
      phoneNumber: ResponsePagePropsPropertyContactsDefaultPhoneNumber,
  )
  private given JsonDecoder[ResponsePagePropsPropertyContactsDefault] = DeriveJsonDecoder.gen[ResponsePagePropsPropertyContactsDefault]

  private case class ResponsePagePropsPropertyContactsDefaultPhoneNumber(
    international: String
  )
  private given JsonDecoder[ResponsePagePropsPropertyContactsDefaultPhoneNumber] = DeriveJsonDecoder.gen[ResponsePagePropsPropertyContactsDefaultPhoneNumber]

  private def getApiRequestUrl(propertyIdAndAddress: PropertyIdAndAddress): String =
    s"https://www.propertypal.com/_next/data/ijabnC9g5QxxQMjkdO83R/en/property.json?address=${propertyIdAndAddress.address}&id=${propertyIdAndAddress.id}"

  private def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestJson(url)
      .retry(recurs(3) && fixed(1.second))

  private def toRecordOption(
      property: ResponsePagePropsProperty
  ): Option[Record] = {
    val price = property.price.price
    val eircode = property.postcode.pipe { postcode =>
      Option.when(postcode.nonEmpty)(postcode)
    }

    price.zip(eircode)
    .map { (price, eircode) =>
      Record(
        at = Instant.now,
        advertUrl = property.shareURL,
        advertPrice = price,
        propertyEircode = eircode,
        propertyImageUrls = property.images.map(_.url),
        contactName = property.contacts.default.organisation,
        contactPhone = property.contacts.default.phoneNumber.international.pipe(Option.apply),
        contactEmail = Option.empty
      )
    }
  }

  val pipeline: ZPipeline[ZioClient, Throwable, PropertyIdAndAddress, Record] =
    ZPipeline
      .map { getApiRequestUrl }
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.pageProps.property }
      .map { toRecordOption }
      .collectSome

}
