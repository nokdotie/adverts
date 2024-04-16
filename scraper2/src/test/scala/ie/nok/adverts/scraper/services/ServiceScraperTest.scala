package ie.nok.adverts.scraper.services

class ServiceScraperTest extends munit.FunSuite {

  test("all unique") {
    val allServices = ServiceScraper.all.map { _.getService() }
    assertEquals(allServices.distinct, allServices)
  }

}
