package ie.nok.adverts.scraper.services.daftie

import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.adverts.scraper.services.ScraperHelper
import ie.nok.ber.Rating
import ie.nok.codecs.hash.Hash
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.net.URL
import java.time.Instant

class DaftIeItemPageScraperTest extends munit.FunSuite {

  test("5611174") {
    ScraperHelper.assertItemPageScraperResults(
      "services/daftie/items/5611174.html",
      "https://www.daft.ie/for-sale/-/5611174",
      DaftIeItemPageScraper,
      Advert(
        advertUrl = "https://www.daft.ie/for-sale/-/5611174",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 265000,
        propertyIdentifier = Hash.encode("91 Great William O'Brien Street, Blackpool, Blackpool, Co. Cork"),
        propertyDescription = Some("""
          |CONTACT SALES AGENT DIARMUID LYNCH FOR A VIEWING 0872622382
          |Dan Howard &amp; Co. Limited are delighted to bring this luxury beautifully decorated three bedroom duplex to the market. The property is in superb condition having been refurbished throughout with a high level of insulation and quality finish. The residence would make an ideal home or investment property.
          |
          |Ground Floor
          |Cloak closet and understairs storage
          |
          |First Floor
          |Living Room: 3.6m x 2.8m
          |Kitchen/Dining Room: 3.1m x 2.8m - modern fitted kitchen incorporating hob, oven, washer, dryer and fridge freezer
          |Bathroom: 3.2m x 1.65m - beautifully decorated with 3 piece white suite
          |
          |Second Floor
          |Bedroom 1: 3.2m x 2.87m - built in robe and fully tiled ensuite off
          |Bedroom 2: 3.0m x 2.2m - built in robe
          |Bedroom 3: 3.0m x 1.7m - built in robe
          |Guest WC
          |Attic entrance with access ladder
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Terraced),
        propertyAddress = "91 Great William O'Brien Street, Blackpool, Blackpool, Co. Cork",
        propertyEircode = Some(Eircode("T23N4C2")),
        propertyCoordinates = Coordinates(
          latitude = 51.911103,
          longitude = -8.474326
        ),
        propertyImageUrls = List(
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjE0NDAsImhlaWdodCI6OTYwfX0sIm91dHB1dEZvcm1hdCI6ImpwZWciLCJrZXkiOiIzLzEvMzE4MzAyZGQzMjFmOGIwOThmNmQ0ZjRjNTY0ZjUzYWIuanBnIn0=?signature=a1d9ebbfd4c934bf69188499d39a4cdfd7b5fe96e55b03275d45893cb34d5731",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6IjMvYi8zYjY5MDFlZGNlYWUzZmQ0OTZhM2UxNjc1Nzk5Njc0ZC5qcGcifQ==?signature=501c13a912b4965a20a1352bf7c4c351ae27ed6d3ca63963c86421b9f472ab59",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6IjAvMi8wMjI4ZmJlZDZlMjQ2ZGM2Y2UzOTk1M2IzMmRhYmQ3NC5qcGcifQ==?signature=0e4aa57dce571f27b51247879c5a688d0bfbc613903a35d146ef694bb4152eb4",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6ImYvNi9mNmFiMmM2NjFlMDZhYzc4MzYzNjI3YThmNmZmMzQ5ZS5qcGcifQ==?signature=f5766f23e8c53cedf33569ba024cd18429a04850ca0a8fb3d0ff624adaaf9c49"
        ),
        propertySize = Area(92, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 92,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.C3),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

}
