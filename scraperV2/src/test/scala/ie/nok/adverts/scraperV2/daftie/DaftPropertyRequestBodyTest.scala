package ie.nok.adverts.scraperV2.daftie
import munit.FunSuite

class DaftPropertyRequestBodyTest extends FunSuite {

  test("DaftPropertyRequestBody paginated encode to json") {
    val actual   = DaftPropertyRequestBody.paginatedStr(0, 20)
    val expected = """{"section":"residential-for-sale","filters":[{"name":"adState","values":["published"]}],"paging":{"from":0,"pageSize":20}}"""
    assertEquals(actual, expected)
  }

  test("DaftPropertyRequestBody paginatedWithAgent encode to json") {
    val actual = DaftPropertyRequestBody.paginatedWithAgentStr(0, 20, 12345)
    val expected =
      """{"section":"residential-for-sale","filters":[{"name":"adState","values":["published"]},{"name":"agentIds","values":["12345"]}],"paging":{"from":0,"pageSize":20}}"""
    assertEquals(actual, expected)
  }
}
