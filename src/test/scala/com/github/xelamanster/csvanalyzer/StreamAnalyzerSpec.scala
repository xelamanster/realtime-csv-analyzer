package com.github.xelamanster.csvanalyzer

import com.github.xelamanster.csvanalyzer.components.TaskMessageAnalyzer
import com.github.xelamanster.csvanalyzer.model.{TaskMessageAnalysisReport, UserAnalysisReport, UserId}
import com.github.xelamanster.csvanalyzer.model.TaskMessage.implicits._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestKit
import akka.util.ByteString
import org.scalatest._

class StreamAnalyzerSpec extends TestKit(ActorSystem("StreamAnalyzerSpec")) with AsyncWordSpecLike with Matchers {

  private implicit val materializer = ActorMaterializer.create(system)

  "CsvStreamAnalyzer" should {

    "Analyze incoming messages and publish simple report" in {
      test(
        batchSize = 3,

        source =
          """0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998""".stripMargin,

        expectedReports =
          Seq(
            TaskMessageAnalysisReport(
              BigInt("15084455767448815815"),
              2,
              List(
                UserAnalysisReport(
                  UserId("0977dca4-9906-3171-bcec-87ec0df9d745"),
                  0.18715484122922377,
                  982761284),
                UserAnalysisReport(
                  UserId("5fac6dc8-ea26-3762-8575-f279fe5e5f51"),
                  0.7626710614484215,
                  1005421520)
              )
            )
          )
      )
    }

    "Analyze incoming messages and publish few reports" in {
      test(
        batchSize = 1,

        source =
          """0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998""".stripMargin,

        expectedReports =
          Seq(
            TaskMessageAnalysisReport(
              BigInt("8442009284719321817"),
              1,
              List(
                UserAnalysisReport(
                  UserId("0977dca4-9906-3171-bcec-87ec0df9d745"),
                  0.18715484122922377,
                  982761284)
              )
            ),
            TaskMessageAnalysisReport(
              BigInt("6642446482729493998"),
              1,
              List(
                UserAnalysisReport(
                  UserId("5fac6dc8-ea26-3762-8575-f279fe5e5f51"),
                  0.7626710614484215,
                  1005421520)
              )
            )
          )
      )
    }

    "Analyze incoming messages and publish few reports of different sized batches" in {
      test(
        batchSize = 2,

        source =
          """0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1045661520,3455345345
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998""".stripMargin,

        expectedReports =
          Seq(
            TaskMessageAnalysisReport(
              BigInt("8442009288174667162"),
              2,
              List(
                UserAnalysisReport(
                  UserId("0977dca4-9906-3171-bcec-87ec0df9d745"),
                  0.18715484122922377,
                  982761284),
                UserAnalysisReport(
                  UserId("5fac6dc8-ea26-3762-8575-f279fe5e5f51"),
                  0.7626710614484215,
                  1045661520)
              )),
            TaskMessageAnalysisReport(
              BigInt("6642446482729493998"),
              1,
              List(
                UserAnalysisReport(
                  UserId("5fac6dc8-ea26-3762-8575-f279fe5e5f51"),
                  0.7626710614484215,
                  1005421520)
              )
            )
          )
      )
    }

    "Combine statistic for user" in {
      test(
        batchSize = 3,

        source =
          """0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1045661520,3455345345
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998""".stripMargin,

        expectedReports =
          Seq(
            TaskMessageAnalysisReport(
              BigInt("15084455770904161160"),
              2,
              List(
                UserAnalysisReport(
                  UserId("0977dca4-9906-3171-bcec-87ec0df9d745"),
                  0.18715484122922377,
                  982761284),
                UserAnalysisReport(
                  UserId("5fac6dc8-ea26-3762-8575-f279fe5e5f51"),
                  0.7626710614484215,
                  1005421520)
              ))
          )
      )
    }

    "Throw exception if source has invalid format" in {
      runFlow(
        batchSize = 3,

        source =
          """0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,fssf,8442009284719321817
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1045661520,3455345345
            |5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998""".stripMargin,
      ).failed
        .map { e =>
          e shouldBe a[IllegalArgumentException]
          e.getMessage should be("For input string: \"fssf\"")
      }
    }
  }

  private def test(batchSize: Int, source: String, expectedReports: Seq[TaskMessageAnalysisReport]) =
    runFlow(batchSize, source)
      .map(_ should contain theSameElementsAs expectedReports)

  private def runFlow(batchSize: Int, source: String) = {
    val streamAnalyzer =
      new StreamAnalyzer(new TaskMessageAnalyzer, Sink.seq[TaskMessageAnalysisReport])

    Source.single(ByteString(source))
      .runWith(streamAnalyzer.flow(batchSize))
  }
}
