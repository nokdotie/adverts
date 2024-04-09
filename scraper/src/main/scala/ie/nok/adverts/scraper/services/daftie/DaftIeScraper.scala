package ie.nok.adverts.scraper.services.daftie

import java.net.URL
import ie.nok.adverts.scraper.services.ServiceScraper

object DaftIeScraper extends ServiceScraper {
  override val getInitialListPageUrl = new URL("https://www.daft.ie/property-for-sale/ireland")
  override val getListPageScraper    = DaftIeListPageScraper
  override val getItemPageScraper    = DaftIeItemPageScraper
}
