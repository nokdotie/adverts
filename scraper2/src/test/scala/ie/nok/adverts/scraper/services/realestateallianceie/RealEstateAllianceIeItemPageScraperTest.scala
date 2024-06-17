package ie.nok.adverts.scraper.services.realestateallianceie

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

class RealEstateAllianceIeItemPageScraperTest extends munit.FunSuite {

  test("947887") {
    ScraperHelper.assertItemPageScraperResults(
      "services/realestateallianceie/items/947887.html",
      "https://www.realestatealliance.ie/lot-1-saint-olivers-road-longwood-meath/947887",
      RealEstateAllianceIeItemPageScraper,
      Advert(
        advertUrl = "https://www.realestatealliance.ie/lot-1-saint-olivers-road-longwood-meath/947887",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 875000,
        propertyIdentifier = Hash.encode("(Lot 1), Saint Oliver's Road, Longwood, County Meath"),
        propertyDescription = Some("""
        |For Sale by Public &amp; Online auction on Wednesday the 26th of June 2024 at 3pm.
        |
        |REA T E Potterton are proud to present this derelict residence on C. 1 acre to the market. This charming old world style holding boasts enormous potential for those who seek countryside living whilst staying within the perhipherals of a village setting. The property which is currently in need of full rennovation boasts all original stone work and archways giving propsective purchasers a real insight as to the potential this property holds.
        |Outisde the property there are a range of cut stone ancillary out buildings along with two small paddocks bordered by mature hedgrows and trees. The property may also be eligable for a range of grants including the vacant homes grant of up to €70,000.
        |
        |Development Potential - The property is laid out in an attractive regular shape and is currently zoned A1 Exisitng Residential under the Meath county development plan 2021-2027, lending it to be ideally suited to the erection of a number of residence subject to the neccessary planning consent from MeathCo Co.
        |
        |The residence at Saint Oliver's road will appeal to all manner of prospective purchasers and an early inspection is an absolute neccessity.
        |
        |
        |Contact Elliott Potterton today to arrange a viewing on 0838568526.
        |Further maps and deatils can be obtained from the sole selling agents REA T E Potterton.
        |
        |No Appliances or Apparatus were tested by the selling agents and prospective purchsers should satisfy themselves as the the working order of any services mentioned on the property. All maps are strictly for identification purposes only.
        |""".stripMargin.trim),
        propertyType = None,
        propertyAddress = "(Lot 1), Saint Oliver's Road, Longwood, County Meath",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 53.4564910626269,
          longitude = -6.925522685051
        ),
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/946687/36572710.jpg",
          "https://media.propertypal.com/hd/p/946687/36572711.jpg",
          "https://media.propertypal.com/hd/p/946687/36572712.jpg",
          "https://media.propertypal.com/hd/p/946687/36572718.jpg",
          "https://media.propertypal.com/hd/p/946687/36572715.jpg",
          "https://media.propertypal.com/hd/p/946687/36572713.jpg",
          "https://media.propertypal.com/hd/p/946687/36572714.jpg",
          "https://media.propertypal.com/hd/p/946687/36572716.jpg",
          "https://media.propertypal.com/hd/p/946687/36572717.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = Some(Rating.G),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.realestatealliance.ie/lot-1-saint-olivers-road-longwood-meath/947887")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("951094") {
    ScraperHelper.assertItemPageScraperResults(
      "services/realestateallianceie/items/951094.html",
      "https://www.realestatealliance.ie/raheenagh-ballagh-newcastle-west-limerick/951094",
      RealEstateAllianceIeItemPageScraper,
      Advert(
        advertUrl = "https://www.realestatealliance.ie/raheenagh-ballagh-newcastle-west-limerick/951094",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 295000,
        propertyIdentifier = Hash.encode("Raheenagh, Ballagh, Newcastle West, County Limerick"),
        propertyDescription = Some("""
        |REA Dooley Group bring to the market this 4 bedroom detached bungalow in an excellent location.
        |
        |Located just on the outskirts of Raheenagh Village this 4 bedroom bungalow comes to the market in turn key condition.
        |
        |Situated on Circa 0.5 Acres of landscaped lawns the bungalow has the benefit of mains water and its own septic tank and detached Garage.
        |
        |Accommodation includes: Entrance Hallway, Sitting Room, Kitchen, Dining Room, Utility with W.C, 4 Bedrooms with Master En Suite.
        |
        |Viewing of the proeprty comes highly recommended through Sole Agents REA Dooley Group on 069-61888.
        |
        |Entrance Hallway - 34'9" (10.59m) x 5'0" (1.52m)
        |Carpet Flooring
        |
        |Sitting Room - 15'0" (4.57m) x 14'4" (4.37m)
        |Carpet flooring with large open fireplace with marble surround.
        |
        |Kitchen - 14'11" (4.55m) x 17'2" (5.23m)
        |Lino flooring with fitted units and solid fuel stove
        |
        |Living Room - 16'6" (5.03m) x 14'7" (4.45m)
        |Laminate flooring with large window facing south garden.
        |
        |Utility - 6'0" (1.83m) x 14'7" (4.45m)
        |Tiled Floor, plumbed with hot and cold water and wc with whb.
        |
        |Bedroom 1 - 11'3" (3.43m) x 14'11" (4.55m)
        |Carpet flooring with built in wardrobe.
        |
        |Ensuite: 0.88 x 2.99 - Tiled floor to ceiling with wc, whb and electric shower.
        |
        |Bathroom - 9'2" (2.79m) x 7'4" (2.24m)
        |Tiled floor to ceiling with wc, whb, heated towel rail, bath and separate shower.
        |
        |Bedroom 2 - 11'3" (3.43m) x 10'0" (3.05m)
        |Carpet flooring with built in wardrobe and sink.
        |
        |Bedroom 3 - 9'3" (2.82m) x 11'5" (3.48m)
        |Carpet flooring and built in wardrobe.
        |
        |Bedroom 4 - 13'7" (4.14m) x 9'11" (3.02m)
        |Carpet flooring and built in wardrobe.
        |
        |Hot Press - 5'9" (1.75m) x 5'11" (1.8m)
        |Carpet flooring and shelving
        |
        |Garage - 12'7" (3.84m) x 12'7" (3.84m)
        |Double Doors
        |
        |2nd Garage - 6'3" (1.91m) x 6'3" (1.91m)
        |
        |
        |what3words /// wiser.crossroad.bundles
        |
        |Notice
        |Please note we have not tested any apparatus, fixtures, fittings, or services. Interested parties must undertake their own investigation into the working order of these items. All measurements are approximate and photographs provided for guidance only.
        |""".stripMargin.trim),
        propertyType = None,
        propertyAddress = "Raheenagh, Ballagh, Newcastle West, County Limerick",
        propertyEircode = Some(Eircode("V42KP68")),
        propertyCoordinates = Coordinates(
          latitude = 52.3727977,
          longitude = -9.037443099999999
        ),
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/949894/36648708.jpg",
          "https://media.propertypal.com/hd/p/949894/36648781.jpg",
          "https://media.propertypal.com/hd/p/949894/36648809.jpg",
          "https://media.propertypal.com/hd/p/949894/36648783.jpg",
          "https://media.propertypal.com/hd/p/949894/36648849.jpg",
          "https://media.propertypal.com/hd/p/949894/36648828.jpg",
          "https://media.propertypal.com/hd/p/949894/36648831.jpg",
          "https://media.propertypal.com/hd/p/949894/36648719.jpg",
          "https://media.propertypal.com/hd/p/949894/36648722.jpg",
          "https://media.propertypal.com/hd/p/949894/36648946.jpg",
          "https://media.propertypal.com/hd/p/949894/36648963.jpg",
          "https://media.propertypal.com/hd/p/949894/36648846.jpg",
          "https://media.propertypal.com/hd/p/949894/36648884.jpg",
          "https://media.propertypal.com/hd/p/949894/36649014.jpg",
          "https://media.propertypal.com/hd/p/949894/36649018.jpg",
          "https://media.propertypal.com/hd/p/949894/36648940.jpg",
          "https://media.propertypal.com/hd/p/949894/36649076.jpg",
          "https://media.propertypal.com/hd/p/949894/36649089.jpg",
          "https://media.propertypal.com/hd/p/949894/36648941.jpg",
          "https://media.propertypal.com/hd/p/949894/36649083.jpg",
          "https://media.propertypal.com/hd/p/949894/36648724.jpg",
          "https://media.propertypal.com/hd/p/949894/36649122.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = Some(103680922),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.realestatealliance.ie/raheenagh-ballagh-newcastle-west-limerick/951094")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("950212") {
    ScraperHelper.assertItemPageScraperResults(
      "services/realestateallianceie/items/950212.html",
      "https://www.realestatealliance.ie/5-athlone-road-moate-westmeath/950212",
      RealEstateAllianceIeItemPageScraper,
      Advert(
        advertUrl = "https://www.realestatealliance.ie/5-athlone-road-moate-westmeath/950212",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 190000,
        propertyIdentifier = Hash.encode("5 Athlone Road, Moate, County Westmeath"),
        propertyDescription = Some("""
        |This semi-detached house is situated close to the heart of Moate town centre, offering convenient access to local amenities. Upon entering, you are welcomed into an entrance hall that leads to a cozy sitting room. The kitchen/dining room is spacious and fitted with a solid fuel stove, creating a warm and inviting atmosphere. Upstairs, the property features two double bedrooms, providing ample sleeping space. The shower room is tastefully tiled, adding a touch of modern elegance.
        |
        |Outside, the house boasts a peaceful garden retreat. To organise a viewing please call REA Hynes on 0906473838.
        |
        |OutsideOutside, the house boasts an extensive garden area at the rear, making it ideal for an extension or development, subject to planning permission. This property is perfect for those looking for a comfortable home with potential for future expansion.
        |""".stripMargin.trim),
        propertyType = None,
        propertyAddress = "5 Athlone Road, Moate, County Westmeath",
        propertyEircode = Some(Eircode("N37R270")),
        propertyCoordinates = Coordinates(
          latitude = 53.396197,
          longitude = -7.730688
        ),
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/949012/36620728.jpg",
          "https://media.propertypal.com/hd/p/949012/36620732.jpg",
          "https://media.propertypal.com/hd/p/949012/36620734.jpg",
          "https://media.propertypal.com/hd/p/949012/36620733.jpg",
          "https://media.propertypal.com/hd/p/949012/36620731.jpg",
          "https://media.propertypal.com/hd/p/949012/36620748.jpg",
          "https://media.propertypal.com/hd/p/949012/36620747.jpg",
          "https://media.propertypal.com/hd/p/949012/36620746.jpg"
        ),
        propertySize = Area.zero,
        propertySizeInSqtMtr = 0,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 1,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.realestatealliance.ie/5-athlone-road-moate-westmeath/950212")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("950173") {
    ScraperHelper.assertItemPageScraperResults(
      "services/realestateallianceie/items/950173.html",
      "https://www.realestatealliance.ie/4-castlegate-square-adamstown-lucan-dublin/950173",
      RealEstateAllianceIeItemPageScraper,
      Advert(
        advertUrl = "https://www.realestatealliance.ie/4-castlegate-square-adamstown-lucan-dublin/950173",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 305000,
        propertyIdentifier = Hash.encode("4 Castlegate Square, Adamstown, Lucan, County Dublin"),
        propertyDescription = Some("""
        |REA McDonald, Lucan's longest-established estate agents, are pleased to present No. 4 Castlegate Square to the market. This bright and airy top-floor apartment has been meticulously maintained by its current owners.
        |Accommodation extends to approximately 893 square feet and upon entering, you are welcomed by an inviting entrance hall that leads to a living area filled with natural light, thanks to its dual aspect. The living area is further enhanced by access to two balconies: one with an easterly aspect for sunny mornings, and the other, a sheltered west-facing balcony ideal for long summer evenings. The kitchen is neatly tucked away off the living area. Two well-proportioned bedrooms provide comfortable living, with the master bedroom featuring an en-suite bathroom. A well-appointed main bathroom completes this contemporary apartment.
        |With an impressive B3 BER rating, this energy-efficient apartment offers the potential for significant cost savings and will appeal to buyers looking to avail of Green Mortgage rates.
        |4 Castlegate Square enjoys a well-connected location, offering residents a convenient lifestyle. Major road networks, including the N4, N7, and M50, are easily accessible, providing excellent connections throughout the region.
        |Additionally, the Adamstown Railway Station and QBC bus service are conveniently located nearby, offering seamless travel to Dublin City Centre and surrounding areas. Residents can enjoy the close proximity of The Crossings, featuring stores like Aldi and Tesco, for their everyday shopping needs. Lucan Village, Supervalu, Liffey Valley Shopping Centre, and various other shops are also within easy reach.
        |There is an abundance of schools in the vicinity, including Adamstown Community College, Lucan Community College, St. John The Evangelist National School, and Adamstown Castle Educate Together. For leisure and outdoor activities, Airlie Park and Tandy's Park &amp; Playground are just a short distance away, providing ample green space to relax and enjoy the fresh air.
        |
        |Accommodation
        |Entrance Hall: 5.87m x 1.09m with wood floor, utility cupboard and hot press cupboard.
        |Living Area: 5.77m x 5.20m a light filled dual aspect room with wood floor and access to two balconies.
        |Kitchen: 3.17m x 2.6 with tiled floor, fitted kitchen units and gas hob.
        |Balcony 1: 5.54m x 1.07m
        |Balcony 2: 5.46m x 2.16m a sheltered balcony with external electrical socket
        |Bedroom 1: 3.93m x 2.89m with fitted wardrobe.
        |En-suite: 2.82m x 1.63m with tiled floor, shower enclosure, window, WC and WHB.
        |Bedroom 2: 4.17m x 3.04m with fitted wardrobe.
        |Bathroom: 2.26m x 2.21m with tiled floor, WC, WHB and bath tub.
        |
        |Features:
        |Two balconies.
        |Turn-key condition.
        |Top floor apartment.
        |Dual aspect living area.
        |Owner occupied.
        |Gas fired central heating.
        |Double glazed NorDan windows and patio doors.
        |Shared block access with one other apartment, with an intercom system and a wide hall separating the two apartments.
        |
        |Please note we have not tested any appliances, apparatus, fixtures, fittings, or services. Interested parties must undertake their own investigation into the working order of these items. Any measurements provided are approximate. Photographs and floor plans provided for guidance only.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "4 Castlegate Square, Adamstown, Lucan, County Dublin",
        propertyEircode = Some(Eircode("K78CN69")),
        propertyCoordinates = Coordinates(
          latitude = 53.3371256,
          longitude = -6.4540686
        ),
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/948973/36619886.jpg",
          "https://media.propertypal.com/hd/p/948973/36619887.jpg",
          "https://media.propertypal.com/hd/p/948973/36619888.jpg",
          "https://media.propertypal.com/hd/p/948973/36619885.jpg",
          "https://media.propertypal.com/hd/p/948973/36619889.jpg",
          "https://media.propertypal.com/hd/p/948973/36619890.jpg",
          "https://media.propertypal.com/hd/p/948973/36619891.jpg",
          "https://media.propertypal.com/hd/p/948973/36619892.jpg",
          "https://media.propertypal.com/hd/p/948973/36619894.jpg",
          "https://media.propertypal.com/hd/p/948973/36619895.jpg",
          "https://media.propertypal.com/hd/p/948973/36619899.jpg",
          "https://media.propertypal.com/hd/p/948973/36619900.jpg",
          "https://media.propertypal.com/hd/p/948973/36619901.jpg",
          "https://media.propertypal.com/hd/p/948973/36619902.jpg",
          "https://media.propertypal.com/hd/p/948973/36619906.jpg",
          "https://media.propertypal.com/hd/p/948973/36619907.jpg",
          "https://media.propertypal.com/hd/p/948973/36619908.jpg",
          "https://media.propertypal.com/hd/p/948973/36619909.jpg",
          "https://media.propertypal.com/hd/p/948973/36620008.jpg",
          "https://media.propertypal.com/hd/p/948973/36620022.jpg",
          "https://media.propertypal.com/hd/p/948973/36620023.jpg",
          "https://media.propertypal.com/hd/p/948973/36620024.jpg",
          "https://media.propertypal.com/hd/p/948973/36620025.jpg",
          "https://media.propertypal.com/hd/p/948973/36620026.jpg",
          "https://media.propertypal.com/hd/p/948973/36620027.jpg",
          "https://media.propertypal.com/hd/p/948973/36620028.jpg",
          "https://media.propertypal.com/hd/p/948973/36620029.jpg",
          "https://media.propertypal.com/hd/p/948973/36620030.jpg",
          "https://media.propertypal.com/hd/p/948973/36620031.jpg",
          "https://media.propertypal.com/hd/p/948973/36620032.jpg"
        ),
        propertySize = Area(83, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 83,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.B3),
        propertyBuildingEnergyRatingCertificateNumber = Some(117469064),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.realestatealliance.ie/4-castlegate-square-adamstown-lucan-dublin/950173")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("946104") {
    ScraperHelper.assertItemPageScraperResults(
      "services/realestateallianceie/items/946104.html",
      "https://www.realestatealliance.ie/27-the-grove-saint-marnocks-bay-portmarnock-dublin/946104",
      RealEstateAllianceIeItemPageScraper,
      Advert(
        advertUrl = "https://www.realestatealliance.ie/27-the-grove-saint-marnocks-bay-portmarnock-dublin/946104",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 450000,
        propertyIdentifier = Hash.encode("27 The Grove, Saint Marnock's Bay, Portmarnock, County Dublin"),
        propertyDescription = Some("""
        |REA McDonald are delighted to present 27 The Grove, Saint Marnock’s Bay—a bright and spacious ground-floor, two-bedroom apartment with its own entrance, situated in a prime location along Station Road in Portmarnock.
        |Upon entry, you will discover an impressive, light-filled home featuring modern flooring, high ceilings, and generously sized rooms. The property comprises a spacious entrance hall with storage, an open-plan living, kitchen, and dining area, and a separate laundry room with additional storage. A hallway with more storage leads to two double bedrooms (master en suite) and a main bathroom. Both bedrooms have access to an enclosed, sunny south-facing rear patio, with the added bonus of a shared outdoor storage facility. The patio leads to a gated communal garden for the sole use of the apartments.
        |This apartment is conveniently located within walking distance of Portmarnock Village, the Portmarnock/Baldoyle cycleway, and the Velvet Strand, with Portmarnock Dart Station just minutes away for quick access to Dublin City Centre and Malahide Village. This is an opportunity not to be missed!
        |
        |Accommodation:
        |Entrance Hall: 3.31m x 1.81m with composite front door, laminate wood floor and storage closet with double doors.
        |Living Area/ Kitchen: 8.23m x 3.88m with laminate wood floor, spot lighting and fitted kitchen units, tiled splash back and storage closet.
        |Utility Room: 1.57m x 1.35m with tiled floor, worktop and timber panelling.
        |Bathroom: 2.53m x 1.79m with tiled floor, WC, large illuminated mirror, WHB, bath and heated towel rail.
        |Hallway: 2.83m x 1.05m with laminate wood floor and storage.
        |Bedroom 1: 4.91m x 2.72m with laminate wood floor, fitted wardrobe, access to patio.
        |En-suite: 2.18m x 1.58m with tiled floor, tiled shower area, shower enclosure and illuminated wall mirror.
        |Bedroom 2: 4.05m x 2.97m with laminate wood floor, access to patio and fitted wardrobe.
        |
        |Features:
        |Air-to-water heat pump
        |Management Fee: €800 p.a.
        |High-performance double-glazed windows
        |Designated parking spaceSouth-facing rear patio
        |Excellent storage
        |A3 Building Energy Rating
        |Built c.2020
        |
        |Please note we have not tested any appliances, apparatus, fixtures, fittings, or services. Interested parties must undertake their own investigation into the working order of these items. Any measurements provided are approximate. Photographs and floor plans provided for guidance only.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "27 The Grove, Saint Marnock's Bay, Portmarnock, County Dublin",
        propertyEircode = Some(Eircode("D13YCH0")),
        propertyCoordinates = Coordinates(
          latitude = 53.4179011,
          longitude = -6.1474541
        ),
        propertyImageUrls = List(
          "https://media.propertypal.com/hd/p/944904/36522806.jpg",
          "https://media.propertypal.com/hd/p/944904/36522808.jpg",
          "https://media.propertypal.com/hd/p/944904/36522810.jpg",
          "https://media.propertypal.com/hd/p/944904/36522812.jpg",
          "https://media.propertypal.com/hd/p/944904/36522814.jpg",
          "https://media.propertypal.com/hd/p/944904/36522816.jpg",
          "https://media.propertypal.com/hd/p/944904/36522818.jpg",
          "https://media.propertypal.com/hd/p/944904/36522820.jpg",
          "https://media.propertypal.com/hd/p/944904/36522822.jpg",
          "https://media.propertypal.com/hd/p/944904/36522825.jpg",
          "https://media.propertypal.com/hd/p/944904/36522829.jpg",
          "https://media.propertypal.com/hd/p/944904/36522832.jpg",
          "https://media.propertypal.com/hd/p/944904/36522835.jpg",
          "https://media.propertypal.com/hd/p/944904/36522838.jpg",
          "https://media.propertypal.com/hd/p/944904/36522841.jpg",
          "https://media.propertypal.com/hd/p/944904/36522864.jpg",
          "https://media.propertypal.com/hd/p/944904/36522865.jpg",
          "https://media.propertypal.com/hd/p/944904/36522866.jpg",
          "https://media.propertypal.com/hd/p/944904/36522867.jpg",
          "https://media.propertypal.com/hd/p/944904/36522869.jpg",
          "https://media.propertypal.com/hd/p/944904/36522870.jpg",
          "https://media.propertypal.com/hd/p/944904/36522871.jpg",
          "https://media.propertypal.com/hd/p/944904/36522929.jpg",
          "https://media.propertypal.com/hd/p/944904/36522930.jpg"
        ),
        propertySize = Area(83, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 83,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.A3),
        propertyBuildingEnergyRatingCertificateNumber = Some(112733951),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.realestatealliance.ie/27-the-grove-saint-marnocks-bay-portmarnock-dublin/946104")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
