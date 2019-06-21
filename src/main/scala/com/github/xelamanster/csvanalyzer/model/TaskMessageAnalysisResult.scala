package com.github.xelamanster.csvanalyzer.model

import com.github.xelamanster.csvanalyzer.utils.csv.{Encoder, Format}
import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._

object TaskMessageAnalysisResult {

  object implicits {

    implicit val taskMessageAnalysisResultEncoder: Encoder[TaskMessageAnalysisResult] =
      (v: TaskMessageAnalysisResult) => {

        val usersAnalysis = v.usersAnalysis
          .map(_.encodeString())
          .mkString(System.lineSeparator())

        s"""${v.integer2Sum}
           |${v.uniqueUsers}
           |${usersAnalysis}""".stripMargin
      }

    implicit val userAnalysisResultEncoder: Encoder[UserAnalysisResult] =
      (v: UserAnalysisResult) =>
        Seq(v.id.id, v.averageFloatingValue, v.recentInteger1)
          .mkString(Format.DefaultDelimiter)
  }
}

case class TaskMessageAnalysisResult(
    integer2Sum: BigInt,
    uniqueUsers: Int,
    usersAnalysis: Seq[UserAnalysisResult])

case class UserAnalysisResult(id: UserId, averageFloatingValue: BigDecimal, recentInteger1: Long)
