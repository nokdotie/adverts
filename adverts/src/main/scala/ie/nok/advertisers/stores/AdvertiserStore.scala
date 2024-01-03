package ie.nok.advertisers.stores

import zio.ZIO
import ie.nok.advertisers.Advertiser

trait AdvertiserStore {
  def getByPropertyServicesRegulatoryAuthorityLicenceNumber(
      propertyServicesRegulatoryAuthorityLicenceNumber: String
  ): ZIO[Any, Throwable, Option[Advertiser]]
}

object AdvertiserStore {
  def getByPropertyServicesRegulatoryAuthorityLicenceNumber(
      propertyServicesRegulatoryAuthorityLicenceNumber: String
  ): ZIO[AdvertiserStore, Throwable, Option[Advertiser]] =
    ZIO.serviceWithZIO[AdvertiserStore](
      _.getByPropertyServicesRegulatoryAuthorityLicenceNumber(
        propertyServicesRegulatoryAuthorityLicenceNumber
      )
    )
}
