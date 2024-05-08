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
import ie.nok.adverts.AdvertFacet

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
        facets = List(AdvertFacet("https://www.daft.ie/for-sale/-/5611174")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("5545956") {
    ScraperHelper.assertItemPageScraperResults(
      "services/daftie/items/5545956.html",
      "https://www.daft.ie/for-sale/-/5545956",
      DaftIeItemPageScraper,
      Advert(
        advertUrl = "https://www.daft.ie/for-sale/-/5545956",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 320000,
        propertyIdentifier = Hash.encode("Apartment 54, Priory Court, Delgany, Co. Wicklow"),
        propertyDescription = Some("""
          |Built by the Cosgrave Group in 2005, this well-presented modern apartment forms part of the award-winning Eden Gate residential development, located directly opposite an expansive green area and positioned on the first floor overlooking the development's well-maintained private grounds.
          |
          |At the hub of the home is an exceptionally large, light-filled open-plan kitchen/dining/living room with a fitted contemporary kitchen and an opening that leads directly to a large, timber deck balcony.
          |
          |The property has two larger-than-average double bedrooms (one ensuite), both with their own double glass doors opening directly on to the balcony. A good-sized main bathroom, entrance hall and internal hallway complete the accommodation.
          |
          |In turnkey condition, the property is newly repainted throughout in light-toned colours, which emphasise the already abundant natural light flooding through the apartment's large windows. Outside, the balcony is surrounded by top quality railings and recently enhanced with a composite timber deck.
          |
          |Managed by Wyse, Eden Gate features extensive, well-maintained landscaped amenity spaces for residents to enjoy, featuring a succession of greens, mature trees and planting areas.
          |
          |Call us today to arrange a viewing of this must-see modern apartment.
          |
          |
          |LOCATION
          |Eden Gate is a mature mixed development of modern apartments, terraced and standalone houses set in well-maintained landscaped grounds conveniently located 2.3 kilometres from Delgany village centre with its popular selection of restaurants, shops, pub, bakery, convenience store, butcher, fishmonger and greengrocer.
          |
          |It is also within easy reach (3.8km) of Greystones town centre with its bustling array of supermarkets, shops, boutiques, stores and other retail outlets, along with the great selection of pubs, cafes, bistros, restaurants and amenities that make the town such a popular lifestyle choice.
          |
          |The Delgany/Greystones area is home to a wide variety of sports clubs including soccer, rugby, GAA, golf, tennis, sailing, cricket and rowing. Top local leisure amenities include Greystones Marina and the Shoreline Leisure Centre, as well as the town's popular beaches and seafront walk. A variety of Wicklow Mountain trails are also within easy driving distance.
          |
          |Commuting to Dublin couldn't be easier thanks to the close proximity of the N11 and Greystones DART station. Local bus routes include the 184 and the 84, while the Aircoach from Greystones provides a direct link to Dublin airport.
          |
          |An excellent choice of local schools includes nearby Delgany National School, Gaelscoil na gCloch Liath, St. Kevin's, St. Patrick's, St. Brigid's and St. Laurence's national schools, and Temple Carrig, St. David's Holy Faith and Charlesland Community secondary schools. There's also a good choice of pre-school and Montessori schools.
          |
          |
          |ACCOMMODATION
          |Entrance hall (3.56 x 1.2m): with:
          |Laminate wooden floor
          |Recessed LED ceiling lights
          |Good sized hot press with shelved storage directly off
          |
          |Internal hall (3.77 x 1.2m): with:
          |Laminate wooden floor
          |Recessed LED ceiling lights
          |
          |Kitchen/living/dining room (9.99 x 3.56m): extensive open-plan living space overlooking communal grounds, with:
          |Contemporary fitted kitchen with floor and eye-level cabinets, extensive polished stone countertops, recessed stainless steel one-and-a-half sink and integrated appliances
          |Tiled splashback in kitchen
          |Laminate wooden floor in kitchen
          |Carpet in living/dining room
          |Large picture windows with Venetian blinds
          |Glass door opening directly out on to balcony
          |
          |Bedroom 1 (4.75 x 2.73m): overlooking communal grounds, with:
          |Ensuite bathroom
          |Carpet
          |Built-in double wardrobe
          |Pole mounted curtains
          |Double glass doors opening directly out on to balcony
          |
          |Ensuite bathroom (1.5 x 1.54m): off Bedroom 1, with contemporary fixtures, fittings and sanitary ware, including:
          |WC and WHB with fixed mirror and light above
          |Walk-in corner shower cubicle with curved glass screen and sliding door
          |Half-wall tiling
          |Built in, recessed shelving
          |
          |Bedroom 2 (4.75 x 2.57m): overlooking communal grounds, with:
          |Carpet
          |Built-in double wardrobe
          |Pole mounted curtains
          |Double glass doors opening directly out on to balcony
          |
          |Main bathroom (2.23 x 1.7m): with contemporary fixtures, fittings and sanitary ware, including:
          |WC and WHB with fixed mirror above
          |Full-length bath with shower
          |Half-wall tiling
          |
          |OUTSIDE
          |Balcony (4.92 x 1.52m): Brand new, timber composite decked balcony accessed directly from living/dining room and both bedrooms via glass double doors.
          |
          |FEATURES
          |Immaculate modern apartment in ready-to-move-in condition with high standard of fittings and finishes throughout
          |Exceptionally spacious, well-proportioned kitchen/living/dining room
          |Two, larger-than-average double bedrooms
          |Spacious, newly installed balcony decking creates inviting 'room outside'
          |Kitchen accommodates complete range of integrated appliances, comprising fridge-freezer (Zanussi), electric oven (Indesit), hob (Zanussi) with extractor fan, dishwasher (Beko) and washing machine (Hotpoint)
          |All rooms newly repainted in pale muted colours, enhancing natural light
          |Storage heaters throughout
          |Double glazed PVC windows
          |Durable laminate flooring in hall and kitchen
          |Quality carpet in living/dining room and both bedrooms
          |WiFi
          |First floor location within secure development
          |Intercom access control to ground floor entrance door
          |
          |
          |OUTSIDE FEATURES
          |Apartments set within own private grounds, featuring paths, lawns and variety of mature plants, shrubs and trees
          |Gated entrance to apartment footprint with intercom controlled access
          |Internal communal areas cleaned regularly
          |Regular outdoor maintenance and gardening service
          |Allocated single parking space close to entrance
          |Ample visitor parking outside building
          |Convenient location within easy walking distance of neighbourhood shopping centre
          |
          |Annual management fee: €2,068 P/A - property managed by Wyse
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "Apartment 54, Priory Court, Delgany, Co. Wicklow",
        propertyEircode = Some(Eircode("A63HN77")),
        propertyCoordinates = Coordinates(
          latitude = 53.125251,
          longitude = -6.078172
        ),
        propertyImageUrls = List(
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjE0NDAsImhlaWdodCI6OTYwfX0sIm91dHB1dEZvcm1hdCI6ImpwZWciLCJrZXkiOiIzLzUvMzUxMjU1N2YwODE1ZmYyYzc1OTcwMWI2ZmYxNWI5MjkuanBnIn0=?signature=15630c5e498fa6165bbe646a0bb5ee24e502ed2e67478f666fa4038552ac5be2",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6IjUvYS81YTQ4YzlmMTZkYWY2ZTg0OWY1MDJlZWMyZTdkYWQ2MC5qcGcifQ==?signature=420a77fcc6aba88aab5392546527f53abdfd60fe4307db53b10b4f97aaf92db4",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6ImIvYy9iYzg3OGRjZjQwMzZhZWJmODU2NWI1NDg2YTA2NjYzYy5qcGcifQ==?signature=3bb82bd4dce03e9d6153036691fdbd7dbf1a988d29657894d2a59ad6f37a5f08",
          "https://media.daft.ie/eyJidWNrZXQiOiJtZWRpYW1hc3Rlci1zM2V1IiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6ImNvdmVyIiwid2lkdGgiOjM2MCwiaGVpZ2h0IjoyNDB9fSwib3V0cHV0Rm9ybWF0IjoianBlZyIsImtleSI6IjQvMy80MzdjOTRlNjU4NTcwZjdhNzdmMDVjMWQ1ZjJkMmM1MC5qcGcifQ==?signature=e3b2bb5477854a764d9f7b2bf8ad7f2ff4660051f4d6bd73b6788f57b0b878a2"
        ),
        propertySize = Area(83, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 83,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = Some(109536789),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(160.59),
        facets = List(AdvertFacet("https://www.daft.ie/for-sale/-/5545956")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("5590770 - property type") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5590770.html",
      "https://www.daft.ie/for-sale/-/5590770"
    )

    val propertyType = DaftIeItemPageScraper.getPropertyType(document)
    assertEquals(propertyType, Some(PropertyType.Detached))
  }

  test("5350705 - price") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5350705.html",
      "https://www.daft.ie/for-sale/-/5350705"
    )

    val price = DaftIeItemPageScraper.getPriceInEur(document)
    assertEquals(price, 0)
  }

  test("5350705 - size") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5350705.html",
      "https://www.daft.ie/for-sale/-/5350705"
    )

    val size = DaftIeItemPageScraper.getSize(document)
    assertEquals(size, Area(1.56, AreaUnit.Acres))
  }

  test("5645812 - ber") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5645812.html",
      "https://www.daft.ie/for-sale/-/5645812"
    )

    val ber = DaftIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(ber, None)
  }

  test("5508432 - address") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5508432.html",
      "https://www.daft.ie/for-sale/-/5508432"
    )

    val address = DaftIeItemPageScraper.getAddress(document)
    assertEquals(address, "Chandos Lane, Chandos Lane, Road, Dundrum, Co. Dublin")
  }

  test("4715649 - ber") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/4715649.html",
      "https://www.daft.ie/for-sale/-/4715649"
    )

    val ber = DaftIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(ber, None)
  }

  test("5291883 - filter") {
    val document = ScraperHelper.getDocument(
      "services/daftie/items/5291883.html",
      "https://www.daft.ie/for-sale/-/5291883"
    )

    val filter = DaftIeItemPageScraper.filter(document)
    assertEquals(filter, false)
  }
}
