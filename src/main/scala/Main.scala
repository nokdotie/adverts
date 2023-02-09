import com.microsoft.playwright.*
import java.nio.file.Paths

@main def hello: Unit =
  println("Hello world!")
  println(msg)

  val playwright = Playwright.create()
  val browser = playwright.webkit().launch()
  val page = browser.newPage()
  page.navigate("http://whatsmyuseragent.org/")
  page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")))

def msg = "I was compiled by Scala 3. :)"
