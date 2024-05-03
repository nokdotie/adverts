package ie.nok.adverts.scraper.services.abbeypropertysalescom

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

class AbbeyPropertySalesComItemPageScraperTest extends munit.FunSuite {

  test("N41H427") {
    ScraperHelper.assertItemPageScraperResults(
      "services/abbeypropertysalescom/items/N41H427.html",
      "https://www.abbeypropertysales.com/properties/38-river-meadow-dromod-co-leitrim-n41h427/",
      AbbeyPropertySalesComItemPageScraper,
      Advert(
        advertUrl = "https://www.abbeypropertysales.com/properties/38-river-meadow-dromod-co-leitrim-n41h427/",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 235000,
        propertyIdentifier = Hash.encode("38 River Meadow Dromod Co. Leitrim"),
        propertyDescription = Some("""
          |38, River Meadow, Dromod, Co. Leitrim
          |
          |
          |Eircode: N41 H427. BER B3.
          |
          |
          |Abbey Property sales are delighted to bring to the market this semi-detached property in the award-winning village of Dromod. The property is located just a short stroll from the mainline Sligo to Dublin Railway station. It is approximately 15 minutes from Carrick on Shannon and 15 minutes from Longford town.
          |
          |
          |The sitting room is open plan and very spacious as it incorporates the hallway and a spiral staircase to the upper level. There are two small windows either side of the internal porch area and two other windows in the sitting area. A stove in the sitting area heats the radiators.
          |
          |
          |A glass panelled pocket sliding door leads to the spacious kitchen/dining room. The modern kitchen includes integrated appliances, a peninsula island and good storage. Patio doors lead to the back garden and an additional two windows make this a light bright room.
          |
          |
          |The first spiral stairs lead to the upper level where there are three large double bedrooms, all of which have built in wardrobes. A large family bathroom with separate shower and bath completes the accommodation on this level. A second spiral stairs lead to the attic room which is floored and has two velux roof lights.
          |
          |
          |The energy rating of this property is a B3. Additional insulation on the ground floor and upper level adds to the comfort and warmth of this property. A new heating boiler was installed approximately three years ago.
          |
          |
          |There is off street parking to the front on the cobble lock driveway. The rear garden can be accessed by the side of the property or from the kitchen patio doors. There is a large gazebo, lawn area and steel garden shed.
          |
          |
          |Accommodation
          |
          |
          |Entrance Porch
          |
          |
          |Sitting Room: 8m x 4.33m Open plan room incorporating the hall area with spiral stairs to upper level, wood effect lino, stove which heats the radiators, two small windows either side of the porch and two windows in the sitting area.
          |
          |
          |Kitchen/Dining Room: 5.91m x 3.74m Fitted kitchen with integrated Fridge Freezer and integrated dishwasher, oven, hob &amp; extractor fan, peninsula island, patio doors to back garden and two windows in dining area.
          |
          |
          |Downstairs w.c.: 2.52m x 1.83m wood effect lino, corner shower, w.h.b., w.c., chrome heated towel radiator, washer/dryer and window.
          |
          |
          |Spiral stairs to upper level
          |
          |
          |Bedroom 1: 4.04m x 3.31m built in wardrobe, wood effect lino, two windows.
          |
          |
          |Bedroom 2: 3.85m x 3.66m with built in wardrobe.
          |
          |
          |Bedroom 3: 3.77m x 3.58m Wood effect lino, built in wardrobe, patio doors to Juliet balcony.
          |
          |
          |Bathroom: 3.84m x 1.64m Bath, separate large shower, w.c., w.h.b., wood effect lino
          |
          |
          |Spiral stairs to attic
          |
          |
          |Attic: 6.21m x 4.21, wood effect lino, two velux roof lights, storage.
          |
          |
          |Features:
          |B3 energy rating
          |Additional insulation on ground floor and first floor
          |Dual heating with oil &amp; stove which heats the radiators &amp; water.
          |Zoned heating
          |Large reception rooms
          |Steel garden shed.
          |Quiet established estate across the road from the Sligo to Dublin main line railway station
          |Proximity to N4, Carrick on Shannon &amp; Longford
          |
          |
          |Viewing of this property is recommended. Please contact Nuala on 087 9372780.
          |
          |
          |“All information provided is to the best of our knowledge, information &amp; belief. The utmost care and attention on our part has been placed on providing factual and correct information. Please note that in certain instances some information may have been provided directly by the vendor to us. Whilst due attention and care is taken in preparing particulars, this firm does not hold itself responsible for mistakes, errors or inaccuracies in our online advertising and give each and every viewer the right to get a professional opinion on any concern they may have.”
        """.stripMargin.trim),
        propertyType = Some(PropertyType.SemiDetached),
        propertyAddress = "38 River Meadow Dromod Co. Leitrim",
        propertyEircode = Some(Eircode("N41H427")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-river-meadow-1-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-river-meadow-2-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-river-meadow-3-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-4-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-5-971x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-6-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-7-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-8-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/38-River-Meadow-9-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_150824-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_150831-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_150836-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_150847-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_150855-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151110-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151115-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151121-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151127-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151157-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151204-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151250-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151315-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151325-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151315-1-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151325-1-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151348-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151354-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151446-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151454-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151500-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151507-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151543-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151549-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151623-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151631-895x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151655-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151754-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151820-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151822-1024x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2024/05/20240410_151833-1024x1024.jpg"
        ),
        propertySize = Area(1291, AreaUnit.SquareFeet),
        propertySizeInSqtMtr = 119.937773,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.abbeypropertysales.com/properties/38-river-meadow-dromod-co-leitrim-n41h427/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("N41Y634") {
    ScraperHelper.assertItemPageScraperResults(
      "services/abbeypropertysalescom/items/N41Y634.html",
      "https://www.abbeypropertysales.com/properties/aughamore-mohill-co-leitrim-n41y634/",
      AbbeyPropertySalesComItemPageScraper,
      Advert(
        advertUrl = "https://www.abbeypropertysales.com/properties/aughamore-mohill-co-leitrim-n41y634/",
        advertSaleStatus = AdvertSaleStatus.Sold,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("Aughamore Mohill, Co. Leitrim"),
        propertyDescription = Some("""
          |Lake View, Aughamore, Mohill, Co. Leitrim
          |
          |
          |Eircode: N41 Y634. BER: B3
          |
          |
          |Abbey Property sales have great pleasure in bringing this exceptional large, detached property to the market together with six acres of land, two garages, one double and one single and an exceptionally large 2400 sq ft multipurpose built steel shed. The property comes to the market with six acres. The house and large workshop to the rear come with two acres and there are two additional paddocks either side of the house both each measuring two acres.
          |
          |
          |Lake View, Aughamore is on an elevated site with far reaching views of Errew lake and Lough Rynn Castle Hotel. It is a five-minute drive to the beautiful Lough Rynn Castle and estate, five-minute drive to local amenities in the town of Mohill, approximately twenty minutes to the N4 and the towns of Longford and Carrick on Shannon.
          |
          |
          |This exceptional property is bordered by stone walls and piers and is accessed via electric gates. The tarmac driveway continues up to and around the property allowing for parking of several cars.
          |
          |
          |The property is presented in pristine condition and offers 3,046 square feet of spacious living accommodation including four living rooms, five bedrooms, a home office and five bathrooms. It is tastefully decorated in neutral colours which gives a great feeling of space, light and comfort. In days of high building costs this property offers a new owner everything they would want without the need for any additional renovation costs or the need of an extension.
          |
          |
          |The upper level of the property is concrete which means no noise disturbance to the lower level. The entrance hall is very spacious with quality quarry tiles and adorned by an impressive double height chandelier hanging from the first-floor ceiling. There are two feature arch windows in the hallway which give character and light to the hall.
          |
          |
          |From the hallway there is a large light filled dual aspect Sitting Room with lovely views of the surrounding gardens and countryside. The feature marble fireplace and inset multifuel stove offers additional source of heat and focal point on cold Winter evenings. Although with a B3 energy rating and the comfort of thermostatically controlled under floor heating extra heat may not be required. The Living/Dining room is directly across the hallway from the sitting room and too is dual aspect with feature marble fireplace and inset multifuel stove. Double doors from the dining room lead to a light filled triple aspect sunroom with a sky light velux in the vaulted ceiling. The family kitchen may be accessed from the hallway, the dining room and the sunroom. Quality fitted cream units with integrated appliances, an island and a freestanding electric range are but a few of the quality features in the kitchen. The kitchen is dual aspect and leads to a large utility room with washing machine and built in units. A door from the utility room leads to the back garden. The fifth bedroom, to the rear of the property is located on the ground floor and is ensuite. This room offers many other uses including a second home office, playroom etc. Finally a large cloaks room with w.c., w.h.b . complete the accommodation on the ground floor.
          |
          |
          |The open thread oak staircase divides at the top leading to either side of the impressive landing. This area is light filled with a window overlooking the front garden and far-reaching countryside views. The master bedroom is an exceptionally large bedroom with walk in wardrobe/dressing room and an ensuite with shower, w.c. and w.h.b. The large guest bedroom across the landing also benefits from having an ensuite with shower, w.h.b. and w.c. A further two double bedrooms are to the rear of the property either side of the large family bathroom with jacuzzi bath, shower, w.c. and w.h.b. The large hot press, with extra size cylinder, offers great storage, as does the attic which is accessed via a staighre.
          |
          |
          |The opportunities outside this property are in abundance. Either side of the property are two block-built garages, one double garage with two electronic roller doors and a staighre stairs to the upper level. The second single garage has double doors to the front and a ladder provides access to the upper level. Both garages have power.
          |
          |
          |To the rear of the property there are raised beds, an abundance of fruit trees, shrubs and trees. The gardens are manicured and have various light fittings including motion light sensors, feature free standing and low-level garden lights. The purpose built 2400 square foot workshop with mechanical car lift is accessed via electric roller door. This workshop is immaculately presented and could serve as a gym, games area and many other uses due to its exceptional size.
          |
          |
          |Finally, to complete the six acres which come with this property there are two, two acre paddocks either side of the property. This property is ideal for any horse enthusiasts or someone who wants a small holding for animals etc.
          |
          |
          |Accommodation
          |
          |
          |Entrance hall: 5.98m x 4.24m Two feature arched windows either side of the front door. Tiled floor, solid oak open tread staircase, double height ceiling over stairs with chandelier over stairs, recessed lighting.
          |
          |
          |Sitting Room: 5.82m x 4.75m Semi solid wooden floor, Dual aspect, feature marble fireplace with inset multi fuel stove, T.V. point
          |
          |
          |Living/Dining Room: 4.97m x 4.68m Semi solid wooden floor, Dual aspect, double oak doors to the sunroom and separate doors to kitchen and hallway, feature marble fireplace with inset multifuel stove, T.V. point
          |
          |
          |Sunroom: 3.50m x 3.44m Semi solid wooden floor, Tripple aspect, vaulted ceiling with velux roof light, double doors to sunroom and double doors to garden.
          |
          |
          |Kitchen: 4.68m x 4.53m Dual aspect, Cream fitted kitchen cabinets, tiled floor, Island, Electric range, integrated appliances including built in microwave.
          |
          |
          |Utility room: 2.63m x 2.55m Tiled floor, Units, washing machine and door to back garden.
          |
          |
          |Downstairs cloaks room: 2.53m x 1.55m Large cloaks room with w.c., w.h.b. tiled floor, window
          |
          |
          |Landing: 6.22m x 4.48m Gallery landing with centre staircase, laminate floor, light filled area with window overlooking countryside and stunning chandelier adorning the double height stair well.
          |
          |
          |Master bedroom: 4.79m x 4.78m Laminate floor, Walk in wardrobe/dressing area, ensuite, windows to front of property, T.V. point
          |
          |
          |Ensuite: 2.77m x 1.82m Tiled floor and partially tiled walls, w.c., shower, w.h.b. &amp; velux window, heated towel rail.
          |
          |
          |Bed 2: 4.78m x 3.74m Large double room with ensuite, laminate floor and windows to the front of the property, T.V. point
          |
          |
          |Ensuite: 2.05m x 1.86m Tiled floor and partially tiled walls, w.c., w.h.b. and shower, heated towel rail, velux window.
          |
          |
          |Bed 3: 4.75m x 3.62m Large double bedroom to the rear of the property, laminate floor, T.V. point
          |
          |
          |Bed 4: 4.74m x 3.69m Large double bedroom to the rear of the property, laminate floor, T.V. point
          |
          |
          |Bed 5 (downstairs): 4.86m x 4.73m Semi solid wooden floor, Window to rear, T.V. point
          |
          |
          |Ensuite: 2.62m x 1.17m Tiled floor and partially tiled walls, shower, w.c. &amp; w.h.b.
          |
          |
          |Home office: 2.43m x 2.01m Semi solid wooden floor, Velux window.
          |
          |
          |Family bathroom:3.15m x 2.40m Jacuzzi bath, separate shower, w.c., w.h.b., tiled floor, partially tiled walls
          |
          |
          |Hot press: 2.20m x 1.78m with shelving and large water cylinder
          |
          |
          |Features
          |
          |
          |The features of this property are too numerous to mention all but viewing with reaffirm the quality of many exceptional features on offer in addition to the following:
          |
          |
          |Large, detached property on elevated site with far reaching countryside views
          |Six acres of land including manicured gardens, two x two acre paddocks
          |Two block-built garages, one double and one single
          |2,400 square foot immaculate workshop with mechanical car lift
          |Five double bedrooms
          |Concrete upper level
          |Under floor heating
          |Solid oak, stairs, internal doors, architrave, and skirting
          |Quality fitted kitchen with electric range
          |Three of the five bedrooms are ensuite
          |Quality curtains throughout included
          |Majority of light fittings included
          |Tripple aspect sunroom with vaulted ceiling
          |Under floor thermostatically controlled heating downstairs
          |O.F.C.H. upper level
          |Two multifuel stoves in set in feature marble fireplaces
          |Chrome light switches, sockets and T.V. points throughout the property
          |Smoke detectors
          |Alarm
          |Staighre to Attic
          |
          |
          |To appreciate this substantial property viewing is a must as the description just gives a brief insight in to all that is on offer. Viewing is highly recommended.
          |
          |
          |Please contact Nuala McElhinney on 087 9372780 for further information or to schedule a viewing.
          |
          |
          |All information provided is to the best of our knowledge, information &amp; belief. The utmost care and attention on our part has been placed on providing factual and correct information. Please note that in certain instances some information may have been provided directly by the vendor to ourselves. Whilst due attention and care is taken in preparing particulars, this firm does not hold itself responsible for mistakes, errors or inaccuracies in our online advertising and give each and every viewer the right to get a professional opinion on any concern they may have.
          |""".stripMargin.trim),
        propertyType = Some(PropertyType.Detached),
        propertyAddress = "Aughamore Mohill, Co. Leitrim",
        propertyEircode = Some(Eircode("N41Y634")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154410-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154413-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154217-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154537-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154224-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154316-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154322-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154516-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154431-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154511-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154559-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154609-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154625-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154630-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154641-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154658-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154747-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155023-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154705-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154728-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154735-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154755-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154812-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_154939-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155002-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155018-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155151-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155154-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155157-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155258-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155305-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155314-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155322-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155353-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155402-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155410-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155442-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155332-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155336-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155419-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155428-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155453-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155459-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155504-1-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155513-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155532-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155540-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155525-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155556-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155608-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155613-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155620-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155631-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155246-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_160022-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155911-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155934-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155720-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155724-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155730-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155748-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155753-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155801-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155805-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155809-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155822-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155832-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155842-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155847-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155944-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_155951-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_160003-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_160011-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_160016-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/09/20230817_160031-768x1024.jpg"
        ),
        propertySize = Area(3045, AreaUnit.SquareFeet),
        propertySizeInSqtMtr = 282.889635,
        propertyBedroomsCount = 5,
        propertyBathroomsCount = 5,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.abbeypropertysales.com/properties/aughamore-mohill-co-leitrim-n41y634/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("N41W778") {
    ScraperHelper.assertItemPageScraperResults(
      "services/abbeypropertysalescom/items/N41W778.html",
      "https://www.abbeypropertysales.com/properties/old-station-house-corramahan-ballinamore-co-leitrim-n41w778/",
      AbbeyPropertySalesComItemPageScraper,
      Advert(
        advertUrl = "https://www.abbeypropertysales.com/properties/old-station-house-corramahan-ballinamore-co-leitrim-n41w778/",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("Old Station House, Corramahan Ballinamore, Co. Leitrim."),
        propertyDescription = Some("""
          |Are you looking for peace and tranquility, and maybe a little bit of history?
          |Then we might just have found the getaway for you.
          |
          |
          |Eircode N41W778
          |
          |
          |Just a 5-minute drive from the town of Ballinamore &amp; nesteled in c 2.3 acres of woodland and gardens, you will find this fully restored &amp; extended old railway house.
          |
          |
          |The current owner purchased the property over 20 years ago and set about turning the old derelict station house into a lovely home.
          |
          |
          |Priced to sell, and ready for the new owner to take on this lovely home.
          |
          |
          |Accommodation consists of –
          |
          |
          |Large extension.
          |
          |
          |Entrance Hall, Large bright kitchen/dining room with solid fuel range for heating and warm water, leading into an open plan sitting room with a stove. Large fully equipped bathroom with bath, shower, wc &amp; whb, large bedroom with walk in wardrobes.
          |
          |
          |Old Station.
          |
          |
          |This area has a large dining area, utility room &amp; a room on the first floor needing attention to convert to a 3rd bedroom.
          |
          |
          |Small Extension.
          |
          |
          |This area has a sitting room with solid fuel range, bedroom plus another room.
          |
          |
          |There are a number of outbuildings and a polytunnel in this mystic garden with walks through the different types of trees.
          |
          |
          |Are you looking for that lovely country home?
          |
          |
          |Have a look at this one.
          |
          |
          |Properties like this don’t come to the market very often.
          |
          |
          |Priced to sell.
          |
          |
          |Call Gerry on 086 8066701 to arrange a viewing.
          |
          |
          |BER. G.
          |
          |
          |All information provided is to the best of our knowledge, information &amp; belief. The utmost care and attention on our part has been placed on providing factual and correct information. Please note that in certain instances some information may have been provided directly by the vendor to us. Whilst due attention and care is taken in preparing particulars, this firm does not hold itself responsible for mistakes, errors or inaccuracies in our online advertising and give each viewer the right to get a professional opinion on any concern they may have.
        """.stripMargin.trim),
        propertyType = Some(PropertyType.House),
        propertyAddress = "Old Station House, Corramahan Ballinamore, Co. Leitrim.",
        propertyEircode = Some(Eircode("N41W778")),
        propertyCoordinates = Coordinates.zero,
        propertyImageUrls = List(
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/5-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130834-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_131027-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_131303-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/1-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/3-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/6-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/2-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/4-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130650-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130644-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130621-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130627-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130632-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130720-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130744-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130756-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130801-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130809-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/7-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130308-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130314-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/8-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130324-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130330-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130538-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130356-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130337-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130342-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/10-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/9-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130552-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/11-1024x576.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130558-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130530-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130409-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130414-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130428-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130439-768x1024.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130453-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130504-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130516-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130605-1024x768.jpg",
          "https://www.abbeypropertysales.com/wp-content/uploads/2023/07/20230706_130610-1024x768.jpg"
        ),
        propertySize = Area(3, AreaUnit.Acres),
        propertySizeInSqtMtr = 12140.58,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 1,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.abbeypropertysales.com/properties/old-station-house-corramahan-ballinamore-co-leitrim-n41w778/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
