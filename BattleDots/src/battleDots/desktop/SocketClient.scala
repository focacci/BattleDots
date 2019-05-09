package battleDots.desktop

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.{IO, Tcp}
import akka.io.Tcp._


class SocketClient(remote: InetSocketAddress) extends Actor {

  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)
  var server: ActorRef = _

  override def receive: Receive = {
    case c: Connected =>
  }



}




object SocketClient {

}
