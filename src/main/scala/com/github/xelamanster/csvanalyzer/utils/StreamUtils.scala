package com.github.xelamanster.csvanalyzer.utils

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Framing}
import akka.util.ByteString

object StreamUtils {
  private final val DefaultLineDelimiter = System.lineSeparator()
  private final val DefaultLineMaxLength = 1024

  def splitLines(delimiter: String = DefaultLineDelimiter,
                 maxLength: Int = DefaultLineMaxLength): Flow[ByteString, ByteString, NotUsed] = {

    Framing.delimiter(
      ByteString(delimiter),
      maximumFrameLength = maxLength,
      allowTruncation = true)
  }

}
