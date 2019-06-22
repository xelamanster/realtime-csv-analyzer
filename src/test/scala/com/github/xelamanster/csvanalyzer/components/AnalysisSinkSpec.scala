package com.github.xelamanster.csvanalyzer.components

import com.github.xelamanster.csvanalyzer.utils.TestTimer
import com.github.xelamanster.csvanalyzer.utils.csv.Encoder

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.testkit.TestKit
import java.nio.file.{Path, Paths}

import org.scalatest._

import scala.util.Using

class AnalysisSinkSpec extends TestKit(ActorSystem("SimpleStreaming"))  with AsyncWordSpecLike with Matchers {
  val time = 123
  implicit val timer = new TestTimer(time)
  implicit val materializer = ActorMaterializer.create(system)
  implicit val ec = system.dispatcher

  implicit val stringEncoder = new Encoder[String] {
    override def encodeString(v: String): String = v
  }

  private val defaultValue = "testValue"
  private val defaultFolder = Paths.get("testOutput")
  private val expectedFile = defaultFolder.resolve(s"$time${AnalysisResultSink.DefaultFileNameExtension}")

  "AnalysisSink" should {

    "write file to correct location" in defaultWrite.map { _ =>
      val fileExist = expectedFile.toFile.exists()

      remove(expectedFile, defaultFolder)

      fileExist should be(true)
    }

    "write correct content to the file" in defaultWrite.map { _ =>
      val contentReadAttempt =
        Using.resource(io.Source.fromFile(expectedFile.toFile))(_.mkString)

      remove(expectedFile, defaultFolder)

      contentReadAttempt should be(defaultValue)
    }
  }


  private def defaultWrite = {
    prepareFolder(defaultFolder)
    Source
      .single(defaultValue)
      .runWith(AnalysisResultSink.writeEachToTimestampedFile(defaultFolder))
  }

  private def prepareFolder(folder: Path): Unit = {
    val folderPath = folder.toFile

    if(!folderPath.exists()) {
      folderPath.mkdir()
    }
  }

  private def remove(paths: Path*): Unit =
    paths.foreach(_.toFile.delete())

}
