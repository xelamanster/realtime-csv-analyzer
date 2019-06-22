package com.github.xelamanster.csvanalyzer.model

import java.util.UUID

import com.github.xelamanster.csvanalyzer.utils.csv.{Decoder, Format}

object TaskMessage {

  object implicits {

    implicit val taskMessageDecoder: Decoder[TaskMessage] =
      (raw: String) => raw.split(Format.DefaultDelimiter).toList match {

        case rawUserId :: encodedData :: rawFloatingValue :: rawInteger1 :: rawInteger2 :: Nil =>

          val userId = UserId(rawUserId)
          val floatingValue = rawFloatingValue.toDouble
          val integer1 = rawInteger1.toLong
          val integer2 = rawInteger2.toLong

          TaskMessage(userId, encodedData, floatingValue, integer1, integer2)

        case _ => throw new IllegalArgumentException(raw)
      }
  }

}

case class TaskMessage(
    userId: UserId,
    encodedData: String,
    floatingValue: Double,
    integer1: Long,
    integer2: Long)

object UserId {
  def apply(id: String): UserId = new UserId(UUID.fromString(id))
}

case class UserId(id: UUID) extends AnyVal
