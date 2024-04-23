package ie.nok.adverts.scraper.services.fivesie

import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.adverts.scraper.services.ScraperHelper
import ie.nok.ber.Rating
import ie.nok.codecs.hash.Hash
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.net.URL
import java.time.Instant

class FiveSIeItemPageScraperTest extends munit.FunSuite {

  test("H91P3K2") {
    ScraperHelper.assertItemPageScraperResults(
      "services/fivesie/items/H91P3K2.html",
      "https://www.5s.ie/property/for-sale-lakeview-apartment-galway-city/",
      FiveSIeItemPageScraper,
      Advert(
        advertUrl = "https://www.5s.ie/property/for-sale-lakeview-apartment-galway-city/",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 580000,
        propertyIdentifier = Hash.encode("9 Tobar Na Mban, Lough Atalia., Galway"),
        propertyDescription = Some("""
          |Description
          |
          |
          |Sophisticated Lakeview Apartment located on Lough Atalia Road, Galway City.
          |
          |5SRE are proud to present this luxurious third floor two bedroom apartment overlooking the stunning views of Lough Atalia and Galway Bay. Situated within close proximity to Galway’s vibrant City Centre and all its local amenities. A highly sought after location with a perfect blend of convenience and desirability. This fabulous home has been professionally renovated by the highly renowned BLUMA interior design label.
          |
          |This home boasts high ceilings with floor to ceiling windows both in the front and back of the apartment allowing maximum light throughout, illuminating your living space and creating a welcoming ambiance that enhances the spaciousness of every room. This residence features glass doors that seamlessly merge with the refined elegance of its furnishings, leading out to an enclosed balcony area with astonishing panoramic views of Lough Atalia.
          |
          |This Lakeview Apartment in Galway City also comes with two underground, secure parking spaces.
          |
          |Guest access to the block is via a secure coded keypad system.
          |
          |Heating is by means of a gas centralised heating system. With an efficient B3 Energy rating, this property offers a fantastic, hassle-free, exclusive lifestyle choice in a prestigious development on the Wild Atlantic Way. The previous occupants highlighted how easy it was to warm this inviting home.
          |
          |Key Features:
          |
          |
          |- Bedrooms: 2 large Super King bedrooms
          |
          |- Bathrooms: 1 large bathroom and one en-suite of the master bedroom
          |
          |- Parking: 2 designated underground parking spaces
          |
          |- Storage: Private indoor storage unit
          |
          |- Double glazed windows
          |
          |- Safe &amp; secure building in a friendly area
          |
          |- Miele electric oven and hob, fridge, microwave extractor and washer-dryer
          |
          |- Contemporary/modern décor – Interior design by BLUMA
          |
          |- Open plan kitchen, dining room &amp; living room
          |
          |- Outdoor space: Front balcony
          |
          |- Heating: Gas
          |
          |- Location: On Lough Atalia, less than a 10-minute walk from Eyre Square and the heart of Galway City Centre
          |
          |- Management Fee €2750 per year
          |
          |- Year Built: 2008
          |
          |
          |Original features: Gas fireplace, double glazed windows &amp; doors, breakfast bar, floor to ceiling windows in living room, spacious foyer, floor to ceiling wardrobes, vast waterfront views, high ceilings. Super bright.
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "9 Tobar Na Mban, Lough Atalia., Galway",
        propertyEircode = Some(Eircode("H91P3K2")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://www.5s.ie/wp-content/uploads/2023/12/Sitting-Room-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2288-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2378-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Living-area-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Kitchen-table-view-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Kitchen-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Hall-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2335-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Bedroom-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2330-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2363-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2359-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/MG_2356-HDR-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2023/12/Balcony-1170x600.jpeg",
          "https://www.5s.ie/wp-content/uploads/2022/02/Photo-18-08-2020-15-49-20-1-1170x600.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("H91E9F2") {
    ScraperHelper.assertItemPageScraperResults(
      "services/fivesie/items/H91E9F2.html",
      "https://www.5s.ie/property/for-sale-6-college-gate-lower-newcastle-road-galway/",
      FiveSIeItemPageScraper,
      Advert(
        advertUrl = "https://www.5s.ie/property/for-sale-6-college-gate-lower-newcastle-road-galway/",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 380000,
        propertyIdentifier = Hash.encode("6 college gate, lower newcastle road, galway"),
        propertyDescription = Some("""
          |Description
          |
          |
          |5S Real Estate is proud to present 6 College Gate, Lower Newcastle Road, Galway for sale by private treaty.
          |
          |This pristine three bedroom, four bathroom apartment boasts a highly desirable, safe location in Galway City. Good solid investment – previous rent was €1950 pcm.
          |
          |All local services and amenities are on its doorstep including coffee shops, supermarkets, schools, bank and hospital. It is situated adjoining the main campus of NUIG.
          |
          |Tucked away to the back of the development this bright &amp; airy two story apartment, with its own door entrances, is generously proportioned at 81 m²/872 ft². A private storage shed and a designated car parking space are included.
          |
          |On entering the property, you will be immediately impressed with just how bright the apartment is. The ground floor consists of a large modern kitchen/diner &amp; living room which overlook the manicured lawn area. Light appears to bounce off the hardwood floors, stainless steel kitchen appliances and fittings
          |
          |The soft tone of the carpeted stairs combined with the toughened glass handrail and overhead velux window accentuates the feeling of space in the property. Each of the three sun filled bedrooms are en-suite with built in wardrobes and hardwood flooring.
          |
          |The apartment is ready to go having been refurbished and freshly painted.
          |
          |To arrange a viewing, please contact our Galway Sales Division on 091 860836 or the agent for the sale, Alan on 085 777 7774.
          |
          |For similar type properties for sale through 5S Real Estate, please see here
          |
          |If you are considering selling and wondering how much your home is worth, give us a call for a free, no-obligation valuation
          |
          |
          |Please Note: These particulars are not to be considered a formal offer. They are for information only and give a general idea of the property. They are not to be taken as forming any part of a resulting contract, nor to be relied upon as statements or representations of fact. Whilst every care is taken in their preparation, neither 5S nor the vendor accept any liability as to their accuracy. Intending purchasers must satisfy themselves by personal inspection or otherwise as to the correctness of these particulars. No person in the employment of 5S has any authority to make or give any representation or warranty whatever in relation to this property.
          """.stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "6 college gate, lower newcastle road, galway",
        propertyEircode = None,
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://www.5s.ie/wp-content/uploads/2021/07/External-from-road-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/External3-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/External-back-garden2-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Internal-hallway-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Downstairs-hallway-showing-stairs-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/living-dining-rm-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/internal-grd-floor2-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/internal-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Kitchen-2-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Bedroom-1.4-ensuite-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Bedroom1-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Bedroom2-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/En-suite3-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Hotpress-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Bedroom-1.4-ensuite-1-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Downstairs-hallway-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/External-from-road2-1170x600.jpg",
          "https://www.5s.ie/wp-content/uploads/2021/07/Hallway-1170x600.jpg"
        ),
        propertySize = Area(81, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 81,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 4,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

}
