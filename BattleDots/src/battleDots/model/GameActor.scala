package battleDots.model


import akka.actor.{Actor, ActorRef}
import battleDots.model.game_objects.Bullet
import battleDots.model.physics.PhysicsVector



class GameActor extends Actor {

  var players: Map[String, ActorRef] = Map()
  val game: Game = new Game()

  override def receive: Receive = {
    case m: AddPlayer =>
      this.game.addPlayer(m.username)

    case m: RemovePlayer =>
      this.game.removePlayer(m.username)

    case m: MovePlayer =>
      println("Moving player: " + m.username)
      this.game.players(m.username).move(new PhysicsVector(m.x, m.y))

    case m: StopPlayer =>
      this.game.players(m.username).stop()

    case Update =>
      this.game.update()

    case SendGameState =>
      sender() ! GameState(this.game.gameState())

    case f: Fire =>
      this.game.fire(f.username)
  }


}
