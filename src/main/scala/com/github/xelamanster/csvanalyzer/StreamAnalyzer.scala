package com.github.xelamanster.csvanalyzer

import com.github.xelamanster.csvanalyzer.components.Analyzer
import com.github.xelamanster.csvanalyzer.utils.StreamUtils
import com.github.xelamanster.csvanalyzer.utils.csv.Decoder
import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._

import akka.stream.scaladsl.{Flow, Keep, Sink}
import akka.util.ByteString

class StreamAnalyzer[T : Decoder, R, S](
    analyzer: Analyzer[T, R],
    sink: Sink[R, S]) {

  def flow(batchSize: Int): Sink[ByteString, S] = {
    val batch = Flow[T].sliding(batchSize, batchSize)

    Flow[ByteString]
      .via(StreamUtils.splitLines())
      .map(decode)
      .via(batch)
      .map(analyze)
      .toMat(sink)(Keep.right)
  }

  private def decode(raw: ByteString): T = raw.decode()

  private def analyze(batch: Seq[T]): R = analyzer.analyze(batch)

}
