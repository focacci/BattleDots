package battleDots.model

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import battleDots.model.physics.PhysicsVector
import play.api.libs.json.{JsValue, Json}



class TcpServer(gameActor: ActorRef) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 4242))

  var webServers: Set[ActorRef] = Set()
  var buffer: String = ""
  val delimiter: String = "~"

  override def receive: Receive = {
    case b: Bound =>
      println("Listening on port: " + b.localAddress.getPort)

    case c: Connected =>
      println("Client Connected: " + c.remoteAddress)
      this.webServers = this.webServers + sender()
      sender() ! Register(self)

    case PeerClosed =>
      println("Client Disconnected: " + sender())
      this.webServers = this.webServers - sender()

    case r: Received =>
      buffer += r.data.utf8String
      println(r.data.utf8String)
      while (buffer.contains(delimiter)) {
        val curr = buffer.substring(0, buffer.indexOf(delimiter))
        buffer = buffer.substring(buffer.indexOf(delimiter) + 1)
        handleMessageFromWebServer(curr)
      }

    case SendGameState =>
      gameActor ! SendGameState

    case gs: GameState =>
      this.webServers.foreach((client: ActorRef) => client ! Write(ByteString(gs.state + delimiter)))
  }


  def handleMessageFromWebServer(messageString:String): Unit = {
    val message: JsValue = Json.parse(messageString)
    val username = (message \ "username").as[String]
    val messageType = (message \ "action").as[String]

    messageType match {
      case "connected" => gameActor ! AddPlayer(username)
      case "disconnected" => gameActor ! RemovePlayer(username)
      case "move" =>
        val x = (message \ "x").as[Double]
        val y = (message \ "y").as[Double]
        gameActor ! MovePlayer(username, x, y)
        val fire = (message \ "fire").as[Boolean]
        if (fire) {
          gameActor ! Fire(username)
        }
      case "stop" => gameActor ! StopPlayer(username)
    }

  }

}


object TcpServer {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()

    import actorSystem.dispatcher
    import scala.concurrent.duration._

    val gameActor = actorSystem.actorOf(Props(classOf[GameActor]))
    val server = actorSystem.actorOf(Props(classOf[TcpServer], gameActor))

    actorSystem.scheduler.schedule(16.milliseconds, 32.milliseconds, gameActor, Update)
    actorSystem.scheduler.schedule(32.milliseconds, 32.milliseconds, server, SendGameState)
  }

}
