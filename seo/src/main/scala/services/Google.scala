package ie.nok.adverts.seo.services

import ie.nok.seo.google.search.{ZIndexingService, ZIndexingServiceImpl}
import zio.ZIO
import zio.stream.ZStream

object Google {
  def notify(added: List[String], deleted: List[String]): ZIO[ZIndexingService, Throwable, Unit] =
    for {
      _ <- ZStream
        .fromIterable(added)
        .mapZIOParUnordered(5) { ZIndexingService.update }
        .runDrain

      _ <- ZStream
        .fromIterable(deleted)
        .mapZIOParUnordered(5) { ZIndexingService.delete }
        .runDrain
    } yield ()
}
