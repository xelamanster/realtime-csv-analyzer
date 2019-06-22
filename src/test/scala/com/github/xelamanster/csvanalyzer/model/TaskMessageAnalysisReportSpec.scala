package com.github.xelamanster.csvanalyzer.model

import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._
import com.github.xelamanster.csvanalyzer.model.TaskMessageAnalysisReport.implicits._

import akka.util.ByteString
import org.scalatest._
import java.util.UUID

class TaskMessageAnalysisReportSpec extends WordSpec with Matchers {

  "TaskMessageAnalysisReportEncoder" should {

    "encode analysis report" in {
      val analysisReport =
        TaskMessageAnalysisReport(
          24425,
          1,
          Seq(
            UserAnalysisReport(
              UserId(UUID.fromString("b0c09889-1899-4b41-80cf-d01b32ab3222")),
              2.34,
              24425),
          )
        )

      val expectedEncodedStringValue =
        """24425
          |1
          |b0c09889-1899-4b41-80cf-d01b32ab3222,2.34,24425""".stripMargin

      analysisReport.encodeString() should be(expectedEncodedStringValue)
      analysisReport.encode() should be(ByteString(expectedEncodedStringValue))
    }

  }

}
