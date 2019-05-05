package tests

import battleDots.model.Game
import battleDots.model.game_objects.{Boundary, PhysicalObject, Player}
import org.scalatest._
import battleDots.model.physics._

class TestPhysics extends FunSuite {

  test("Testing:  computePotentialLocation") {
    val location: PhysicsVector = new PhysicsVector(1, 1)
    val velocity: PhysicsVector = new PhysicsVector(-2, 3)
    val obj: PhysicalObject = new PhysicalObject(location, velocity)

    val time: Double = 2

    val actual: PhysicsVector = Physics.computePotentialLocation(obj, time)
    val expected: PhysicsVector = new PhysicsVector(10, 7)

    assert((actual.x == actual.x) && (actual.y == expected.y))
  }

  test("Testing:  detectBoundaryCollision") {
    val time: Double = 1

    val game: Game = new Game
    game.addPlayer("test_user")

    val test_user: Player = game.players("test_user")
    test_user.velocity.x = -3

    var pl: PhysicsVector = Physics.computePotentialLocation(test_user, time)
    assert(Physics.detectBoundaryCollision(pl, game))

    newLocation(test_user, 594, 400)
    newVelocity(test_user, 100, 0)
    pl = Physics.computePotentialLocation(test_user, time)
    assert(Physics.detectBoundaryCollision(pl, game))
  }



  def newLocation(player: Player, x: Double, y: Double): Unit = {
    player.location.x = x
    player.location.y = y
  }

  def newVelocity(player: Player, x: Double, y: Double): Unit = {
    player.velocity.x = x
    player.velocity.y = y
  }

}
