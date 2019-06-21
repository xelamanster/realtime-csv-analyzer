package com.github.xelamanster.csvanalyzer.components

import com.github.xelamanster.csvanalyzer.model.{TaskMessage, TaskMessageAnalysisResult, UserAnalysisResult, UserId}

trait Analyzer[T, R] {
  def analyze(batch: Seq[T]): R
}

class TaskMessageAnalyzer extends Analyzer[TaskMessage, TaskMessageAnalysisResult] {

  override def analyze(messages: Seq[TaskMessage]): TaskMessageAnalysisResult = {

    val integer2Sum =
      messages
        .map(_.integer2)
        .foldLeft(BigInt(0))(_ + _)

    val messagesByUser =
      messages.groupBy(_.userId)

    val uniqueUsers =
      messagesByUser.keys.size

    val usersAnalysis =
      messagesByUser.toSeq
        .map((analyzeUser _).tupled)

    TaskMessageAnalysisResult(integer2Sum, uniqueUsers, usersAnalysis)
  }

  private def analyzeUser(id: UserId, userMessages: Seq[TaskMessage]) = {

    val averageFloatingValue: BigDecimal =
      userMessages.map(_.floatingValue).foldLeft(BigDecimal(0))(_ + _) / userMessages.size

    val recentInteger1 = userMessages.last.integer1

    UserAnalysisResult(id, averageFloatingValue, recentInteger1)
  }

}
