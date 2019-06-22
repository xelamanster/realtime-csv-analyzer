package com.github.xelamanster.csvanalyzer.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestKit
import akka.util.ByteString
import org.scalatest._

class StreamUtilsSpec extends TestKit(ActorSystem("StreamUtilsSpec")) with AsyncWordSpecLike with Matchers {

  private implicit val materializer = ActorMaterializer.create(system)

  "StreamUtils" should {

    "split source by lines" in {
      val ls = System.lineSeparator()
      val source = s"dfkjsfdj${ls}nfdssdf${ls}nsdfsdfsdf${ls}nfsdsdf${ls}nsdfsfd"

      val expected = Seq(
        "dfkjsfdj",
        "nfdssdf",
        "nsdfsdfsdf",
        "nfsdsdf",
        "nsdfsfd"
      )

      Source.single(ByteString(source))
        .via(StreamUtils.splitLines())
        .runWith(Sink.seq)
        .map(_.map(_.utf8String))
        .map { seq =>
          seq should contain theSameElementsAs expected
        }
    }
  }
}
