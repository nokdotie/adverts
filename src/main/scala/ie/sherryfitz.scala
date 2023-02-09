package ie.sherryfitz

import com.microsoft.playwright.Browser
import collection.convert.ImplicitConversions.*
import scala.util.Using
import util.chaining.scalaUtilChainingOps

enum Page(val url: String):
    case Search(page: Int) extends Page(s"https://www.sherryfitz.ie/properties/search?sort_by=created_at&sort_order=desc&page=$page")
    case Property(path: String) extends Page(s"https://www.sherryfitz.ie/$path")

def scrape(browser: Browser): Unit =
    scrapeSearch(browser, Page.Search(1))

private def scrapeSearch(browser: Browser, search: Page.Search): Unit =
  val pages = Using.resource(browser.newPage()) { page =>
    page.navigate(search.url)

    val nextSearchPage = page
      .isVisible(".search-results-pagination .pagination-next")
      .pipe { Option.when(_)(Page.Search(search.page + 1)) }

    val propertyPages = page
      .locator(".search_results_loop .property-card-container .property-card-action a")
      .all()
      .map { _.getAttribute("href") }
      .map { Page.Property.apply }

    propertyPages ++ nextSearchPage
  }

  pages.foreach {
    case search: Page.Search => scrapeSearch(browser, search)
    case property: Page.Property => scrapeProperty(browser, property)
  }

private def scrapeProperty(browser: Browser, property: Page.Property): Unit =
  val found = Using.resource(browser.newPage()) { page =>
    page.navigate(property.url)

    val address = page.locator(".property-address h1").textContent().trim().replaceAll("\\s+", " ")
    val price = page.locator(".property-price").textContent().trim().replaceAll("\\s+", " ")
    val agentName = page.locator(".agent-card-details-name").textContent().trim().replaceAll("\\s+", " ")
    val agentPhone = page.locator(".agent-card-details-contact-phone a").textContent().trim().replaceAll("\\s+", " ")

    (property.url, address, price, agentName, agentPhone)
  }

  println(s"Found: $found")
