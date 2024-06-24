package ie.nok.adverts.scraper.services.myhomeie

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

class MyHomeIeItemPageScraperTest extends munit.FunSuite {

  test("4785390") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4785390.html",
      "https://www.myhome.ie/residential/brochure/_/4785390",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4785390",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 1075000,
        propertyIdentifier = Hash.encode("52 Shrewsbury, Ballsbridge, Dublin 4"),
        propertyDescription = Some("""
        |Superb end of terrace three bedroom home with additional attic conversion, well positioned within this gated development off Merrion Road in Ballsbridge. Accommodation over two levels extends to approx 131 sq. m (1,410 sq.ft) and comprises spacious entrance hallway with guest w.c and staircase to first floor. There is a generous reception room to the front of the house with bay window and fireplace. To the rear the property has an attractive modern kitchen with excellent storage and a dining room off same. There are double doors from the dining room out to the rear garden which offers a lovely space from which to enjoy al fresco dining.
        |
        |Upstairs there are three generous bedrooms, the main bedroom with en-suite shower room. There is also a family bathroom at this level. A superb addition to this property is the excellent converted attic room providing good additional space.
        |
        |To the rear of the property there is an attractive garden with patio area accessed from the diningroom and the garden is mainly laid in lawn with good privacy and enjoying lovely sunshine. There is side access and to the front a driveway provides important off street parking.
        |
        |Shrewsbury is a small, gated development with a range of mature quality family homes set within beautifully maintained grounds. This is a location of enviable convenience situated off the Merrion Road, a short distance to nearby Ballsbridge with its range of shopping amenities and also the Merrion Shopping Centre. The DART at Sydney Parade Avenue is easily accessible providing coastal access to South and North Dublin. The property is within walking distance of The Royal Dublin Society, Herbert Park and the Aviva Stadium. Dublin city centre is easily accessible as are all the coastal amenities along the south coast of Dublin.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "52 Shrewsbury, Ballsbridge, Dublin 4",
        propertyEircode = Some(Eircode("D04K8X9")),
        propertyCoordinates = Coordinates(
          latitude = 53.326236,
          longitude = -6.220648
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/44b3689d-d1ee-46e1-8f70-16d4b5d8767c_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/bf02dfd2-e118-4aeb-b086-92da3b0ffb08_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/b831cb58-9d68-4fc0-ae76-32a6d1c3587c_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/157e2f93-b48f-4710-b88d-d34aee1d98e3_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/e9344867-181a-4be2-aee2-54af16bbcbc1_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/bcf6ed97-c9f5-4b2c-8f96-261858c607d1_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/ce3bff5d-b47d-409d-9dee-54766a6cee84_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/3d663c7d-8c5e-42be-99c2-e6d63e68aea7_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/eb1bcdd3-a729-4ff4-bd15-6fc4c2ef5152_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/b19b85c6-b234-413f-b0a9-646ac3172349_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/b0b06071-d911-4254-8e69-8294cc3cc5de_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/f3cc0505-615e-4286-b9c9-8f7131209739_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/fe6993f5-ff0e-413f-ad1c-09fc32a19845_l.jpg"
        ),
        propertySize = Area(131, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 131,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4785390")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4786237") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4786237.html",
      "https://www.myhome.ie/residential/brochure/_/4786237",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4786237",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("11 Carrickmines Little, Foxrock, Dublin 18"),
        propertyDescription = Some(
          """
        |Sherry FitzGerald is delighted to bring this substantial, detached family home to the market. Extended and upgraded over the years the property enjoys exceptionally spacious accommodation meeting the demands of today's busy family lifestyles.
        |
        |The entrance hall with monochrome tiled floor, wall panelling and radiator cabinets exudes a warm welcome and note of calm elegance. The living room enjoys a square bay and period style marble fireplace with cast iron inset to the front. Warm hues generated by the narrow board wooden floorboards create a cosy ambience. Double doors from here lead to a further reception room. Currently this room is suited for reading and contemplation but would suit for a variety of uses. To the right of the hall there is a snug TV room with cast iron fireplace and coal effect gas fire. The kitchen has a modern country feel to it with hand- painted teal cupboards, Belfast sink and off-set by a black granite worktop. In addition there is a cast iron fireplace surround for aesthetic appeal. Off the kitchen is the utility room off which is a room which would suit as further study / playroom / bedroom.
        |
        |A well-appointed reception room hugs the back of the house with access off the living area and the kitchen cum breakfast room. This is a wonderful family hub where light abounds and is reflected through the Velux windows and the glazed walls. Currently used as both a living and dining area this space adds invaluable additional reception room leaving no compromise in the accommodation. With two sets of French doors to the paved patio area this extension brings the outdoors in and vice versa ensuring maximum access and use of the south facing rear garden.
        |
        |No. 11 Carrickmines Little is a superb, spacious family home providing excellent accommodation with the combination of a private, south facing rear garden. Viewing highly recommended.
        |
        |GARDEN
        |Large tarmacadamed front driving apron with off-street parking for several cars. Curved flower beds with mature hedges to include a neatly clipped Juniper bush for privacy. Side entrance. A generous sized corner site provides for a spacious sized rear garden. A garden shed (4.13m x 3.01m), wired for electricity, provides invaluable storage space for garden cushions, bikes and garden paraphernalia whilst discreetly positioned behind a low lying, clematis clad, decorative wall to the side. Large patio area overlooking spacious, private lawned back garden. Neatly pruned Cypress bush to the back provides a natural opaque wall and screening. Wonderful southerly orientation. Feature clematis pergola and separate garden gazebo.
        |
        |LOCATION
        |The property is located within easy walking distance from the Carrickmines Luas stop and within a short drive of the M50 (Exit 15) as well as the N11. The 84, 84A and 145 buses run along the N11 easily accessed down Cornelscourt Hill Road. The Park Retail centre is also close by with its vast array of shops and DIY stores as is Dunnes Stores in Cornelscourt. Spoilt for choice being equidistant from both Cabinteely and Foxrock villages, renowned for their variety of eateries and specialist food shops and picturesque with their hanging baskets and olde world shop fronts. The property also benefits from being within easy reach of St. Brigid`s Girls National School and Boys National School and Loreto College Foxrock. Cabinteely Park with its 110 acres of parkland with wooded walks, duck pond and large children`s playground is located ten minute- walk away. The Nord Anglian International School is located in nearby Leopardstown. Sporting facilities include Carrickmines Croquet and Lawn Tennis Club, Foxrock Golf Club, Westwood Club and Carrickmines Equestrian Centre to name a few.""".stripMargin.trim
        ),
        propertyType = None,
        propertyAddress = "11 Carrickmines Little, Foxrock, Dublin 18",
        propertyEircode = Some(Eircode("D18H1W2")),
        propertyCoordinates = Coordinates(
          latitude = 53.256952,
          longitude = -6.173645
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/026269ce-c616-4bab-90cb-85e9ae84d2b0_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/945fc825-1496-4dab-a1ec-4d37bbab0cfb_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/c8daf47d-6101-43a8-b46c-20d33304cfd4_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/34fc8181-c9b1-4186-95ce-d99b540f7f50_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/9df11cca-cee3-435e-9cb3-2f715eb5f0c3_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/a2d06196-4c60-4049-8789-5b236abab023_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/913be67e-2768-4247-a0bf-70c9b944bbf4_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/913e2442-df2c-4a5e-80ab-f02de6d51cc9_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/b04734fc-f2c7-4cc3-94aa-a66e2e71cc31_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/d27f3bdd-65d5-4373-be7f-12682cf3415a_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/3b5dee1f-1b67-4b0a-bb02-4ccd3fac903b_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/817ee2a8-3647-45ef-b9c8-390bf5f83bbf_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/1d901f88-8259-4845-971b-e1d2d2d72da6_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/30ad2a70-cc04-47bf-8242-e69f0838a969_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/3b379247-aa5e-4c60-80a3-fb76f75f6396_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/2930d5ff-781e-4e45-bef0-ab2daad1fc64_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/020e3f28-8d98-4d63-9e66-fc97c3378be8_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/be78310d-fbd5-45ce-83ad-5fe478b1f6f5_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/cff3fa3f-2b5a-4427-baf5-ec1cef1ad919_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/491de373-9692-45f9-b679-3d0b3a36e96f_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/69e03e04-e1b3-4d80-981a-3d77727bf44e_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/c10a525e-27c8-4572-adfa-53f4d6c40126_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/a1044227-c41f-4df1-bcc0-8337f2e16117_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/3/2/4786237/629345b2-668b-490f-b459-839857a394c2_l.jpg"
        ),
        propertySize = Area(234, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 234,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.B3),
        propertyBuildingEnergyRatingCertificateNumber = Some(117328872),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(126.7),
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4786237")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4752283") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4752283.html",
      "https://www.myhome.ie/residential/brochure/_/4752283",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4752283",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 390000,
        propertyIdentifier = Hash.encode("78 Thornhill Gardens, Celbridge, County Kildare"),
        propertyDescription = Some("""
        |FOR SALE BY PRIVATE TREATY
        |78, THORNHILL GARDENS, CELBRIDGE, CO KILDARE, W23 R684.
        |
        |BIDDING ONLINE:: https://homebidding.com/property/78-thornhill-gardens240
        |
        |
        |Award winning Auctioneering Team for over 20 years, Team Lorraine Mulligan of RE/MAX Results welcomes you to this charming three-bedroom, semi-detached home in Celbridge that is a testament to the care and attention bestowed upon it by its current owners. Nestled in a tranquil residential neighbourhood, this property exudes warmth and character, making it an ideal family home.
        |
        |Upon entering the property, you are greeted by a welcoming hallway that leads to the main living areas. The ground floor boasts a spacious sitting room, featuring large window that allow ample natural light to flood the space. Off the hallway, you will find a downstairs guest W.C., as well as a well-appointed kitchen and dining area. The kitchen is equipped with a cream fitted kitchen with storage space. There is a rear door that leads to a sun trapped south facing sunny back garden with garden room and utility space with Guest W.C.
        |
        |Upstairs, the property offers three bedroom and a family bathroom with a full bath and separate shower unit. Each bedroom features large windows, allowing natural light to illuminate the space and creating a pleasant ambiance throughout.
        |
        |Externally, the property enjoys a south rear garden with low maintenance, providing an excellent space for outdoor activities, al fresco dining, or simply enjoying the sunshine and leads to the garden room. The garden is designed with low-maintenance in mind and offers a peaceful retreat from the hustle and bustle of everyday life. Additionally, the property benefits from off-street parking and a front garden area.
        |
        |Located in Celbridge, this property enjoys a prime position in a sought-after residential area. The town itself offers a host of amenities, including shops, supermarkets, restaurants, schools, and recreational facilities. The property is also well-connected to major road networks, providing easy access to Dublin city centre and other neighbouring towns.
        |
        |In summary, this charming three-bedroom semi-detached property with garden room in Celbridge presents an excellent opportunity to acquire a beautiful family home in a desirable location. With its well-designed living spaces, modern features, and convenient amenities, this property offers comfortable and contemporary living for its fortunate new owners. Viewing is highly recommended to truly appreciate all that this home has to offer.
        |
        |Thornhill Gardens is a peaceful and well- established residential area, with a large green area at the front of the estate. It is close to all local amenities in including `Scoil Mochua` primary school, local convenience shops, hairdressers and has easy access to the X27 BD l59 which stop at the front of the estate along with access to the M4 motorway.
        |
        |If you have a similar property and you are looking to sell, please call our office on: (01) 6272770 for a valuation.
        |
        |Please email office@teamlorraine.ie to book a viewing.""".stripMargin.trim),
        propertyType = None,
        propertyAddress = "78 Thornhill Gardens, Celbridge, County Kildare",
        propertyEircode = Some(Eircode("W23R684")),
        propertyCoordinates = Coordinates(
          latitude = 53.3486706,
          longitude = -6.5518274
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/3d1ff332-8a6b-4db9-9051-229aa34035eb_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/bde714db-d0a9-4b2b-bb86-c7d6c4a77ecc_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/e2809acd-dc9a-4181-a1f7-6185d1602489_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/e292ebb1-7fa2-4979-8446-550aedd82c35_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/ea9d7f87-9310-45d0-8c4e-db8d20f32f03_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/d89a7f74-650e-4c38-af79-4848e711e061_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/985cd78d-a993-4017-8e76-32e5757a8b01_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/c015e23a-7786-44e9-b249-315c1cc702a6_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/19d8252e-140a-4a07-b062-420248bf85ca_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/173d3f8e-ddbb-4e33-ac38-4cdd4cc58c42_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/b2ff0536-4f75-45f2-9014-525d95acc8fc_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/de0d0ac7-fbc1-4db2-95fd-371b40f37762_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/498d6b27-4d6e-48d0-84b1-e1888fbf5642_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/f99d82d8-b130-4e44-9063-1c2a516eddd7_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/f4070788-b42f-4b82-87bc-97af2fb71c0a_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/46e02226-4454-4684-89f5-0d9536a606f4_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/faaecbf9-2b2c-4501-881e-fab1fcadd430_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/87911a48-73cc-4dc0-8351-91ec541a3d6d_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/4cb790da-49fc-43e9-bd33-ce4fbcd17c91_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/f4ed41c3-051e-4f12-876f-1a16033049e6_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/691c7d82-3b34-46a1-9bb2-d91f51fa30ce_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/8b603bf0-d380-4688-93b1-30e0c5f9aa5e_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/f32d18c3-dfe6-4c7b-8e64-8ac04b87da45_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/8d493aec-48bf-4642-b088-8b84f04d77d1_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/776a55e7-1479-40bd-a7ba-2c593bc51b6b_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/1a2860ac-5336-42c7-9b4b-a86ae30e7a70_l.jpg",
          "https://photos-a.propertyimages.ie/media/3/8/2/4752283/ffde9d09-9c19-4fe3-bc69-202f7a058ab1_l.jpg"
        ),
        propertySize = Area(84.91, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 84.91,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C3),
        propertyBuildingEnergyRatingCertificateNumber = Some(116936360),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(203.74),
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4752283")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4786284") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4786284.html",
      "https://www.myhome.ie/residential/brochure/_/4786284",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4786284",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("16 Avonbeg Drive, Harbour View, Wicklow Town, Co. Wicklow"),
        propertyDescription = Some("""
        |Forkin Property are delighted to present 16 Avonbeg Drive to the open market For Sale. 16 Avonbeg Drive is a beautifully presented 4 bedroom, 2 bathroom extended and renovated, dormer bungalow style family home brought to the market in excellent condition. This property benefits from a large kitchen/diner extension to the rear of the home and an extension to the home office and utility room to the side of the property. The owners have significanlty improved the property's BER with the addition on triple glazed windows in the extension, a new condensing oil boiler and PV solar panels.
        |
        |Ground floor accommodation comprises of entrance hall, living room, modern kitchen with shaker style cabinets and stone work surfaces, bright and spacious dining area with double doors to the garden, home office, fully fitted utility room, full family bathroom with designer sanitary ware, double bedroom and a double bedroom/family room. First floor accommodation comprises of landing, master bedroom with en suite and fitted wardrobes and a good sized double bedroom.
        |
        |To the front of the property there is parking for 3 cars with an electric car charging point. To the rear of the property there is a beautifully landscaped south facing back garden with raised flower boxes and timber garden shed.
        |
        |No.16 is within walking distance of every conceivable amenity Wicklow Town has to offer including ;primary and secondary schools , shops, restaurants, sports and leisure facilities and transport networks including trains and regional bus routes. The N11/M11 motorway is close by making it ideal for those commuting to Dublin. This property will appeal to a range of purchasers including First Time Buyers and those looking to trade up.
        |
        |Viewing of this property is highly recommended!
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "16 Avonbeg Drive, Harbour View, Wicklow Town, Co. Wicklow",
        propertyEircode = Some(Eircode("A67YH30")),
        propertyCoordinates = Coordinates(
          latitude = 52.982112,
          longitude = -6.057948
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/071269a1-8ff7-4197-a154-44f94edb6043_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/19718902-72ce-48e5-8af2-d24b6bbc9c0c_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/b3a4b698-e1f1-4347-aaa3-9426a2bcdcab_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/20573349-0ae9-402d-82e8-870ab36b1589_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/425ab495-860b-4ce0-83ca-96d5fa52ffbb_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/540efe2e-4536-421b-b969-6862d61e55a2_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/73a2ff92-e656-4514-8f32-14ec704eacb0_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/63fcf588-55d9-4263-9e36-9a37696cd76d_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/59cec4de-85fc-4320-a34e-90cd38481af5_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/428f20e2-ed97-42e4-83a1-8720870096d0_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/6a5d18c6-a404-40e3-bca5-c3f86d0de8b5_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/22b30ad7-b35b-4e17-a252-596a87066e78_l.jpg",
          "https://photos-a.propertyimages.ie/media/4/8/2/4786284/07fd9c42-3bf6-4f79-b48e-8e6762cafa05_l.jpg"
        ),
        propertySize = Area(141, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 141,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.B1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4786284")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
