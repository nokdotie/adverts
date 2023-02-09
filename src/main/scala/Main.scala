import com.microsoft.playwright.*
import java.nio.file.Paths

@main def hello: Unit =
  println("Hello world!")
  println(msg)

  val playwright = Playwright.create()
  val browser = playwright.webkit().launch()

  ie.sherryfitz.scrape(browser)

def msg = "I was compiled by Scala 3. :)"
