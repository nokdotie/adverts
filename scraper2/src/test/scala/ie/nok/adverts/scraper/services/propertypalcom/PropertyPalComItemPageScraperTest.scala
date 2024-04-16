package ie.nok.adverts.scraper.services.propertypalcom

import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.adverts.scraper.services.ScraperHelper
import ie.nok.ber.Rating
import ie.nok.codecs.hash.Hash
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.net.URL
import java.time.Instant

class PropertyPalComItemPageScraperTest extends munit.FunSuite {

  test("4785119") {
    ScraperHelper.assertItemPageScraperResults(
      "services/propertypalcom/items/841658.html",
      "https://www.propertypal.com/four-winds-tullow-road-carlow/841658",
      PropertyPalComItemPageScraper,
      Advert(
        advertUrl = "https://www.propertypal.com/four-winds-tullow-road-carlow/841658",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 565000,
        propertyIdentifier = Hash.encode("Four Winds, Tullow Road, Carlow"),
        propertyDescription = Some("""
        |Oozing character, Four Winds sits proudly on an exceptional and elevated c.1 acre site on the fringes of Carlow town.&nbsp; Approached by electronic gates, the property is surrounded by an extensive and beautifully landscaped mature garden with interesting and different vistas from all corners.
        |There is extensive living space including a wraparound living/office area with views of the gardens from almost every window.&nbsp;
        |All rooms are generously proportioned and this is, without question, one of the finest family &nbsp;homes to come to the market in the last decade.
        |Carlow town centre can be accessed on foot by footpath all the way.&nbsp;
        |Access to the M9 motorway is less than 10 minutes drive linking Carlow to Dublin, Kilkenny and Waterford
        |&nbsp;
        |ACCOMMODATION
        |Ground Floor
        |Hallway - Maple flooring, wc off
        |Kitchen: Tiled, storage at floor and eye level – Wood burning stove, oven &amp; hobUtility: Tiled floor, sink unitFamily Room/Office: Tiled Floor
        |Sitting Room: Carpet, Impressive cut stone fireplace to ceiling height
        |Dining Room: Carpet
        |Study/Snug: Cast iron fireplace, solid oak floor
        |Stairs &amp; Landing to:
        |First Floor
        |Master Bedroom: Shower, vanity unit, fitted Sliderobes, carpet.&nbsp;
        |Guest Bedroom: Hotpress, vanity unit, wardrobe, laminate floor
        |Bedroom 3: Wall to wall wardrobe, vanity unit t&amp;g floor
        |Bedroom 4: Double wardrobe, vanity unit, t&amp;g floor
        |Bedroom 5/Nursery/Study
        |Bathroom: wc, whb, shower
        |Outside:&nbsp;&nbsp;
        |Lofted double garage, loft area for storage and currently used for home entertainment.&nbsp;
        |Secure rear yard with access off the wraparound living area.&nbsp; Ideal for those with children.&nbsp;
        |Fish pond, water feature &amp; fountain
        |Features:
        |Security gate,
        |Security system including motion activated light, seven cameras,
        |Emergency lighting on ground &amp; first floors.&nbsp;
        |OFCH,
        |Double glazed uPVC windows.&nbsp;
        |C.1km from town centre, filling station, Lidl, pharmacy, schools, all within an easy walk along with St Lawrence O Tooles Athletic Club and Eire Og GFC located closeby.&nbsp;&nbsp;
        |&nbsp;BER Details
        |BER Rating: C3
        |BER No.: 115482093
        |Energy Performance Indicator: Not provided
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Detached),
        propertyAddress = "Four Winds, Tullow Road, Carlow",
        propertyEircode = Some(Eircode("R93Y893")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/840458/35049341.jpg",
          "https://media.propertypal.com/hd/p/840458/35049342.jpg",
          "https://media.propertypal.com/hd/p/840458/35049375.jpg"
        ),
        propertySize = Area(238.14, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 238.14,
        propertyBedroomsCount = 5,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C3),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("912502") {
    ScraperHelper.assertItemPageScraperResults(
      "services/propertypalcom/items/912502.html",
      "https://www.propertypal.com/granite-cottge-link-road-chapelstown-carlow/912502",
      PropertyPalComItemPageScraper,
      Advert(
        advertUrl = "https://www.propertypal.com/granite-cottge-link-road-chapelstown-carlow/912502",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 399000,
        propertyIdentifier = Hash.encode("Granite Cottge, Link Road, Chapelstown, Carlow"),
        propertyDescription = Some("""
        |REA Dawson are delighted to present a truly unique and beautifully presented family home located in a much sought after area close to Carlow town.
        |Dating back to the mid 1800s the property has been sympathetically extended over the years to provide unique, bright and spacious living accommodation over two floors with a ground floor bedroom and 3 further bedrooms on first floor; entrance porch sittingroom, livingroom and spacious kitchen/dining area complete the accommodation. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        |In addition, the property has a studio to the rear ideal for working from home or conversion to additional living accommodation if desired. There is off street parking for a number of cars, private back garden and pleasant views of Browneshill House and surrounding countryside to the front.
        |The Accommodation Comprises:
        |Entrance porch leading to &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        |Living Room with timber floor and cast iron fireplace
        |Spacious Living Room with solid fuel stove, timber floor, double doors to
        |Kitchen/Dining Room with tiled floor, feature brick wall, natural timber kitchen with wooden counter, plumbed for washing machine &amp; dishwasher.
        |Utility area with downstairs wc, whb&nbsp;&nbsp;
        |Bedroom No. 4 ground floor&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        |First Floor
        |Master Bedroom with built in wardrobes, timber floor
        |Bedroom No. 2 with timber floor
        |Bedroom No. 3 with timber floor
        |Family Bathroom with shower, wc, whb
        |Outside:&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        |Studio with open plan workspace including kitchen area, wc, whb.&nbsp;
        |Private back garden with barbeque area. Side entrance with ample parking.
        |The local area has much to offer with Carlow town center approximately 1km from the property with excellent shopping in the Fairgreen shopping center and a host of local shops and restaurants.
        |Granite Cottage is a short distance from Carlow Golf Club and Oakpark woodland.
        |The arboretum garden centre and many other local amenities just a short drive from the property.
        |For commuting the M9 motorway junction at Tinryland is approximately 4 kilometres from the property making Dublin city and Waterford very accessible by road.
        |There are a number of excellent National and secondary schools within easy reach of the property and the new South East Technical University (SETU)
        |Features;
        |Attractive granite façade with natural slate roof
        |Oil fired central heating
        |Solid fuel stove
        |Private setting
        |Good condition throughout
        """.stripMargin.trim),
        propertyType = Some(PropertyType.House),
        propertyAddress = "Granite Cottge, Link Road, Chapelstown, Carlow",
        propertyEircode = Some(Eircode("R93EY05")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/911302/35596225.jpg",
          "https://media.propertypal.com/hd/p/911302/35627111.jpg",
          "https://media.propertypal.com/hd/p/911302/35627112.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("939428") {
    ScraperHelper.assertItemPageScraperResults(
      "services/propertypalcom/items/939428.html",
      "https://www.propertypal.com/investment-property-14-clayton-court-staplestown-road-carlow-town/939428",
      PropertyPalComItemPageScraper,
      Advert(
        advertUrl = "https://www.propertypal.com/investment-property-14-clayton-court-staplestown-road-carlow-town/939428",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 145000,
        propertyIdentifier = Hash.encode("Investment Property, 14 Clayton Court, Staplestown Road, Carlow Town"),
        propertyDescription = Some("""
        |For sale by Carlow Property via the iamsold Bidding Platform
        |
        | Please note this property will be offered by online auction (unless sold prior). For auction date and time please visit iamsold.ie. Vendors may decide to accept pre-auction bids so please register your interest with us to avoid disappointment.
        |
        | We are delighted to present this beautiful three-bedroom duplex apartment in Clayton Court, an esteemed residential estate in the desirable area of Carlow Town.
        |
        | Clayton Court offers a peaceful and secure environment with private access via electric gates, while conveniently providing easy access to all the amenities of the town within walking distance.
        |
        | This property is a perfect combination of comfort and convenience, making it an ideal choice for families seeking a beautiful home in Carlow Town.
        |
        | Additionally, the purchaser will have the option to acquire the property as seen, inclusive of all furnishings and white goods, with the details of this arrangement open to negotiation at a later stage.
        |
        | The property's ground floor comprises of a beautiful, tiled entrance hallway that gives way to the welcoming, spacious living room brightly lit via large windows.
        |
        | Rounding off the ground floor is the accommodating, fully fitted kitchen it also benefits from a substantial balcony area. The first floor of the property features two spacious bedrooms, both equipped with built-in wardrobes, and one of which in ensuite.
        |
        | Additionally, there is a smaller bedroom ideally sized for a home office. Completing the first floor is a tiled family bathroom, offering the convenience of both a Triton shower and a bath.
        |
        | The property is being sold subject to an ongoing tenancy.
        |
        | Features
        | GFCH
        | Large Private Balcony
        | Sought After Estate
        | Private Gated Complex
        | Walking Distance To All Amenities
        | BER C2 / BER No. 106839228
        |
        | TO VIEW OR MAKE BID Contact Carlow Propertry or iamsold, www.iamsold.ieStarting Bid and Reserve Price
        |*Please note all properties are subject to a starting bids price and an undisclosed reserve. Both the starting bid and reserve price may be subject to change. Terms and conditions apply to the auction, which is powered by IAM Sold Property Auctions.Auctioneer's Comments
        |This property is offered for sale by unconditional auction. The successful bidder is required to pay a 10% deposit and contracts are signed immediately on acceptance of a bid. Please note this property is subject to an undisclosed reserve price. Terms and conditions apply to this sale.Building Energy Rating (BERs)
        |Building Energy Ratings (BERs) give information on how to make your property more energy efficient and reduce your energy costs. All properties bought, sold or rented require an BER. BERs carry ratings that compare the current energy efficiency and estimated costs of energy use with potential figures that your property could achieve. Potential figures are calculated by estimating what the energy efficiency and energy costs could be if energy saving measures were put in place. The rating measures the energy efficiency of your home using a grade from ‘A’ to ‘G’. An ‘A’ rating is the most efficient, while ‘G’ is the least efficient. The average efficiency grade to date is ‘D’. All properties are measured using the same calculations, so you can compare the energy efficiency of different properties.Property Particulars
        |Please contact us for more information about this property.BER Details
        |BER Rating: C2
        |BER No.: 106839228
        |Energy Performance Indicator: Not provided""".stripMargin.trim),
        propertyType = Some(PropertyType.Apartment),
        propertyAddress = "Investment Property, 14 Clayton Court, Staplestown Road, Carlow Town",
        propertyEircode = None,
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/938228/36325007.jpg",
          "https://media.propertypal.com/hd/p/938228/36325003.jpg",
          "https://media.propertypal.com/hd/p/938228/36325008.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 0,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
