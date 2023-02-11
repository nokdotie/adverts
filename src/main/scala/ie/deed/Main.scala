package ie.deed

import ie.deed.websites.*

@main def hello: Unit =
  println("Hello world!")
  println(msg)

  // ie.sherryfitz.scrape()
  ie.dng.scrape()

def msg = "I was compiled by Scala 3. :)"
