package com.github.xelamanster.csvanalyzer

import com.github.xelamanster.csvanalyzer.components.Setup
import com.github.xelamanster.csvanalyzer.utils.TcpUtils

import akka.Done
import akka.event.Logging
import akka.stream.Attributes
import akka.stream.scaladsl.Source

import scala.concurrent.Future

import TcpStreamAnalyzerApp._

object TcpStreamAnalyzerApp {
  private final val NewConnectionLogMessage = "New connection"
}

class TcpStreamAnalyzerApp[T, R](analyzer: StreamAnalyzer[T, R, _]) {

  def run(setup: Setup): Future[Done] = {
    import setup._

    TcpUtils.bindConnections(port)
      .log(NewConnectionLogMessage)
      .withAttributes(Attributes
        .logLevels(onElement = Logging.WarningLevel))
      .runForeach { connection =>
        connection.flow
          .to(analyzer.flow(batchSize))
          .runWith(Source.maybe)
      }
  }
}
