package ie.nok.adverts.scraper.services.allenandjacobsie

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

class AllenAndJacobsIeItemPageScraperTest extends munit.FunSuite {

  test("816199") {
    ScraperHelper.assertItemPageScraperResults(
      "services/allenandjacobsie/items/816199.html",
      "https://allenandjacobs.ie/agent/78090/property/816199/3-bed-House-For-Sale-in-Monkstown/26-Abbey-Park-Monkstown-Co-Dublin",
      AllenAndJacobsIeItemPageScraper,
      Advert(
        advertUrl = "https://allenandjacobs.ie/agent/78090/property/816199/3-bed-House-For-Sale-in-Monkstown/26-Abbey-Park-Monkstown-Co-Dublin",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 660000,
        propertyIdentifier = Hash.encode("26 Abbey Park, Monkstown, Co. Dublin"),
        propertyDescription = Some("""
          |Description
          |
          |Allen &amp; Jacobs is delighted to present this extended three-bedroom semi-detached house stretching to approximately 103 sq/m of accommodation and boasting a wonderful and private rear garden measuring 18m in length and a large front garden with ample off-street parking. Extremely well maintained by its previous owner, No. 26, offers endless possibilities to further extend and convert subject to planning permission. The bright and spacious accommodation briefly comprises reception hall, living room, dining room, kitchen, guest wc, three bedrooms and a bathroom.
          |
          |Location
          |
          |Nestled away in this quiet cul de sac, location really couldnt be better. Situated adjacent to Blackrock Rugby Club and close to Monkstown, Dun Laoghaire, Blackrock &amp; Deansgrange and surrounded by all amenities including schools, colleges, parks, shops and public transport. The property is also within easy reach of the city centre and the M50 allowing easy access to all national routes.
          |
          |Viewing strongly recommended
          |
          |Features
          |
          |C. 17m rear garden plus large front garden with off street parking
          |
          |Semi-detached house of approximately 103sq/m
          |
          |PVC double glazed windows throughout.
          |
          |Quite cul de sac position
          |
          |Close by to all amenities
          |
          |Easy reach of the city &amp; all transport route via M50
          |
          |GFCH
          |
          |Phone &amp; TV Connection
          |
          |
          |Accommodation
          |
          |Entrance porch: 2.1m x 0.7m:
          |
          |Reception Hall: 3.95m x 2.1m:
          |
          |Dining Room: 3.4m x 3.35m:
          |
          |Living Room: 4.25m x 3.4m:
          |
          |Breakfast Room: 3.1m x 2.1m:
          |
          |Kitchen: 2.6m x 2.2m:
          |
          |Rear Hall: 2.6m x 1.25m:
          |
          |Shower Room: 2.9m x 2.6m:
          |
          |Upstairs:
          |
          |Landing: 2.85m mx 2.5m:
          |
          |Bedroom 1: 4.25m x 3.4m:
          |
          |Bedroom 2: 3.4m x 3.1m:
          |
          |Bedroom 3: 2.5m x 2.4m:
          |
          |Bathroom: 2.35m x 2.1m:
          |
          |Outside
          |To the front is a large garden with ample street parking. To the rear is a large c. 17m secluded.
          |
          |Strictly by prior appointment only with sole agents
          |Allen &amp; Jacobs (Southside Office)
          |107 Fosters Avenue
          |Mount Merrion, Co. Dublin
          |t : 2100360 f : 2789494
          |w : allenandjacobs.ie
          |
          |
          |Negotiator
          |Andrew Allen MIPAV MCCEPI
        """.stripMargin.trim),
        propertyType = Some(PropertyType.House),
        propertyAddress = "26 Abbey Park, Monkstown, Co. Dublin",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 53.28534,
          longitude = -6.159007
        ),
        propertyImageUrls = List(
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_038ac728.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_06f02472.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_a85765b8.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_ad2e3807.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_0032281d.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_9c81ea07.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_8ab222c3.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_cdc31efa.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_12494253.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_e67630cf.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_d80000f9.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_a701b563.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_68711811.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_3e382c18.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_2fbd6aa7.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_275b4a59.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_fc0ab07e.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_336ea03d.jpg",
          "https://storage.googleapis.com/4pm-pd-files/816199/s/26-abbey-park-monkstown-co-dublin-85115b71_6da0dc7f_7214c8f7_9e327546.jpg"
        ),
        propertySize = Area(103, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 103,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = Some(Rating.D2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://allenandjacobs.ie/agent/78090/property/816199/3-bed-House-For-Sale-in-Monkstown/26-Abbey-Park-Monkstown-Co-Dublin")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("806833") {
    ScraperHelper.assertItemPageScraperResults(
      "services/allenandjacobsie/items/806833.html",
      "https://allenandjacobs.ie/agent/78090/property/806833/77-Westbourne-Lodge-Knocklyon-Dublin-16",
      AllenAndJacobsIeItemPageScraper,
      Advert(
        advertUrl = "https://allenandjacobs.ie/agent/78090/property/806833/77-Westbourne-Lodge-Knocklyon-Dublin-16",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("77 Westbourne Lodge, Knocklyon, Dublin 16"),
        propertyDescription = Some("""
          |Allen &amp; Jacobs is delighted to present this lovely semi-detached bay windowed family home providing c.94sqm/1,012sqft of bright well laid accommodation. Presented in excellent condition throughout, no.77 is positioned in a quiet cul de sac in this very popular and convenient address. Accommodation briefly comprises entrance hall, living room, open plan kitchen/dining room, 3 bedrooms and family bathroom.
          |
          |Situated in a quiet residential enclave close by to all amenities including schools (St. Colmciles National School/ Santa Maria/Colasite Eanna), shops, parks, public transport, and within easy access to the city centre and all major routes via the M50.
          |
          |Viewing highly recommended.
          |
          |At A Glance
          |Well-proportioned accommodation c.94sqm/1,012sqft
          |Presented in excellent condition
          |GFCH
          |Landscaped low maintenance rear garden
          |Double glazed windows throughout
          |Side passage
          |Cul de sac position
          |Off street parking
          |Easy Reach of the City &amp; All Transport Route Via M50
          |Phone &amp; TV Connection
          |
          |Accommodation
          |
          |Entrance hall: 4.54m x 1.75m: With understairs storage.
          |
          |Living Room: 5.24m x 3.5m: attractive open fire place, bay window.
          |
          |Kitchen/Dining Room: 5.38m x 3.27m: Kitchen with fully fitted eye &amp; floor level press units, stainless steel sink unit, gas oven &amp; hob with extractor fan, double doors to garden.
          |
          |Upstairs
          |
          |Landing: 2.7m x 1.88m: Shelved hot press with dual immersion.
          |
          |Bedroom 1: 3.78m x 3.44m: Built in double wardrobes.
          |
          |Bathroom: 2.35m x 1.9m: Bath, whb, wc,
          |
          |Bedroom 2: 3.78m x 3.44m: Built in wardrobes.
          |
          |Bedroom 3: 2.6m x 2.15m: Built in wardrobes.
          |
          |Outside
          |Ample off street parking to the front. A side passage leads to a good sized private low maintenance rear garden which is laid out in tarmac &amp; artificial grass
          |
          |Strictly by prior appointment only with sole agents
          |Allen &amp; Jacobs (Southside Office)
          |107 Fosters Avenue
          |Mount Merrion, Co. Dublin
          |t : 2100360
          |w : allenandjacobs.ie
        """.stripMargin.trim),
        propertyType = Some(PropertyType.House),
        propertyAddress = "77 Westbourne Lodge, Knocklyon, Dublin 16",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 53.28759,
          longitude = -6.319354
        ),
        propertyImageUrls = List(
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_776ba6ca.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_6653cc93.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_7a90ed78.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_b1de668f.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_01171b44.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_0f4e170d.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_6462bdb7.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_a911701d.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_0ed10c16.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_365830a9.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_c4fe019a.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_fe5f6d0a.jpg",
          "https://storage.googleapis.com/4pm-pd-files/806833/s/77-westbourne-lodge-knocklyon-dublin-16-85115b71_6da0dc7f_6b0ea6c0_f8439ac1.jpg"
        ),
        propertySize = Area(94, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 94,
        propertyBedroomsCount = 3,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = Some(Rating.D2),
        propertyBuildingEnergyRatingCertificateNumber = Some(107519282),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(268.33),
        facets = List(AdvertFacet("https://allenandjacobs.ie/agent/78090/property/806833/77-Westbourne-Lodge-Knocklyon-Dublin-16")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
