import sbt._

object Dependencies {
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.23"
  lazy val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

  lazy val dependencies = Seq(
    akkaStream
  )

  lazy val testDependencies = Seq(
    scalaTest,
    akkaStreamTestkit
  )
}
