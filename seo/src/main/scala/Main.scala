package ie.nok.adverts.seo

import ie.nok.adverts.{Advert, AdvertUrl}
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.google.search.{ZIndexingService, ZIndexingServiceImpl}
import ie.nok.stores.compose.ZFileAndGoogleStorageStoreImpl
import zio.{Console, ZIOAppDefault}
import zio.stream.ZStream

object Main extends ZIOAppDefault {
  def run = (for {
    today <- AdvertStoreImpl.readAndDecodeLatest
    todayUrl = today.map(AdvertUrl.fromAdvert)
    _ <- Console.printLine(s"Today: ${today.size}")

    yesterday <- AdvertStoreImpl.readAndDecodeYesterday
    yesterdayUrl = yesterday.map(AdvertUrl.fromAdvert)
    _ <- Console.printLine(s"Yesterday: ${yesterday.size}")

    added = todayUrl.diff(yesterdayUrl)
    _ <- ZStream
      .fromIterable(added)
      .mapZIOParUnordered(5) { ZIndexingService.update }
      .runDrain
    _ <- Console.printLine(s"Added: ${added.size}")

    deleted = yesterdayUrl.diff(todayUrl)
    _ <- ZStream
      .fromIterable(deleted)
      .mapZIOParUnordered(5) { ZIndexingService.delete }
      .runDrain
    _ <- Console.printLine(s"Deleted: ${deleted.size}")
  } yield ())
    .provide(
      ZFileAndGoogleStorageStoreImpl.layer[Advert],
      ZIndexingServiceImpl.layer
    )
}
