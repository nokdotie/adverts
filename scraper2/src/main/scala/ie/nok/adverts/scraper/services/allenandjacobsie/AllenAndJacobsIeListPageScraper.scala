package ie.nok.adverts.scraper.services.allenandjacobsie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object AllenAndJacobsIeListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://allenandjacobs.ie/home/sitemap")
  override def getNextPageUrl(document: Document) = None

  private val itemPageUrlsRegex = raw"https://allenandjacobs.ie/agent/\d+/property/\d+/.+".r
  override def getItemPageUrls(document: Document) =
    super
      .getItemPageUrls(document)
      .filter { url => itemPageUrlsRegex.matches(url.toString) }
}
