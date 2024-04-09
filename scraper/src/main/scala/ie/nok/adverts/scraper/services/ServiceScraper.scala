package ie.nok.adverts.scraper.services

import java.net.URL
import org.jsoup.nodes.Document

trait ServiceScraper {

  def getInitialServiceListPageUrl(): URL
  def getServiceListPageScraper(document: Document): ServiceListPageScraper
  def getServiceItemPageScraper(document: Document): ServiceItemPageScraper

}
