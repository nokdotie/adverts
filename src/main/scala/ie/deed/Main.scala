package ie.deed

import ie.deed.websites.*
import zio.http.*
import scala.util.chaining.scalaUtilChainingOps

@main def hello: Unit =
  println("Hello world!")
  println(msg)

  zio.Unsafe.unsafe { implicit unsafe =>
    val config = ClientConfig.empty.requestDecompression(true)
    val run = ie.sherryfitz
      // ie.dng
      .scrape()
      .provide(ClientConfig.live(config), Client.fromConfig)

    zio.Runtime.default.unsafe.run(run).pipe(println)
  }

def msg = "I was compiled by Scala 3. :)"
