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

  test("4785119") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4785119.html",
      "https://www.myhome.ie/residential/brochure/_/4785119",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4785119",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 500000,
        propertyIdentifier = Hash.encode("59, New Road, Clondalkin, Dublin 22"),
        propertyDescription = Some("""
        |TOM MAHER &amp; COMPANY SCSI RICS IPAV – This imposing 5 bed semi-detached EXTENDED family home is sure to please all who view, offering tremendous scope and potential for further development or extension subject to the customary planning consents. The site area is .11hectares / .27 acre the property is further enhanced by a c. 173 ft rear garden, complete with large yard and extensive lawn area perfect for family living. Built circa 1920’s the property has been substantially extended providing generously proportioned accommodation with the added benefit of a downstairs bedroom and shower room. The property has been meticulously maintained, lovingly cared for and upgraded over the years this exceptional home must be viewed to fully appreciate its size, quality of finish and extensive grounds. Early viewing is strongly advised. REGISTER YOR INTERST BY EMAIL.
        |
        |Prominently position on New Road circa .2k Clondalkin Village, via Laurel Park - 10.3k Dublin City Centre via the Naas Road. The property is conveniently located adjacent to excellent Public transport, links, bus routes and the Luas Red Line (Red Cow Stop). The N7 &amp; M50 motorways are also easily accessed, as are the Mill, Liffey Valley and the Square Shopping Centres, Round Tower Heritage Centre. Clondalkin Village is nearby as is picturesque Corkagh Park with its 700 acres of parkland.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "59, New Road, Clondalkin, Dublin 22",
        propertyEircode = Some(Eircode("D22YR25")),
        propertyCoordinates = Coordinates(
          latitude = 53.3169849,
          longitude = -6.3910736
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/9/1/1/4785119/74eb388f-8bde-4f4c-83a9-02d32bf25dad_x.jpg",
          "https://photos-a.propertyimages.ie/media/9/1/1/4785119/be149cd8-abd8-427e-913c-9525366e2bcd_l.jpg",
          "https://photos-a.propertyimages.ie/media/9/1/1/4785119/689d3f3d-a04d-47e6-b015-403fd4c48e06_l.jpg",
          "https://photos-a.propertyimages.ie/media/9/1/1/4785119/90fe318a-2bb7-4168-ab08-59e86e87da77_l.jpg",
          "https://photos-a.propertyimages.ie/media/9/1/1/4785119/4f9b55a8-0980-4a13-bb59-7faa866eceaf_l.jpg"
        ),
        propertySize = Area(138, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 138,
        propertyBedroomsCount = 5,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = None,
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4785119")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4784321") {
    ScraperHelper.assertItemPageScraperResults(
      "services/myhomeie/items/4784321.html",
      "https://www.myhome.ie/residential/brochure/_/4784321",
      MyHomeIeItemPageScraper,
      Advert(
        advertUrl = "https://www.myhome.ie/residential/brochure/_/4784321",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 239000,
        propertyIdentifier = Hash.encode("Apt 8, Cois Abhainn, Clane, Co Kildare"),
        propertyDescription = Some("""
        |Leinster Property presents this spacious 2 bed 1st floor apartment in the Abbey, Cois Abhainn, an exclusive development situated beside the River Liffey in Clane. The property is tastefully presented and maintained by the current owner.
        |
        |Clane town is only a few minutes stroll and offers excellent amenities from local restaurants, cafes, bars, shops and plenty of leisure facilities. There is also a good selection of primary and secondary schools. There are regular bus services to and from Dublin City Centre and the M4 and N7 are only a few minutes away offering connectivity to Dublin City Centre and Dublin Airport.
        |
        |Sallins Village is less than 10 minutes drive and offers regular rail services. Maynooth is also less than 20 mins away offering public transport, cafes, bars, restaurants and shops.
        |
        |Clane offers plenty of scenic walks from the paved path by the River Liffey to the picturesque Donadea Forest.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "Apt 8, Cois Abhainn, Clane, Co Kildare",
        propertyEircode = Some(Eircode("W91X398")),
        propertyCoordinates = Coordinates(
          latitude = 53.2866595529527,
          longitude = -6.68460037249146
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/1/2/3/4784321/ffded371-fbdb-4295-89e2-ba24b3f90ced_x.jpeg",
          "https://photos-a.propertyimages.ie/media/1/2/3/4784321/07bef756-e66e-4608-a1ce-a468a7eab4af_l.jpeg",
          "https://photos-a.propertyimages.ie/media/1/2/3/4784321/a79ad105-8e38-4017-b6ef-0292924e98a6_l.jpeg",
          "https://photos-a.propertyimages.ie/media/1/2/3/4784321/fb06291d-6ed3-40dc-8c86-95a0166e5e86_l.jpeg",
          "https://photos-a.propertyimages.ie/media/1/2/3/4784321/15ade1f6-1695-4b70-ab9a-e166e479d2a6_l.jpeg"
        ),
        propertySize = Area(68, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 68,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 2,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4784321")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

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
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/44b3689d-d1ee-46e1-8f70-16d4b5d8767c_x.jpg",
          "https://img.youtube.com/vi/xXg3QgVq2Ro/hqdefault.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/bf02dfd2-e118-4aeb-b086-92da3b0ffb08_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/02f406ce-cf8a-4a2d-873c-00f1dd0e7d4c_l.jpg",
          "https://photos-a.propertyimages.ie/media/0/9/3/4785390/b831cb58-9d68-4fc0-ae76-32a6d1c3587c_l.jpg"
        ),
        propertySize = Area(131, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 131,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://www.myhome.ie/residential/brochure/_/4785390")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4786237 - size") {
    val document = ScraperHelper.getDocument(
      "services/myhomeie/items/4786237.html",
      "https://www.myhome.ie/residential/brochure/_/4786237"
    )

    val size = MyHomeIeItemPageScraper.getSize(document)
    assertEquals(size, Area(234, AreaUnit.SquareMetres))
  }

  test("4752283 - ber") {
    val document = ScraperHelper.getDocument(
      "services/myhomeie/items/4752283.html",
      "https://www.myhome.ie/residential/brochure/_/4752283"
    )

    val rating = MyHomeIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(rating, Some(Rating.C3))

    val certificateNumber = MyHomeIeItemPageScraper.getBuildingEnergyRatingCertificateNumber(document)
    assertEquals(certificateNumber, Some(116936360))
  }

  test("4786284 - filter") {
    val document = ScraperHelper.getDocument(
      "services/myhomeie/items/4786284.html",
      "https://www.myhome.ie/residential/brochure/_/4786284"
    )

    val filter = MyHomeIeItemPageScraper.filter(document)
    assertEquals(filter, false)
  }
}
