package battleDots.model


import akka.actor.{Actor, ActorRef}
import battleDots.model.game_objects.Bullet
import battleDots.model.physics.PhysicsVector



class GameActor extends Actor {

  var players: Map[String, ActorRef] = Map()
  val game: Game = new Game()

  override def receive: Receive = {
    case m: AddPlayer => game.addPlayer(m.username)
    case m: RemovePlayer => game.removePlayer(m.username)
    case m: MovePlayer => game.players(m.username).move(new PhysicsVector(m.x, m.y))
    //println("Moving player: " + m.username)
    case m: StopPlayer => game.players(m.username).stop()

    case Update => game.update()
    case SendGameState => sender() ! GameState(game.gameState())
    case f: Fire => game.fire(f.username)
  }


}
