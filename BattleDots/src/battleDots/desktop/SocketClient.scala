package battleDots.desktop

import akka.actor.Actor
import akka.io.Tcp._


class SocketClient extends Actor {


  override def receive: Receive = {
    case c: Connected =>
  }



}




object SocketClient {

}
