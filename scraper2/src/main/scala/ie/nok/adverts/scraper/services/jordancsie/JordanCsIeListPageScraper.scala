package ie.nok.adverts.scraper.services.jordancsie

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}

import java.net.URI

object JordanCsIeListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URI("https://jordancs.ie/sitemap.xml").toURL
  )
}
