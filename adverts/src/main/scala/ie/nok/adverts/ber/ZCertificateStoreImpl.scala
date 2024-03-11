package ie.nok.adverts.ber

import ie.nok.ber.{Certificate, CertificateNumber, Eircode}
import ie.nok.ber.stores.CertificateStore
import ie.nok.adverts.{Advert, AdvertUrl}
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}
import zio.json.EncoderOps

trait ZCertificateStore {
  def getAll(adverts: List[Advert]): ZIO[Any, Throwable, List[Certificate]]
}

object ZCertificateStore {
  def getAll(adverts: List[Advert]): ZIO[ZCertificateStore, Throwable, List[Certificate]] =
    ZIO.serviceWithZIO[ZCertificateStore](_.getAll(adverts))
}

class ZCertificateStoreImpl(
    certificateStore: CertificateStore
) extends ZCertificateStore {
  private def getAllByEircode(adverts: List[Advert]): ZIO[Any, Throwable, List[Certificate]] =
    adverts
      .flatMap { _.propertyEircode }
      .map { _.value }
      .distinct
      .map { Eircode.fromString }
      .map { certificateStore.getAllByEircode }
      .pipe { ZIO.collectAllPar }
      .map { _.flatten }

  private def getAllByCertificateNumber(adverts: List[Advert]): ZIO[Any, Throwable, List[Certificate]] =
    adverts
      .flatMap { _.propertyBuildingEnergyRatingCertificateNumber }
      .distinct
      .map { CertificateNumber(_) }
      .map { certificateStore.getByNumber }
      .pipe { ZIO.collectAllPar }
      .map { _.flatten }

  def getAll(adverts: List[Advert]): ZIO[Any, Throwable, List[Certificate]] =
    getAllByEircode(adverts)
      .zipPar(getAllByCertificateNumber(adverts))
      .map { case (byEircode, byCertificateNumber) =>
        (byEircode ++ byCertificateNumber).distinct
          .tap {
            case certificates if certificates.size > 1 =>
              println(s"""
              | More than one certificate found for adverts:
              | - ${AdvertUrl.fromAdvert(adverts.head)}
              | - ${certificates.map { _.toJson }.mkString("\n - ")}
              """.stripMargin.trim)
            case _ => ()
          }
      }
}

object ZCertificateStoreImpl {
  val layer: ZLayer[CertificateStore, Throwable, ZCertificateStore] = ZLayer.fromFunction { ZCertificateStoreImpl(_) }
}
