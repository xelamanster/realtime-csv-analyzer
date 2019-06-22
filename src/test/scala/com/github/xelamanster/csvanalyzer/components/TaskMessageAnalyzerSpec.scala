package com.github.xelamanster.csvanalyzer.components

import java.util.UUID

import com.github.xelamanster.csvanalyzer.model.{TaskMessage, TaskMessageAnalysisReport, UserAnalysisReport, UserId}
import org.scalatest._

class TaskMessageAnalyzerSpec extends WordSpec with Matchers {

  "TaskMessageAnalyzer" should {

    "create Analysis for single message" in {
      val id = userId(0)

      val messages = Seq(
        TaskMessage(id, "data", 1.23, 111, 222)
      )

      val expected =
        TaskMessageAnalysisReport(
          222,
          1,
          Seq(
            UserAnalysisReport(id, 1.23, 111)
          )
        )

      new TaskMessageAnalyzer().analyze(messages) should be(expected)
    }

    "create Analysis for messages from different users" in {
      val messages = Seq(
        TaskMessage(userId(0), "data", 1.23, 111, 112),
        TaskMessage(userId(1), "data", 2.23, 211, 222),
        TaskMessage(userId(2), "data", 3.23, 311, 333)
      )

      val expected =
        TaskMessageAnalysisReport(
          667,
          3,
          Seq(
            UserAnalysisReport(userId(1), 2.23, 211),
            UserAnalysisReport(userId(2), 3.23, 311),
            UserAnalysisReport(userId(0), 1.23, 111),
          )
        )

      new TaskMessageAnalyzer().analyze(messages) should be(expected)
    }

    "create Analysis for messages from single users" in {
      val messages = Seq(
        TaskMessage(userId(0), "data", 1.23, 111, 112),
        TaskMessage(userId(0), "data", 2.54, 211, 222),
        TaskMessage(userId(0), "data", 3.25, 311, 333)
      )

      val expected =
        TaskMessageAnalysisReport(
          667,
          1,
          Seq(
            UserAnalysisReport(userId(0), 2.34, 311),
          )
        )

      new TaskMessageAnalyzer().analyze(messages) should be(expected)
    }

    "create Analysis for messages that contains edge integer values" in {
      val messages = Seq(
        TaskMessage(userId(0), "data", 1.23, Long.MaxValue, Long.MaxValue),
        TaskMessage(userId(0), "data", 2.54, Long.MaxValue, Long.MaxValue),
        TaskMessage(userId(0), "data", 3.25, Long.MaxValue, Long.MaxValue)
      )

      val expected =
        TaskMessageAnalysisReport(
          BigInt(Long.MaxValue) * 3,
          1,
          Seq(
            UserAnalysisReport(userId(0), 2.34, Long.MaxValue),
          )
        )

      new TaskMessageAnalyzer().analyze(messages) should be(expected)
    }

    "create Analysis for messages that contains edge floating values" in {
      val messages = Seq(
        TaskMessage(userId(0), "data", Double.MaxValue, 111, 112),
        TaskMessage(userId(0), "data", Double.MaxValue, 211, 222),
        TaskMessage(userId(0), "data", Double.MaxValue, 311, 333)
      )

      val expected =
        TaskMessageAnalysisReport(
          667,
          1,
          Seq(
            UserAnalysisReport(userId(0), Double.MaxValue, 311),
          )
        )

      new TaskMessageAnalyzer().analyze(messages) should be(expected)
    }
  }

  private def userId(id: Int) = {
    val defaultValue = "b0c09889-1899-4b41-80cf-d01b32ab3222"
    val idString = id.toString

    val value = defaultValue.substring(0, defaultValue.length - idString.length) + idString

    UserId(UUID.fromString(value))
  }

}
