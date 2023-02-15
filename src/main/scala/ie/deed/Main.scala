package ie.deed

import zio._
import zio.http.*
import zio.Console._
import ie.deed.websites.*

object Main extends ZIOAppDefault:

  val config = ClientConfig.empty.requestDecompression(true)
  def run =
    SherryFitzIe.scrape
      .concat(DngIe.scrape)
      .debug
      .runCount
      .provide(ClientConfig.live(config), Client.fromConfig)
