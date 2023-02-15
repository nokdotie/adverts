package ie.deed

import zio._
import zio.http._
import zio.json._
import zio.stream._
import ie.deed.websites._
import java.io.File
import scala.util.chaining._

object Main extends ZIOAppDefault:
  def run =
    SherryFitzIe.scrape
      .concat(DngIe.scrape)
      .pipe {
        val file = File.createTempFile("tmp", ".jsonl")
        json.writeJsonLinesAs(file, _).map { _ => file.getAbsolutePath }
      }
      .debug("File")
      .provide(ClientConfig.default, Client.fromConfig)
