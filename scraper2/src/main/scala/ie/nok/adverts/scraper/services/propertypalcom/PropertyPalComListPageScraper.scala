package ie.nok.adverts.scraper.services.propertypalcom

import ie.nok.adverts.scraper.services.{SitemapListPageScraper, SelectedListPageScraper}
import java.net.URL

object PropertyPalComListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-carlow"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-cavan"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-clare"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-cork"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-donegal"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-dublin"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-galway"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-kerry"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-kildare"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-kilkenny"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-laois"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-leitrim"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-limerick"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-longford"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-louth"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-mayo"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-meath"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-monaghan"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-offaly"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-roscommon"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-sligo"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-tipperary"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-waterford"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-westmeath"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-wexford"),
    URL("https://www.propertypal.com/sitemaps/property-for-sale/county-wicklow")
  )
}
