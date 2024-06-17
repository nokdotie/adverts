package ie.nok.adverts.scraper.services.realestateallianceie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object RealEstateAllianceIeListPageScraper extends SitemapListPageScraper {
  override def getFirstPageUrl()                  = URL("https://www.realestatealliance.ie/sitemaps/propertySearchSitemap?sta=forSale&sta=saleAgreed&st=sale")
  override def getNextPageUrl(document: Document) = None
}
