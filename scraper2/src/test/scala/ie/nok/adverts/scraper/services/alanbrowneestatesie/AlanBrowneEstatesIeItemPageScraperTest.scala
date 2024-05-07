package ie.nok.adverts.scraper.services.alanbrowneestatesie

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

class AlanBrowneEstatesIeItemPageScraperTest extends munit.FunSuite {

  test("T12V4K3") {
    ScraperHelper.assertItemPageScraperResults(
      "services/alanbrowneestatesie/items/T12V4K3.html",
      "https://alanbrowneestates.ie/property.php?property=T12%20V4K3",
      AlanBrowneEstatesIeItemPageScraper,
      Advert(
        advertUrl = "https://alanbrowneestates.ie/property.php?property=T12%20V4K3",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 425000,
        propertyIdentifier = Hash.encode("2 Ardfallen Road, Douglas, Cork"),
        propertyDescription = Some("""
          |Alan Browne Estates are delighted to offer for sale No. 2 Ardfallen Road, Douglas, Cork. T12 V4K3
          |
          |
          |Ardfallen Estate is a super location that is within walking distance of all amenities including
          |
          |
          |Douglas Village, Cork city, Gus Healy swimming pool, Saint Anthony's National School and Ballinlough Tennis Club.
          |
          |
          |This 3 bedroomed house is presented in great condition throughout. It has the benefit of off street parking for 1 car to the front, cobble lock driveway, and a well maintained private south facing lawn to the rear.
          |
          |
          |Accommodation includes:
          |
          |Ground floor: Hallway, 2 living-rooms, kitchen, sun room and attached garage
          |
          |First floor has 3 spacious bedrooms and family bathroom..
          |
          |
          |Welcome to 2 Ardfallen Road, Douglas, Cork.
          |
          |
          |Entrance hall: PVC front door. Bright and welcoming hallway. Carpet flooring. Under-stairs storage. Radiator
          |
          |
          |Family-room: 4.3 m x 3.3 m plus bay window
          |
          |A spacious and bright family room with a large front bay window. Carpet flooring. Open fire with tiled surround. TV point. Radiator.
          |
          |
          |Living-room: 4 m x 3.55 m
          |
          |Another bright and spacious room. Carpet flooring. Open fireplace with tiled surround. Sliding doors to conservatory. Radiator. TV poiint
          |
          |
          |Kitchen: 4.3 m x 2.9 m
          |
          |Lino flooring. Built in kitchen storage units. Window over looking south facing back garden. Plumbed for washing machine. Radiator.
          |
          |
          |Attached garage:
          |
          |Remote controlled roller door. Ideal storage area.
          |
          |
          |Carpeted stairs and landing. Window on landing return.
          |
          |
          |Bedroom 1: 4.35 m x 3.95 m
          |
          |Bright and spacious double bedroom. Carpet flooring. Window. Radiator.
          |
          |
          |Bedroom 2: 3.5 m x 3.45 m
          |
          |Another spacious double bedroom. Carpet flooring. Built in wall to wall robes. Radiator. Bay window.
          |
          |
          |Bedroom 3: 3 m x 2.4 m
          |
          |Spacious single bedroom. Carpet flooring. Built in robe. Radiator. Window.
          |
          |
          |Bathroom:
          |
          |3 piece bathroom suite. Electric shower. Lino flooring. Window.
          |
          |
          |Outside.
          |
          |Front: Off street parking for 2 cars on cobble lock driveway. Well maintained lawn area.
          |
          |Private: South facing private garden to the rear.
          |
          |
          |Gas fired central heating.
          |
          |
          |Underpinned in 2004
          |
          |
          |Double glazed windows
          |
          |
          |Viewings are very welcome but STRICTLY by prior appointment only.
          |
          |Contact Alan Browne on 086-8547861
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "2 Ardfallen Road, Douglas, Cork",
        propertyEircode = Some(Eircode("T12V4K3")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/cover/front.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619320.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619331.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619342.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619353.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619364.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619375.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619386.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619397.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619408.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/17147309619419.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 V4K3/171473096194210.jpg"
        ),
        propertySize = Area(105, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 105,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 1,
        propertyBuildingEnergyRating = Some(Rating.E1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://alanbrowneestates.ie/property.php?property=T12%20V4K3")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("T12C4HF") {
    ScraperHelper.assertItemPageScraperResults(
      "services/alanbrowneestatesie/items/T12C4HF.html",
      "https://alanbrowneestates.ie/property.php?property=T12%20C4HF",
      AlanBrowneEstatesIeItemPageScraper,
      Advert(
        advertUrl = "https://alanbrowneestates.ie/property.php?property=T12%20C4HF",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 525000,
        propertyIdentifier = Hash.encode("20 Ashleigh Drive, Blackrock, Cork"),
        propertyDescription = Some("""
          |Alan Browne Estates are delighted to offer for sale this very spacious, bright and well presented 4 bedroomed semi detached home, situated in Corks premier suburb of Blackrock. No.20 Ashleigh Drive will make a superb family home.
          |
          |
          |No. 20 Ashleigh Drive is perfectly situated being within walking distance of a host of amenities including Mahon Point/ City Gate complex, a number of sporting grounds (Blackrock Hurling Club, Pairc Ui Chaoimh, Pairc Ui Rinn and Cork Constitution), Blackrock/Rochestown greenway and Marina riverside amenity walk. There is a selection of primary and secondary schools within close proximity. Easy access to the South-Link road network.
          |
          |
          |This is a very bright and spacious 4 bedroom home that is presented in excellent condition throughout. This lovely property has accommodation extending to c.1,500 square feet. This well loved family home property has been maintained to a very high standard.
          |
          |
          |Accommodation includes:
          |
          |Ground floor: Hallway, living-room, kitchen, dining-room, sunroom, bedroom/playroom and guest wc with shower.
          |
          |First floor has 3 spacious bedrooms and a modern family bathroom.
          |
          |
          |Outside: A cobble lock drive way with 2 parking spaces
          |
          |To the rear there is a lovely south facing garden with an elevated patio area.
          |
          |
          |Welcome to 20 Ashleigh Drive, Blackrock, Cork. T12 C4HF
          |
          |
          |Entrance hall: Access via composite front door. Bright, spacious and welcoming hallway. Solid flooring. Radiator.
          |
          |
          |Family-room: 4.5 m x 3.6 m
          |
          |A lovely bright and very spacious family room. Solid wood flooring. Open fire with marble and wood surround.TV point. Large window. Radiator. French doors link to dining-room.
          |
          |
          |Kitchen:
          |
          |A bright and airy kitchen naturally illuminated by a large south facing window over the sink. Modern built in floor and wall units providing extensive storage. Tiled flooring and tiled splash back. Integrated oven and hob. Plumbed for washing machine and dishwasher. Radiator. Door to back patio and garden.
          |
          |
          |Dining-room: 3.6 m and 2.85 m
          |
          |Located adjacent to the kitchen and links onto sunroom via French doors. Solid wood flooring. Radiator.
          |
          |
          |Sun-room: A wonderful sun trap given its southerly aspect. Wall to wall surround windows with sliding doors to back patio and garden. Solid wood flooring. TV point.
          |
          |
          |Bedroom 4/ Playroom: 3.65 m x 2.65 m
          |
          |This room is located off the entrance hallway. It can be utilised a s 4th bedroom, playroom or second living area. Naturally illumiated by a large bay window. Solid wood flooring. TV point.
          |
          |
          |Guest wc and shower: Mains shower. Fully tiled floor, wall and shower surround. Toilet and wash hand basin. Window.
          |
          |
          |First floor:
          |
          |Carpeted stairs and landing.
          |
          |
          |Bedroom 1:3.6 m x 3.6 m
          |
          |Very spacious and bright double bedroom. Laminate wood flooring. Built in wall to wall robes. Window. Radiator.
          |
          |
          |Bedroom 2: 3.6 m x 3.1 m
          |
          |Another spacious and bright double bedroom. Wood flooring. Built in robes. Radiator. Window.
          |
          |
          |Bedroom 3: 3 m x 2 m
          |
          |Large single bedroom. Laminate wood flooring. Radiator. Window.
          |
          |
          |Bathroom:
          |
          |Modern bathroom suite. Tiled floor, walls and shower surround. Mains shower. Window. Towel radiator.
          |
          |
          |Hot press: Ample storage.
          |
          |
          |Pull down attic stairs.
          |
          |
          |Outside: Private parking for 2 cars on cobble lock driveway to the front. Planted border.
          |
          |Large enclosed south facing lawn with elevated patio area to the rear.
          |
          |
          |Zoned Gas fired central heating.
          |
          |Double glazed windows.
          |
          |
          |Viewings are very welcome but STRICTLY by prior appointment only.
          |
          |Contact Alan Browne on 086-8547861
          |
          |
          |JOINT AGENT: GARRY O DONNELL ERA DOWNEY Mc CARTHY
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "20 Ashleigh Drive, Blackrock, Cork",
        propertyEircode = Some(Eircode("T12C4HF")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/cover/front.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574860.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574871.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574882.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574893.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574904.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574915.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574926.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574937.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574948.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/17095792574959.jpg",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/IMG_9316.JPG",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/IMG_9323.JPG",
          "https://alanbrowneestates.ie/img/prop/T12 C4HF/IMG_9325.JPG"
        ),
        propertySize = Area(140, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 140,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://alanbrowneestates.ie/property.php?property=T12%20C4HF")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("T45CK11 - ber") {
    val document = ScraperHelper.getDocument(
      "services/alanbrowneestatesie/items/T45CK11.html",
      "https://alanbrowneestates.ie/property.php?property=T45%20CK11"
    )

    val ber = AlanBrowneEstatesIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(ber, Some(Rating.D1))

    val cert = AlanBrowneEstatesIeItemPageScraper.getBuildingEnergyRatingCertificateNumber(document)
    assertEquals(cert, Some(116108903))
  }

  test("E34Y236 - ber") {
    val document = ScraperHelper.getDocument(
      "services/alanbrowneestatesie/items/E34Y236.html",
      "https://alanbrowneestates.ie/property.php?property=E34%20Y236"
    )

    val ber = AlanBrowneEstatesIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(ber, Some(Rating.B3))

    val cert = AlanBrowneEstatesIeItemPageScraper.getBuildingEnergyRatingCertificateNumber(document)
    assertEquals(cert, Some(116697541))
  }

  test("CMAC - price") {
    val document = ScraperHelper.getDocument(
      "services/alanbrowneestatesie/items/CMAC.html",
      "https://alanbrowneestates.ie/property.php?property=SiteCMAC"
    )

    val price = AlanBrowneEstatesIeItemPageScraper.getPriceInEur(document)
    assertEquals(price, 0)
  }
}
