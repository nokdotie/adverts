package ie.nok.adverts.scraper.services.maherpropertyie

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
import ie.nok.advertisers.stores.AdvertiserStoreInMemory

class MaherPropertyIeItemPageScraperTest extends munit.FunSuite {

  test("R93Y327") {
    ScraperHelper.assertItemPageScraperResults(
      "services/maherpropertyie/items/R93Y327.html",
      "https://maherproperty.ie/property/18-millbrook-mill-lane-carlow-town-co-carlow/",
      MaherPropertyIeItemPageScraper,
      Advert(
        advertUrl = "https://maherproperty.ie/property/18-millbrook-mill-lane-carlow-town-co-carlow/",
        advertSaleStatus = AdvertSaleStatus.Sold,
        advertPriceInEur = 165000,
        propertyIdentifier = Hash.encode("18 Millbrook, Mill Lane, Carlow Town, Co. Carlow"),
        propertyDescription = Some("""
        |Well maintained four-bedroom ground floor apartment comes to the market in good condition. Located in the heart of Carlow Town adjacent to Carlow Castle. Located within walking distance to all Carlow’s amenities, the apartment is located in a predominantly student complex. The property comprises of four large bedrooms, shower room, family bathroom and a generously sized open-plan Kitchen living area which comes fully fitted. . This is an excellent investment opportunity for any investor. The property is generally rented out on a Fixed-term 9-month contract yielding €14,400 with revisionary potential, and currently represents a return of 8.7% Management fees are a discounted payment structure of €1,650 per annum, if paid within the financial year, further information available on request. Viewings come highly recommended and by appointment only.
        |
        |
        |Property Features
        |
        |
        |- Excellent investment opportunity
        |
        |
        |- Gas fired central heating
        |
        |
        |- PVC double glazed windows
        |
        |
        |- Ground floor apartment
        |
        |
        |- Management fees €1650 discounted
        |
        |
        |- Great location &amp; close too many local amenities
        |
        |
        |BER No: 116188590
        |
        |
        |Energy Performance Indicator: 158.63 kWh/m2/yr
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "18 Millbrook, Mill Lane, Carlow Town, Co. Carlow",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 52.835504556363,
          longitude = -6.9367289543152
        ),
        propertyImageUrls = List(
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG_4791-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG_4793-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-2724-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-2453-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4271-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4266-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4267-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4270-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4269-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-4265-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-2721-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-2456-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/02/IMG-2454-scaled.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://maherproperty.ie/property/18-millbrook-mill-lane-carlow-town-co-carlow/")),
        advertiser = Some(AdvertiserStoreInMemory.maherPropertyIe),
        createdAt = Instant.now()
      )
    )
  }

  test("R93FP29") {
    ScraperHelper.assertItemPageScraperResults(
      "services/maherpropertyie/items/R93FP29.html",
      "https://maherproperty.ie/property/21-beech-road-rivercourt-carlow-town-co-carlow-r93fp29/",
      MaherPropertyIeItemPageScraper,
      Advert(
        advertUrl = "https://maherproperty.ie/property/21-beech-road-rivercourt-carlow-town-co-carlow-r93fp29/",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 175000,
        propertyIdentifier = Hash.encode("21 Beech Road, Rivercourt, Carlow Town, Co. Carlow"),
        propertyDescription = Some("""
          |Well presented two bed ground floor apartment located in Rivercourt just off O’Brien Road. The property uniquely has the added benefit of small private garden to the rear with side access. The property is within walking distance to Fairgreen Shopping Centre, creches, primary and secondary schools, and is adjacent to Eire Óg GAA Grounds. Carlow has all local amenities all in close proximity including a Tesco, Aldi, Lidl. There is also local transport nearby with Carlow Town bus stop a few minutes walk, Carlow bus station is 15 minutes away and Carlow train station is a 25 minute walk from the property.
          |
          |
          |The property comprises entrance hallway, fully fitted kitchen with access to garden, separate sitting room to the front, with two double bedrooms, both with fitted wardrobes and family bathroom. There is currently no management company in place for the development with owners paying for insurance separately.
          |
          |
          |Viewing is strictly by appointment only.
          |
          |
          |Entrance Hallway: L: 3.797m x W: 1.171m Laminate flooring,
          |
          |
          |Hot Press Sitting Room: L: 5.412m x W: 2.965m Laminate Flooring, Venetian Blinds
          |
          |
          |Kitchen: L: 5.737m x W: 2.352m Laminate Flooring, Wall &amp; Floor Kitchen unit, white goods, patio door to garden
          |
          |
          |Bathroom: L: 1.712m x W: 1.808m Tiled Flooring &amp; Walls, bath WC, WHB
          |
          |
          |Bedroom 1: L: 3.339m x W: 2.918m Laminate Flooring, Fitted Wardrobe
          |
          |
          |Bedroom 2: L: 2.973m x W: 2.304m Laminate Flooring, Fitted wardrobe
          |
          |
          |Disclaimer: The above particulars are issued by Maher Property Advisors on the understanding that all negotiations are conducted through them. Please note that we have not tested any apparatus, fixtures, fittings, or services. Interested parties must undertake their own investigation into the working order of these items. All measurements are approximate and photographs provided for guidance only
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "21 Beech Road, Rivercourt, Carlow Town, Co. Carlow",
        propertyEircode = Some(Eircode("R93FP29")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8671-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8672-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8675-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8681-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8682-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8683-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8687-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8688-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8690-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8691-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8693-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8694-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8713-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8714-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8715-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8716-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8717-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8718-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2023/11/IMG_8719-scaled.jpg"
        ),
        propertySize = Area(66, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 66,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 1,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(198.41),
        facets = List(AdvertFacet("https://maherproperty.ie/property/21-beech-road-rivercourt-carlow-town-co-carlow-r93fp29/")),
        advertiser = Some(AdvertiserStoreInMemory.maherPropertyIe),
        createdAt = Instant.now()
      )
    )
  }

  test("R93X8C4") {
    ScraperHelper.assertItemPageScraperResults(
      "services/maherpropertyie/items/R93X8C4.html",
      "https://maherproperty.ie/property/67-ashfield-blackbog-road-carlow-r93-x8c4/",
      MaherPropertyIeItemPageScraper,
      Advert(
        advertUrl = "https://maherproperty.ie/property/67-ashfield-blackbog-road-carlow-r93-x8c4/",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 265000,
        propertyIdentifier = Hash.encode("67 Ashfield, Blackbog Road, Carlow"),
        propertyDescription = Some("""
          |Well-presented four bed semi-detached house in an established, quiet estate, on the edge of Carlow town. The estate contains 71 houses and is a mixture of three bed and four bed houses with large green areas, and is within a short drive to Carlow town and all its amenities.
          |
          |
          |The property has been freshly redecorated throughout, and comes to the market in very good condition. The south east facing enclosed garden has accessed through side gate boasts one of the largest gardens in the estate, allowing the new owner plenty of scope for a future extension subject to relevant planning permission
          |
          |
          |The property comprises entrance hallway, large double bedroom to the left, sitting room to the right and kitchen to the rear with adjoining utility and guest WC. Upstairs comprises family bathroom, double bedroom to the rear, single bedroom to the front, master bedroom to the front with en-suite.
          |Viewing is highly recommended and strictly by appointment only through Maher Property Advisors.
          |
          |
          |Accommodation:
          |Entrance hallway- lino flooring
          |Lounge- Carpets, open fire, bay window
          |Kitchen- Lino flooring, floor and wall units, tiled splashback, patio doors to garden and access to utility and Guest WC
          |Utility 1.570m x3.073m-Tiled flooring, guest WC
          |Guest WC 0.890m X 1.742m-Tiled floor, WC, WHB, tiled splashback
          |Double Bedroom 2.595m x 5.150m- Carpets, dual aspect bedroom, attic space
          |Landing- Carpet flooring
          |Family bathroom 1.936m x 2.139m- Lino flooring, WC, WHB, Bath, part tiled wall
          |Double Bedroom 3.189m x 3.596m- Carpet, fitted wardrobes
          |Master Bedroom- carpet, fitted wardrobes
          |Ensuite 1.878m x 1.730m- lino flooring, WC, WHB, Part tiled walls, shower unit
          |Single Bedroom 2.445m x 2.369m- Carpet, fitted wardrobes
          |Disclaimer: The above particulars are issued by Maher Property Advisors on the understanding that all negotiations are conducted through them. Please note that we have not tested any apparatus, fixtures, fittings, or services. Interested parties must undertake their own investigation into the working order of these items. All measurements are approximate and photographs provided for guidance only.
          """.stripMargin.trim),
        propertyType = Some(PropertyType.SemiDetached),
        propertyAddress = "67 Ashfield, Blackbog Road, Carlow",
        propertyEircode = Some(Eircode("R93X8C4")),
        propertyCoordinates = Coordinates(
          latitude = 52.819759268745,
          longitude = -6.9308173656464
        ),
        propertyImageUrls = List(
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-1.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-2.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-3.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-4.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-5.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-6.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-7.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-8.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-9.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-10.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-11.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-12.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-13.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-14.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-15.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-16.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-17.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-18.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-20.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-19.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-21.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-22.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-23.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-24.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-25.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/67_Ashfield_Maher-26.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/1_67-Ashfield_Ground-scaled.jpg",
          "https://maherproperty.ie/wp-content/uploads/2024/03/2_67-Ashfield_First-scaled.jpg"
        ),
        propertySize = Area(109, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 109,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://maherproperty.ie/property/67-ashfield-blackbog-road-carlow-r93-x8c4/")),
        advertiser = Some(AdvertiserStoreInMemory.maherPropertyIe),
        createdAt = Instant.now()
      )
    )
  }
}
