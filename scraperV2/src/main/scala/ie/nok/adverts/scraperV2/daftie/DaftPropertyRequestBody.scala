package ie.nok.adverts.scraperV2.daftie

import zio.json.*

case class NameValuePair(name: String, values: List[String])
case class Paging(from: Int, pageSize: Int)

case class DaftPropertyRequestBody(
    section: String,
    filters: List[NameValuePair],
    paging: Paging
)

object DaftPropertyRequestBody {
  import DaftPropertyRequestDecoders.given_JsonEncoder_DaftPropertyRequestBody

  private val published              = NameValuePair(name = "adState", values = List("published"))
  private val sectionForSale         = "residential-for-sale"
  private def agentIds(agentId: Int) = NameValuePair(name = "agentIds", values = List(agentId.toString))

  private def paginated(from: Int, pageSize: Int): DaftPropertyRequestBody =
    DaftPropertyRequestBody(
      section = sectionForSale,
      filters = List(published),
      paging = Paging(from, pageSize)
    )

  private def paginatedWithAgent(from: Int, pageSize: Int, agentId: Int) =
    DaftPropertyRequestBody(
      section = sectionForSale,
      filters = List(published, agentIds(agentId)),
      paging = Paging(from, pageSize)
    )
  def paginatedStr(from: Int, pageSize: Int): String = paginated(from, pageSize).toJson

  def paginatedWithAgentStr(from: Int, pageSize: Int, agentId: Int): String = paginatedWithAgent(from, pageSize, agentId).toJson
}

object DaftPropertyRequestDecoders {

  protected[daftie] given JsonEncoder[NameValuePair]           = DeriveJsonEncoder.gen[NameValuePair]
  protected[daftie] given JsonEncoder[Paging]                  = DeriveJsonEncoder.gen[Paging]
  protected[daftie] given JsonEncoder[DaftPropertyRequestBody] = DeriveJsonEncoder.gen[DaftPropertyRequestBody]
}
