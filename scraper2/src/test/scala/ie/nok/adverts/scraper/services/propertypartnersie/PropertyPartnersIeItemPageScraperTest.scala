package ie.nok.adverts.scraper.services.propertypartnersie

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

class PropertyPartnersIeItemPageScraperTest extends munit.FunSuite {

  test("4737247") {
    ScraperHelper.assertItemPageScraperResults(
      "services/propertypartnersie/items/4737247.html",
      "https://www.propertypartners.ie/residential/brochure/21-holt-crescent-lugduff-tinahely-wicklow/4737247",
      PropertyPartnersIeItemPageScraper,
      Advert(
        advertUrl = "https://www.propertypartners.ie/residential/brochure/21-holt-crescent-lugduff-tinahely-wicklow/4737247",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 349950,
        propertyIdentifier = Hash.encode("21 Holt Crescent. Lugduff, Tinahely, Wicklow"),
        propertyDescription = Some("""
        |Property Partners O' Brien Swaine are delighted to present this fantastic 5 bedroom dormer residence located on the outskirts of Tinahely village. Holt Crescent is a charming and family-friendly residential development of 22 homes situated in the quaint village of Tinahely, County Wicklow. No 21 comes to the market in excellent condition throughout and offers bright, spacious accommodation with a large enclosed garden and off street parking.
        |
        |Welcoming you to the home is an impressive spacious tiled entrance hallway which leads to all areas of the living accommodation. The separate living room boasts a bay window allowing for natural light to flow through, feature fireplace for cosy winter nights and laminate flooring. The fully fitted kitchen has a range of floor and wall units, ample worktop space, and a door leading to a separate utility room for added convenience and further storage. The ground floor bedrooms are all generous in size with carpet flooring, free-standing wardrobes and would easily accommodate double beds. The master bedroom also benefits from an en-suite bathroom.
        |
        |The carpeted stairs lead to two impressive bedrooms, with carpet flooring and free-standing wardrobes, each with its own en-suite with step-in showers, perfect for the growing family or guests. This spacious well laid out home is ready to walk into and boasts, large entrance hall, kitchen, utility room, sitting room, 5 bedrooms, 3 en suites and family bathroom.
        |
        |Tinahely village is located south of County Wicklow on the Derry River. It has a fantastic community that encourages its residents to get involved in community life by providing lots of entertainment and events. The village boasts a community hall, library, Courthouse Arts centre, craft shop, coffee shop, pubs, and supermarkets. There are two local primary schools, a creche, and a playground. Local link bus provides public transport to the major shopping areas of Gorey and Arklow which are a short drive away.
        |
        |This family home must be viewed to be appreciated.
        """.stripMargin.trim),
        propertyType = Some(PropertyType.Detached),
        propertyAddress = "21 Holt Crescent. Lugduff, Tinahely, Wicklow",
        propertyEircode = Some(Eircode("Y14F832")),
        propertyCoordinates = Coordinates(
          latitude = 52.7949614,
          longitude = -6.454919
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/77437d6a-7727-40bb-adf4-660dce7cbbe9_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/cb391aa4-3e63-430b-aa7f-480dc03344ed_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/adda67ec-861d-4aac-9a78-cbd2e96f3f27_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/50b19fe4-85a0-4467-9e1c-c6556a702f1a_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/55a1b4cb-d952-4ec8-8df6-aee461af8d1d_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/00ab1b47-8ec7-47d7-a9cc-3df07ed24b34_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/0980116d-7eea-488a-a8bd-8fa45f05ccb8_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/5468f509-e980-4c5a-9a88-4b9501032a13_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/d452100f-c862-483a-b51b-82f41cd223dc_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/fa617215-c712-4315-8161-56396bf7400d_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/2290aedd-fd6a-405c-a38b-7daca24f9f16_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/e17b1636-42e4-4b1c-8d7d-271bf22b39d3_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/839dae69-e430-4f41-af6a-9f23880faa2d_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/7ca82d55-f1cb-42f1-ab07-8d81b987a24d_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/f44de282-3614-4fd3-9646-7f6da1a88ea7_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/fef4035a-9d47-40b2-8fa4-0e6eb678cb09_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/23464e07-8211-49cd-b84c-37b90c42cf69_l.jpg",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/f8b6bd68-4c51-4136-9b09-77953e80f935_l.png",
          "https://photos-a.propertyimages.ie/media/7/4/2/4737247/563c37aa-e15e-4a20-ae11-78388d0dc2ae_l.png"
        ),
        propertySize = Area(183, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 183,
        propertyBedroomsCount = 5,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        facets = List(AdvertFacet("https://www.propertypartners.ie/residential/brochure/21-holt-crescent-lugduff-tinahely-wicklow/4737247")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("4713362") {
    ScraperHelper.assertItemPageScraperResults(
      "services/propertypartnersie/items/4713362.html",
      "https://www.propertypartners.ie/residential/brochure/21-crobally-heights-tramore-waterford/4713362",
      PropertyPartnersIeItemPageScraper,
      Advert(
        advertUrl = "https://www.propertypartners.ie/residential/brochure/21-crobally-heights-tramore-waterford/4713362",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 0,
        propertyIdentifier = Hash.encode("21 Crobally Heights, Tramore, Waterford"),
        propertyDescription = Some("""
        |No.21 Crobally Heights is a spacious and attractive five bedroom semi -detached property measuring c. 1,676 sq ft, situated in a quiet cul-de-sac in a mature residential area located just off the ring road, within walking distance of shops, schools, the town centre and main bus route. The property is presented in excellent condition boasting many upgrades including new windows &amp; doors and underfloor heating. Ground floor accommodation comprises an entrance hall, sitting room, open plan dining room / kitchen, utility &amp; shower room and office/ playroom. The first floor contains five bedrooms , bathroom &amp; en-suite. Viewing is advised.
        """.stripMargin.trim),
        propertyType = Some(PropertyType.SemiDetached),
        propertyAddress = "21 Crobally Heights, Tramore, Waterford",
        propertyEircode = Some(Eircode("X91CX27")),
        propertyCoordinates = Coordinates(
          latitude = 52.1696286,
          longitude = -7.1522634
        ),
        propertyImageUrls = List(
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/285d46eb-0b92-4db9-bd49-316d437d3c38_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/226f4f7b-c8ad-4256-a505-a45c6cbcba80_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/981540c2-137e-4395-8563-9114c98825dc_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/804aefd0-ab3d-4075-b028-331084bcb825_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/6333cdbc-1cc5-4058-80f3-8328982b6b2a_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/a3728c65-343c-4e89-8a4c-bea406f893df_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/298313e4-ee89-46d5-b483-2775e7163a3a_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/bbb4bc54-a56e-4361-8489-94da2dfd729a_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/a7c7df44-81b3-47b7-bb2a-3c9c5807abf9_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/b7d14443-16cd-425f-8e44-532104fb460d_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/e6d985b2-57f6-4111-90a1-7fb7354c670e_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/03f15825-b478-46d5-a18e-ec72de12d372_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/cb72d419-7b9d-4da1-939f-d82e4e1398fe_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/3a7ae83b-500a-4bd7-9f56-b1765394c694_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/5845cd16-810e-4fc6-8cc9-f3a52b42a0a2_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/d23ef5d8-58fa-40d7-aea7-e49fdff5234f_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/29113e5d-550d-457a-88c8-a329d81935a2_l.jpg",
          "https://photos-a.propertyimages.ie/media/2/6/3/4713362/d53112bc-43fa-45a7-a6e6-5e760c72a0c2_l.jpg"
        ),
        propertySize = Area(155.71, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 155.71,
        propertyBedroomsCount = 5,
        propertyBathroomsCount = 0,
        propertyBuildingEnergyRating = Some(Rating.C1),
        propertyBuildingEnergyRatingCertificateNumber = Some(116552324),
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = Some(174.93),
        facets = List(AdvertFacet("https://www.propertypartners.ie/residential/brochure/21-crobally-heights-tramore-waterford/4713362")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }
}
