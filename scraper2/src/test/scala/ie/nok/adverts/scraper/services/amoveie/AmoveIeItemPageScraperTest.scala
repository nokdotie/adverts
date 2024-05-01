package ie.nok.adverts.scraper.services.amoveie

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

class AmoveIeItemPageScraperTest extends munit.FunSuite {

  test("1") {
    ScraperHelper.assertItemPageScraperResults(
      "services/amoveie/items/1.html",
      "https://amove.ie/listing/1-white-abbey-lawns-kildare-co-kildare/",
      AmoveIeItemPageScraper,
      Advert(
        advertUrl = "https://amove.ie/listing/1-white-abbey-lawns-kildare-co-kildare/",
        advertSaleStatus = AdvertSaleStatus.SaleAgreed,
        advertPriceInEur = 400000,
        propertyIdentifier = Hash.encode("1 White Abbey Lawns, Kildare, Co. Kildare"),
        propertyDescription = Some("""
          |Sale Type: For Sale by Private Treaty
          |
          |Overall Floor Area: 124 m²
          |
          |AMOVE is delighted to welcome buyers to Number 1 White Abbey Lawns, this smart 4 bed home is positioned in a town location that cannot be beaten. A pristine, cleverly designed, lovely home that is tastefully presented and comes with a fantastic, exceptionally large west facing garden with the benefit of full planning to extend to the front, side and rear (KCC21/211). White Abbey Lawns is a small exclusive development of homes directly opposite Round Towers GAA pitches. Recently redecorated throughout, this smart 4-bedroom home is ready for any buyer to move, but equally could be extended if desired. A bright spacious home extending to 124 sqm with all the towns amenities on your doorstep.
          |
          |Accommodation comprises: Reception Hall, Livingroom, Dining room / Kitchen, Utility, Guest w.c.. Bedroom / Office Upstairs: 3 large bedrooms with Master bedroom en-suite and main bathroom.
          |
          |To the rear is a superb, exceptionally private south / west facing garden, that enjoys the sun all day and comes with the benefit or a large steel garden shed.
          |
          |White Abbey Lawns location is second to none with every amenity within minutes of this properties doorstep. A car is simply not needed at this address. Tesco, the town centre, St. Brigid’s Park, the National Stud, choice local schools / creche’s, Kildare Train station are all within walking distance of this exclusive address. This ‘drop your bags home’ is also situated mere minutes’ drive from Junction 13 beside Kildare Village so within easy reach of the M7 to Dublin and south of the country.
          |
          |Viewing comes highly recommended
          |
          |ACCOMMODATION
          |
          |GROUND FLOOR
          |
          |Entrance Hall: 4.90m x 1.74m with laminate flooring, ceiling coving, radiator, alarm panel &amp; door to
          |
          |Living Room: 4.76m x 3.42m (excludes bay) laminate flooring, ceiling coving, feature Limestone carved mantle with inset stove and granite hearth, bay window, double doors to
          |
          |Kitchen/Dining Room: 5.45m x 5.29m with porcelain tiled floor, fully fitted kitchen (new in 2020) with a range of high &amp; low cabinets, inset single sink unit, integrated dishwasher, integrated Electrolux double oven and integrated microwave, electric Zanussi hob with Faber extractor fan over, breakfast counter, space for American Fridge, Freezer and additional lower cabinets, French doors to patio and rear garden
          |
          |Utility Room: 1.8m x 2.58m with porcelain tiled floor, wall mounted storage cabinets, space for dryer and plumbed for washing machine, door to garden
          |
          |Guest WC: 2.57m x 0.75m with porcelain tiled floor, w.c., &amp; vanity w.h.b. with mirror over, towel rail and window
          |
          |Bedroom 4 / Office / Playroom: 2.75m x 2.54m Laminate flooring and attic hatch
          |
          |FIRST FLOOR
          |
          |Landing: Carpeted stairs and landing, window over stairs
          |
          |Bedroom 1 (Master – Rear): 4.28m x 3.41m Master double bedroom with carpeted flooring, integrated wardrobes and door to
          |
          |En-suite: 1.46m x 1.78m Fully tiled walls and tiled floor, step-in corner shower with electric MIRA shower, vanity w.h.b. with mirror over, w.c., chrome wall mounted radiator and window
          |
          |Hot press: hot water tank &amp; a range of shelves
          |
          |Bedroom 2:(Front) 2.66m x 2.55m Single room with carpeted flooring with fitted wardrobes
          |
          |Bedroom 3 (Front): 3.78m x 2.67m (excluding bay) Double bedroom with carpeted flooring and integrated wardrobes
          |
          |Bathroom: 2.29m x 1.79 (max) Partially tiled walls, tiled floor, vanity w.h.b. with mirror over, w.c. and bath with shower hose, chrome wall mounted radiator and window
          |
          |OUTSIDE
          |
          |To the front:
          |
          |Generous parking forecourt with lawn on both sides and wall surround.
          |
          |To the rear:
          |
          |Superb private southwest facing rear garden 11m wide (36ft) by 15m (50ft) deep from back wall to rear boundary, Indian sandstone curved patio off dining room opening to manicured garden, laid to lawn with concrete steps to steel shed. Rear garden with bark play area. Gated side access on both sides of house and outdoor tap. Oil tank and Grant Vortex oil burner.
          |
          |Steel shed with concrete base.
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "1 White Abbey Lawns, Kildare, Co. Kildare",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 53.16081149999999,
          longitude = -6.9161101
        ),
        propertyImageUrls = List(
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-Cropped-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.1-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.2-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.3-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.0-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.1-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.3-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.4-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.5-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-6.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.2-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-10.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-10.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.2-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.5.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.6.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.7.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.8.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-13.0-1-Floorplan-1-White-Abbey-Lawns-Kildare_2D.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-14.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-14.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-14.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-3.jpg"
        ),
        propertySize = Area(124, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 124,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://amove.ie/listing/1-white-abbey-lawns-kildare-co-kildare/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("56") {
    ScraperHelper.assertItemPageScraperResults(
      "services/amoveie/items/56.html",
      "https://amove.ie/listing/apartment-56-station-house-sallins-co-kildare-2/",
      AmoveIeItemPageScraper,
      Advert(
        advertUrl = "https://amove.ie/listing/apartment-56-station-house-sallins-co-kildare-2/",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 200000,
        propertyIdentifier = Hash.encode("Apartment 56, Station House, Sallins, Co. Kildare"),
        propertyDescription = Some("""
          |Sale Type: For Sale by Private Treaty
          |
          |Overall Floor Area: 60 m²
          |
          |2nd FLOOR 2 BED APARTMENT WITH PARKING
          |
          |AMOVE are delighted to present this 2nd floor, 2-bedroom apartment to the market. This smart, spacious home is located in a small block of 8 apartments, adjacent to Sallins train station. No. 56 has a wonderful, elevated aspect with a balcony view of the Kildare countryside. There is a communal roof terrace on the third floor for use by the residents. Extending to 60 sq.m (646 sq.ft) this is a well-appointed 2-bedroom home with parking.
          |
          |Accommodation comprises; Reception Entrance, Lounge / Dining /Kitchen, Utility Room, Hot Press, 2 bedrooms (double and single) and a large bathroom.
          |
          |The Waterways is located just a few minutes’ walk from Sallins trains station and is only a short drive to N7/M7 Motorway and the new Sallins Bypass. Schools, Childcare, Restaurants, Pubs, Cafes, Supervalu, Post Office, pharmacies and other local amenities are situated in the Waterways complex with Monread Shopping Centre only a short drive away.
          |
          |‘This is an ideal property for first time buyers, for those trading up, as an investment, or for anyone looking for a centrally located property with great transport links’
          |
          |ACCOMMODATION:
          |
          |Reception Entrance: 5.97m x 1.66m Laminate flooring, telephone intercom, storage heater, opening to
          |
          |Dining / Lounge: 2.97m x 4.74m Laminate Flooring and storage heater, door leading to balcony
          |
          |Balcony: 2.17m x 0.98m
          |
          |Kitchen: 2.27m x 2.58m Fully fitted kitchen with a range of high and low oak cabinets, tiled over worktops, tiled floor, integrated electric hob, with extractor over, Zanussi oven, cabinet for Fridge/Freezer and single drainer sink
          |
          |Bathroom: 2.2m x 1m Fully tiled, pedestal w.h.b. with mirror over, bath with telephone shower over, extractor fan, towel rail and w.c.
          |
          |Storage Room: 1.22m x 1.42m laminate floor, plumbed for washing machine, space for dryer and additional storage space
          |
          |Hotpress: Water tank and shelving
          |
          |Bedroom 1: 4.55m x 2.49m Double room with laminate flooring, electric radiator and door to door leading to balcony
          |
          |Bedroom 2: 2.92m x 3.49m Single room, laminate flooring, fitted wardrobes and window overlooking balcony
          |
          |Property Features
          |
          |- Bright, spacious interior extending to 60 sq.m (646 sq.ft)
          |
          |- 2 bedrooms (double and single room)
          |
          |- Storage heating, Electric heating, BER C2 &amp; double glazing
          |
          |- Laminate flooring throughout
          |
          |- Integrated kitchen appliances included in sale
          |
          |- Car parking space included
          |
          |- Coded refuse collection area
          |
          |- Lift access
          |
          |- Management Fees include parking, insurance, block maintenance, lights in communal areas, upkeep of common areas and refuse collection
          |
          |- Located beside the Sallins train station and close to N7/M7
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "Apartment 56, Station House, Sallins, Co. Kildare",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 53.24664980000001,
          longitude = -6.661545900000001
        ),
        propertyImageUrls = List(
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-5.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.2-5.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.3-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.4-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.0-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.1-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.2-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.3-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.5-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.6-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.0-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.1-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.0-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.0-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-6.0-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-6.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-6.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.1-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.2-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.1-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-10.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-Cropped-4.jpg"
        ),
        propertySize = Area(60, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 60,
        propertyBedroomsCount = 2,
        propertyBathroomsCount = 1,
        propertyBuildingEnergyRating = Some(Rating.C2),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://amove.ie/listing/apartment-56-station-house-sallins-co-kildare-2/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("137") {
    ScraperHelper.assertItemPageScraperResults(
      "services/amoveie/items/137.html",
      "https://amove.ie/listing/137-branswood-athy-co-kildare/",
      AmoveIeItemPageScraper,
      Advert(
        advertUrl = "https://amove.ie/listing/137-branswood-athy-co-kildare/",
        advertSaleStatus = AdvertSaleStatus.ForSale,
        advertPriceInEur = 275000,
        propertyIdentifier = Hash.encode("137 Branswood, Athy, Co. Kildare"),
        propertyDescription = Some("""
          |Sale Type: For Sale by Private Treaty
          |
          |Overall Floor Area: 120 m²
          |
          |Welcome to 137 Branswood, this is a bright, spacious 4 bed home, recently redecorated with an impressive energy efficient B3 BER rating. Extending to an impressive 120sq.m (1,291 sq.ft) with a superb south facing garden extending to 10.4 sqm (34ft) long and 10.6sqm (34ft) wide with a generous side garden. 137 is approached by a wonderful driveway and sits privately, at the end of this quiet cul de sac.
          |
          |Accommodation comprises: Hall, Lounge, Dining room, Kitchen, Utility Room, Guest W.C and upstairs there are 3 double rooms (master en-suite), 1 large single &amp; a family bathroom.
          |
          |137 enjoys a prime end position in this popular Athy estate complemented by a large driveway and side garden. A short walk from the town centre, with all the towns amenities on hand, great local shops, restaurants, pubs, both national schools and a choice of secondary schools on hand. This smart home is just under 50 minutes’ drive to Newlands Cross and 20 minutes’ drive to Carlow Town. For buyers commuting the M9 and M7 are on hand as is an excellent commuter service by train. For sporting enthusiasts Athy has it all, an excellent swimming pool and Gym at K Leisure, an 18-hole golf club, GAA club, Tennis club, Athy Triathlon club, Badminton club, Bowling, Rugby and Soccer club. There is also 2 playgrounds on hand and local shopping at Petits, Lidl &amp; Aldi.
          |
          |The new southern distribution road has reduced commuting times to Dublin and gives easier access to main road networks.
          |
          |Viewing comes highly recommended
          |
          |ACCOMMODATION
          |
          |Entrance Hall: 6.10m x 1.90m ceiling coving, laminate flooring, alarm panel, radiator
          |
          |Guest W.C.: 1.42m x 1.29m Pedestal w.h.b. with tiled backsplash, w.c. and window
          |
          |Living Room: 3.91m x 4.911 (excluding bay) Laminate Flooring, bay window, ceiling coving, open fireplace with granite hearth, cast iron insert &amp; surround and painted wooden mantlepiece double doors to
          |
          |Dining Room: 2.79m x 4.29m Laminate flooring, ceiling coving, radiator and French doors to garden
          |
          |Kitchen: 5.94m x 2.98m (max) Fully fitted kitchen with a range of high &amp; low cabinets with tiled backsplash, Fridge/Freezer, integrated Zanussi oven &amp; 4 gas ring hob, extractor hood over, single sink unit and plumbed for dishwasher (Normende), door to
          |
          |Utility Room: Plumbed for washing machine and gas boiler (Vokera), door to side garden, hot press with Watertank and shelved
          |
          |FIRST FLOOR
          |
          |Hall: side window, carpeted, attic hatch and radiator
          |
          |Bedroom 1: 2.95m x 2.42m Single room with fitted wardrobes and carpeted flooring
          |
          |Bedroom 2: 3.75m (max) x 3.42m Double bedroom, carpeted flooring &amp; fitted wardrobes
          |
          |Bedroom 3: 3.35m (max) x 2.7m Double bedroom, carpeted flooring, shelving &amp; integrated wardrobes
          |
          |En-suite: .1.77m x 1.50m Pedestal w.h.b. with tiled backsplash, w.c. and corner shower fully tiled
          |
          |Bathroom: 2.20m x 1.94m Bath, radiator, w.c,, pedestal w.h.b.
          |
          |Bedroom 4: 2.52m x 2.71m large single &amp; carpeted flooring
          |
          |OUTSIDE
          |
          |To the front:
          |
          |To the front of the property there is a large concrete parking forecourt, bordered concrete wall on one side and gated side access to large rear garden
          |
          |To the rear:
          |
          |Landscaped garden, fully enclosed, walled to side &amp; fence panelling to rear and one side. This property has a south facing garden with a covered patio area off dining room. To the side there is gated access with a concrete pathway, giving the home bin &amp; side storage space and outdoor tap.
          |
          |Property Features
          |
          |- Freshly painted, drop you bags living accommodation – 120 sq.m (1,291 sq.ft)
          |
          |- Excellent position with large driveway and good off-street parking to front of property
          |
          |- Energy efficient timber framed home – BER B3
          |
          |- GFCH &amp; double glazing throughout
          |
          |- Gated side access, exterior tap and light
          |
          |- Ample green space within development
          |
          |- Timber Barna Shed
          |
          |- Carpets, blinds and integrated appliance included in sale
        """.stripMargin.trim),
        propertyType = None,
        propertyAddress = "137 Branswood, Athy, Co. Kildare",
        propertyEircode = None,
        propertyCoordinates = Coordinates(
          latitude = 52.9839237,
          longitude = -6.995303799999999
        ),
        propertyImageUrls = List(
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-Cropped-3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.0-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-1.1-4.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-2.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.1-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.2-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-3.4-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.1-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-4.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-5.0-2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-6.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-7.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-8.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-9.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-10.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-10.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-11.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.0-1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-12.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-13.0.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-13.1.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-13.2.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-13.3.jpg",
          "https://amove.ie/wp-content/uploads/2024/03/web_-14.-3-Floorplan-137-Branswood-Athy_2d.jpg"
        ),
        propertySize = Area(120, AreaUnit.SquareMetres),
        propertySizeInSqtMtr = 120,
        propertyBedroomsCount = 4,
        propertyBathroomsCount = 3,
        propertyBuildingEnergyRating = Some(Rating.B3),
        propertyBuildingEnergyRatingCertificateNumber = None,
        propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
        sources = List.empty,
        facets = List(AdvertFacet("https://amove.ie/listing/137-branswood-athy-co-kildare/")),
        advertiser = None,
        createdAt = Instant.now()
      )
    )
  }

  test("14 - sale status") {
    val document = ScraperHelper.getDocument(
      "services/amoveie/items/14.html",
      "https://amove.ie/listing/4-distillery-court-monasterevin-co-kildare/"
    )

    val saleStatus = AmoveIeItemPageScraper.getSaleStatus(document)
    assertEquals(saleStatus, AdvertSaleStatus.Sold)
  }

  test("23 - eircode") {
    val document = ScraperHelper.getDocument(
      "services/amoveie/items/23.html",
      "https://amove.ie/listing/23-ardrew-court-athy-co-kildare/"
    )

    val eircode = AmoveIeItemPageScraper.getEircode(document)
    assertEquals(eircode, Some(Eircode("R51TR23")))
  }

  test("23 - ber") {
    val document = ScraperHelper.getDocument(
      "services/amoveie/items/23.html",
      "https://amove.ie/listing/23-ardrew-court-athy-co-kildare/"
    )

    val eircode = AmoveIeItemPageScraper.getBuildingEnergyRating(document)
    assertEquals(eircode, None)
  }

  test("65 - price") {
    val document = ScraperHelper.getDocument(
      "services/amoveie/items/65.html",
      "https://amove.ie/listing/65-blackmillers-hill-kildare-town-co-kildare-sale-agreed/"
    )

    val price = AmoveIeItemPageScraper.getPriceInEur(document)
    assertEquals(price, 0)
  }
}
