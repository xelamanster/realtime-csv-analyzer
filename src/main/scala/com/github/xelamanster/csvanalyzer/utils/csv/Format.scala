package com.github.xelamanster.csvanalyzer.utils.csv

import akka.util.ByteString

object Format {

  final val DefaultDelimiter = ","

  object syntax {

    implicit class DecoderOps[T : Decoder](val raw: ByteString) {

      def decode(): T =
        implicitly[Decoder[T]].decode(raw)
    }

    implicit class EncoderOps[T : Encoder](val v: T) {

      def encode(): ByteString =
        implicitly[Encoder[T]].encode(v)

      def encodeString(): String =
        implicitly[Encoder[T]].encodeString(v)
    }
  }
}

trait Encoder[T] {

  def encode(v: T): ByteString = ByteString(encodeString(v))

  def encodeString(v: T): String
}

trait Decoder[T] {

  def decode(raw: ByteString): T = decodeString(raw.utf8String)

  protected def decodeString(raw: String): T
}
