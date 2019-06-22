package com.github.xelamanster.csvanalyzer.model

import com.github.xelamanster.csvanalyzer.utils.csv.{Encoder, Format}
import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._

object TaskMessageAnalysisReport {

  object implicits {

    implicit val taskMessageAnalysisReportEncoder: Encoder[TaskMessageAnalysisReport] =
      (v: TaskMessageAnalysisReport) => {

        val usersAnalysis = v.usersAnalysis
          .map(_.encodeString())
          .mkString(System.lineSeparator())

        s"""${v.integer2Sum}
           |${v.uniqueUsers}
           |${usersAnalysis}""".stripMargin
      }

    implicit val userAnalysisReportEncoder: Encoder[UserAnalysisReport] =
      (v: UserAnalysisReport) =>
        Seq(v.id.id, v.averageFloatingValue, v.recentInteger1)
          .mkString(Format.DefaultDelimiter)
  }
}

case class TaskMessageAnalysisReport(
    integer2Sum: BigInt,
    uniqueUsers: Int,
    usersAnalysis: Seq[UserAnalysisReport])

case class UserAnalysisReport(id: UserId, averageFloatingValue: BigDecimal, recentInteger1: Long)
