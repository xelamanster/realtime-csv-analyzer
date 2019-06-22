package com.github.xelamanster.csvanalyzer.utils

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Source, Tcp}

import scala.concurrent.Future

object TcpUtils {
  private final val Localhost = "0.0.0.0"

  def bindConnections(port: Int)(implicit system: ActorSystem): Source[Tcp.IncomingConnection, Future[Tcp.ServerBinding]] =
    Tcp().bind(Localhost, port)
}
