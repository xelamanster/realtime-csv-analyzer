package com.github.xelamanster.csvanalyzer

import com.github.xelamanster.csvanalyzer.components.{AnalysisReportSink, Setup, TaskMessageAnalyzer}
import com.github.xelamanster.csvanalyzer.model.TaskMessage.implicits._
import com.github.xelamanster.csvanalyzer.model.TaskMessageAnalysisReport.implicits._
import com.github.xelamanster.csvanalyzer.model.TaskMessageAnalysisReport
import com.github.xelamanster.csvanalyzer.utils.FileUtils

object Runner extends App {
  val setup = new Setup()

  import setup._

  val outputFolder = FileUtils.createFolderIfAbsent(Setup.DefaultOutputFolder)

  val sink =
    AnalysisReportSink
      .writeEachToTimestampedFile[TaskMessageAnalysisReport](outputFolder)

  val streamAnalyzer =
    new StreamAnalyzer(new TaskMessageAnalyzer, sink)

  new TcpStreamAnalyzerApp(streamAnalyzer).run(setup)
}