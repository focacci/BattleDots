package battleDots.desktop

import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import javafx.application.Platform
import javafx.scene.input.KeyEvent
import play.api.libs.json.{JsValue, Json}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.{Group, Scene}


class HandleMessagesFromPython2 extends Emitter.Listener {
  override def call(objects: Object*): Unit = {
    // Use runLater when interacting with the GUI
    Platform.runLater(() => {
      val jsonGameState = objects.apply(0).toString
      val gameState: JsValue = Json.parse(jsonGameState)
      val gridDimensions: Map[String, Int] = (gameState \ "gridDimensions").as[Map[String, Int]]
      var width = gridDimensions("width")
      var height = gridDimensions("height")
      val players = (gameState \ "players").as[List[Map[String, JsValue]]]
      val bullets = (gameState \ "bullets").as[List[Map[String, Double]]]


      Gui2.clearRect()

      for (i <- players) {
        val x: Double = i("location")("x").as[Double]
        val y: Double = i("location")("y").as[Double]
        val username: String = i("username").as[String]
        var color: String = "#0ec51f"
        if (username != Gui2.socket.id()) {
          color = "#ce000d"
        }
        Gui2.drawPlayer(x, y, 10, color)
      }

      for (bullet <- bullets) {
        val x: Double = bullet("x")
        val y: Double = bullet("y")
        Gui2.drawBullet(x, y, 3, "#000000")
      }


    })
  }
}


object Gui2 extends JFXApp{

  /*
    var players: List[Map[String, JsValue]] = List()
    var width: Int = 500
    var height: Int = 300
    */


  var socket: Socket = IO.socket("http://localhost:1234/")
  socket.on("gameState", new HandleMessagesFromPython2)
  socket.connect()


  var keyPressed: Map[String, Boolean] = Map(
    "w" -> false,
    "a" -> false,
    "s" -> false,
    "d" -> false,
    "p" -> false
  )

  val pressed: Boolean = true
  val released: Boolean = false

  var sceneGraphics: Group = new Group {}

  def drawPlayer(x: Double, y: Double, size: Int, color: String): Unit = {
    val newPlayer: Circle = new Circle {
      centerX = x
      centerY = y
      radius = size
      fill = Color.web(color)
    }
    Gui2.sceneGraphics.children.add(newPlayer)
  }

  def drawBullet(x: Double, y: Double, size: Int, color: String): Unit = {
    val newBullet: Circle = new Circle {
      centerX = x
      centerY = y
      radius = size
      fill = Color.web(color)
    }
    Gui2.sceneGraphics.children.add(newBullet)
  }

  def clearRect(): Unit = {
    val rect: Rectangle = new Rectangle() {
      width = 500
      height = 300
      translateX = 0
      translateY = 0
      fill = Color.White
    }
    Gui2.sceneGraphics.children.add(rect)
  }



  def keyStates(event: KeyEvent): Unit = {
    val key = event.getCode
    event.getEventType.getName match {
      case "KEY_PRESSED" => key.getName match {
        case "W" => setState("w", pressed)
        case "A" => setState("a", pressed)
        case "S" => setState("s", pressed)
        case "D" => setState("d", pressed)
        case "P" => setState("p", pressed)
        case _ =>
      }

      case "KEY_RELEASED" => key.getName match {
        case "W" => setState("w", released)
        case "A" => setState("a", released)
        case "S" => setState("s", released)
        case "D" => setState("d", released)
        case "P" => setState("p", released)
        case _ =>
      }
      case _ =>
    }
  }

  def setState(key: String, value: Boolean): Unit = {
    if (keyPressed(key) != value) {
      keyPressed += (key -> value)
      socket.emit("inputs", Json.toJson(keyPressed))
      //socket.emit()
    }
  }


  this.stage = new PrimaryStage {
    this.title = "BattleDots"
    scene = new Scene(500.0, 300.0) {
      content = List(sceneGraphics)
      addEventHandler(KeyEvent.ANY, (event: KeyEvent) => keyStates(event))

    }
  }

}
