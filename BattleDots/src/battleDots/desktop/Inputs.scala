package battleDots.desktop

import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import battleDots.desktop.Gui.socket
import play.api.libs.json.Json

class Inputs extends EventHandler[KeyEvent] {

  var keyPressed: Map[String, Boolean] = Map(
    "w" -> false,
    "a" -> false,
    "s" -> false,
    "d" -> false,
    "p" -> false
  )

  val pressed: Boolean = true
  val released: Boolean = false

  override def handle(event: KeyEvent): Unit = {
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
    }

  }


  def setState(key: String, value: Boolean) {
    if (this.keyPressed(key) != value) {
      this.keyPressed(key) = value
      socket.emit("inputs", Json.toJson(keyPressed))
    }
  }

}
