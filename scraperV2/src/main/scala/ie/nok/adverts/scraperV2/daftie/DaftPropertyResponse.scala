package ie.nok.adverts.scraperV2.daftie

object DaftPropertyResponse {

  protected[daftie] case class DaftResponse(listings: List[Listings])

  protected[daftie] case class Listings(listing: Listing)

  protected[daftie] case class Listing(
      floorArea: Option[FloorArea],
      media: Media,
      numBathrooms: Option[String],
      numBedrooms: Option[String],
      price: String,
      seoFriendlyPath: String,
      title: String,
      point: Point,
      ber: Option[Ber],
      seller: Seller
  )

  protected[daftie] case class FloorArea(unit: String, value: String)

  protected[daftie] case class Media(images: Option[List[Image]])

  protected[daftie] case class Image(size720x480: String)

  protected[daftie] case class Point(coordinates: List[BigDecimal])

  /** Ber data
    *
    * @param rating
    *   energy rating, eg. A3, C2.
    * @param code
    *   unique identifier of the BER certificate
    * @param epi
    *   primary energy use per unit floor area per year (kWh/m2/yr)
    */
  protected[daftie] case class Ber(
      rating: Option[String],
      code: Option[String],
      epi: Option[String]
  )

  protected[daftie] case class Seller(
      sellerId: Int,
      name: String,
      phone: String,
      alternativePhone: Option[String],
      address: Option[String],
      branch: Option[String],
      profileImage: Option[String],
      standardLogo: Option[String],
      squareLogo: Option[String],
      licenceNumber: Option[String]
  )
}
