package ie.deed

import ie.deed.websites.*
import zio.http.*
import scala.util.chaining.scalaUtilChainingOps

@main def hello: Unit =
  println("Hello world!")
  println(msg)

  zio.Unsafe.unsafe { implicit unsafe =>
    val config = ClientConfig.empty.requestDecompression(true)
    val run = SherryFitzIe.scrape
      .concat(DngIe.scrape)
      .debug
      .runCount
      .provide(ClientConfig.live(config), Client.fromConfig)

    zio.Runtime.default.unsafe.run(run).pipe(println)
  }

def msg = "I was compiled by Scala 3. :)"

// https://www.citizensinformation.ie/en/consumer/phone_internet_tv_and_postal_services/eircode.html
val eircodeRegex = "([A-Z][0-9]{2}|D6W) ?[A-Z0-9]{4}".r
