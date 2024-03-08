package ie.nok.adverts.seo

import ie.nok.adverts.Advert
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.google.search.ZIndexingService
import ie.nok.stores.compose.ZFileAndGoogleStorageStoreImpl
import zio.{Console, ZIOAppDefault}
import zio.stream.ZStream

object Main extends ZIOAppDefault {
  def url(advert: Advert): String =
    s"https://nok.ie/properties/${advert.propertyIdentifier}"

  def run = (for {
    today <- AdvertStoreImpl.readAndDecodeLatest
    _     <- Console.printLine(s"Today: ${today.size}")

    yesterday <- AdvertStoreImpl.readAndDecodeYesterday
    _         <- Console.printLine(s"Yesterday: ${yesterday.size}")

    added = today.map(url).diff(yesterday)
    _ <- ZStream
      .fromIterable(added)
      .mapZIOParUnordered(5) { url =>
        // ZIndexingService.update(url)
        Console.printLine(s"Add: $url")
      }
      .runDrain
    _ <- Console.printLine(s"Added: ${added.size}")

    deleted = yesterday.map(url).diff(today)
    _ <- ZStream
      .fromIterable(deleted)
      .mapZIOParUnordered(5) { url =>
        // ZIndexingService.delete(url)
        Console.printLine(s"Delete: $url")
      }
      .runDrain
    _ <- Console.printLine(s"Deleted: ${deleted.size}")
  } yield ())
    .provide(
      ZFileAndGoogleStorageStoreImpl.layer[Advert]
      // ZIndexingService.layer
    )
}
