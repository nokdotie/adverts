package ie.deed

import zio._
import zio.config._
import zio.config.magnolia.descriptor
import zio.config.typesafe.TypesafeConfigSource

case class ScraperConfig(
    env: String,
    numberOfRetries: Int,
    numberOfPar: Int,
    uploadToStorage: Boolean
)

object ScraperConfig {
  val layer: ZLayer[Any, ReadError[String], ScraperConfig] =
    ZLayer {
      read {
        descriptor[ScraperConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("ScraperConfig"))
        )
      }
    }
}
