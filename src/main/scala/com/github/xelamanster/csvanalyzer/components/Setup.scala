package com.github.xelamanster.csvanalyzer.components

import com.github.xelamanster.csvanalyzer.utils.Timer

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import java.nio.file.Paths

import Setup._

object Setup {
  final val DefaultPort = 9000
  final val DefaultBatchSize = 1000
  final val DefaultOutputFolder = Paths.get("metrics")
}

class Setup(val port: Int = DefaultPort, val batchSize: Int = DefaultBatchSize) {
  implicit val system: ActorSystem = ActorSystem()

  private val logErrorsDecider: Supervision.Decider = { e =>
    system.log.error("Unhandled exception in stream", e.getMessage)
    Supervision.Stop
  }

  private val materializerSettings =
    ActorMaterializerSettings(system).withSupervisionStrategy(logErrorsDecider)

  implicit val materializer: ActorMaterializer =
    ActorMaterializer(materializerSettings)(system)

  implicit val timer: Timer = new Timer()

}
