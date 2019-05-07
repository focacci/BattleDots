package tests

import battleDots.model.Game
import battleDots.model.game_objects.Bullet
import battleDots.model.physics.{Physics, PhysicsVector}
import org.scalatest._

class TestGame extends FunSuite {

  test("Testing:  bullets fire and players take damage") {
    val game: Game = new Game

    game.addPlayer("user")
    game.players("user").location.x = 2
    game.players("user").location.y = 2

    game.addPlayer("user2")
    game.players("user2").location.x = 2
    game.players("user2").location.y = 7

    game.fire("user2")


    Physics.updateGame(game, 1)

    println(game.gameState())

    assert(game.players("user").health == 4)
    assert(game.world.bullets.isEmpty)
  }

}
