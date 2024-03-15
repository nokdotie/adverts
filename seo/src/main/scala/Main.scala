package ie.nok.adverts.seo

import ie.nok.adverts.{Advert, AdvertUrl}
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.seo.indexnow.ZIndexNowServiceImpl
import ie.nok.seo.google.search.ZIndexingServiceImpl
import ie.nok.stores.compose.ZFileAndGoogleStorageStoreImpl
import zio.{Console, ZIOAppDefault}

object Main extends ZIOAppDefault {
  def run = (for {
    today <- AdvertStoreImpl.readAndDecodeLatest
    todayUrl = today.map(AdvertUrl.fromAdvert)
    _ <- Console.printLine(s"Today: ${today.size}")

    yesterday <- AdvertStoreImpl.readAndDecodeYesterday
    yesterdayUrl = yesterday.map(AdvertUrl.fromAdvert)
    _ <- Console.printLine(s"Yesterday: ${yesterday.size}")

    added = todayUrl.diff(yesterdayUrl)
    _ <- Console.printLine(s"Added: ${added.size}")

    deleted = yesterdayUrl.diff(todayUrl)
    _ <- Console.printLine(s"Deleted: ${deleted.size}")

    _ <- services.Google.notify(added, deleted)
    _ <- services.IndexNow.inform(added ++ deleted)
  } yield ())
    .provide(
      ZFileAndGoogleStorageStoreImpl.layer[Advert],
      ZIndexingServiceImpl.layer,
      ZIndexNowServiceImpl.layer
    )
}
