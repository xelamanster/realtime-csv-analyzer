package com.github.xelamanster.csvanalyzer.components

import java.nio.file.Path

import com.github.xelamanster.csvanalyzer.utils.Timer
import com.github.xelamanster.csvanalyzer.utils.csv.Encoder
import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._

import akka.Done
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Sink, Source}

import scala.concurrent.Future

object AnalysisResultSink {

  val DefaultFileNameExtension = ".txt"

  def writeEachToTimestampedFile[T : Encoder](folder: Path)
                                             (implicit materializer: Materializer, timer: Timer): Sink[T, Future[Done]]=
  Sink.foreach { v =>
    Source.single(v)
      .map(_.encode())
      .runWith(toTimestampedFile(folder, timer))
  }
  private def toTimestampedFile(folder: Path, timer: Timer) =
    FileIO.toPath(folder.resolve(timestampedFilename(timer)))

  private def timestampedFilename(timer: Timer) =
    s"${timer.currentTimeMillis()}$DefaultFileNameExtension"
}
