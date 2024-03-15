package ie.nok.adverts.seo.services

import ie.nok.seo.indexnow.ZIndexNowService
import zio.ZIO

object IndexNow {
  def inform(changed: List[String]): ZIO[ZIndexNowService, Throwable, Unit] =
    ZIndexNowService
      .submit(changed)
}
