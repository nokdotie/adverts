package ie.nok.adverts.utils.zio

import java.io.{File => JavaFile}
import _root_.zio.ZIO
import _root_.zio.json.{writeJsonLinesAs, JsonEncoder}
import _root_.zio.stream.ZStream

object File {

  def createTempJsonLinesFile[R, A: JsonEncoder](
      stream: ZStream[R, Throwable, A]
  ): ZIO[R, Throwable, JavaFile] = {
    val file = JavaFile.createTempFile("tmp", null)
    writeJsonLinesAs[R, A](file, stream).map { _ => file }
  }

}
