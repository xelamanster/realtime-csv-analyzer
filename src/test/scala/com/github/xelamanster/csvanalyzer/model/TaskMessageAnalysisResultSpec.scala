package com.github.xelamanster.csvanalyzer.model

import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._
import com.github.xelamanster.csvanalyzer.model.TaskMessageAnalysisResult.implicits._

import akka.util.ByteString
import org.scalatest._
import java.util.UUID

class TaskMessageAnalysisResultSpec extends WordSpec with Matchers {

  "TaskMessageAnalysisResultEncoder" should {

    "encode analysis result" in {
      val analysisResult =
        TaskMessageAnalysisResult(
          24425,
          1,
          Seq(
            UserAnalysisResult(
              UserId(UUID.fromString("b0c09889-1899-4b41-80cf-d01b32ab3222")),
              2.34,
              24425),
          )
        )

      val expectedEncodedStringValue =
        """24425
          |1
          |b0c09889-1899-4b41-80cf-d01b32ab3222,2.34,24425""".stripMargin

      analysisResult.encodeString() should be(expectedEncodedStringValue)
      analysisResult.encode() should be(ByteString(expectedEncodedStringValue))
    }

  }

}
