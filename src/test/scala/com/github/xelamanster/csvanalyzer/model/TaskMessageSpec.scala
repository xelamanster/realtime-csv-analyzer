package com.github.xelamanster.csvanalyzer.model

import com.github.xelamanster.csvanalyzer.utils.csv.Format.syntax._
import com.github.xelamanster.csvanalyzer.model.TaskMessage.implicits._

import java.util.UUID
import akka.util.ByteString
import org.scalatest._

class TaskMessageSpec extends WordSpec with Matchers {

  "TaskMessageDecoder" should {

    "decode task message" in {
      val message =
        "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"

      val expectedDecodedMessage =
        TaskMessage(
          UserId("0977dca4-9906-3171-bcec-87ec0df9d745"),
          "kFFzW4O8gXURgP8ShsZ0gcnNT5E=",
          0.18715484122922377,
          982761284L,
          8442009284719321817L)

      ByteString(message).decode() should be(expectedDecodedMessage)
    }

    "throw exception content has unexpected format" in {
      val message =
        "0977dca4-9906-3171-bcec,0.18715484122922377,982761284,8442009284719321817"

      the [IllegalArgumentException] thrownBy
        ByteString(message).decode() should have message "0977dca4-9906-3171-bcec,0.18715484122922377,982761284,8442009284719321817"
    }

    "throw exception if UUID is invalid" in {
      val message =
        "0977dca4-9906-3171-bcec,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"

      the [IllegalArgumentException] thrownBy
        ByteString(message).decode() should have message "Invalid UUID string: 0977dca4-9906-3171-bcec"
    }

    "throw exception if float value is invalid" in {
      val message =
        s"0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,fsdf,982761284,8442009284719321817"

      the [NumberFormatException] thrownBy
        ByteString(message).decode() should have message "For input string: \"fsdf\""
    }

    "throw exception if integer 1 value is invalid" in {
      val message =
        "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,sdf,8442009284719321817"

      the [NumberFormatException] thrownBy
        ByteString(message).decode() should have message "For input string: \"sdf\""
    }

    "throw exception if integer 2 value is invalid" in {
      val message =
        "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,fgfd"

      the [NumberFormatException] thrownBy
        ByteString(message).decode() should have message "For input string: \"fgfd\""
    }
  }
}
