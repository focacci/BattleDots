package battleDots.model.physics

import battleDots.model.Game
import battleDots.model.game_objects.{Boundary, PhysicalObject}

object Physics {


  val s: Double = 0.0000001


  def equal(a: Double, b: Double): Boolean = {
    (a - b).abs < this.s
  }


  def computePotentialLocation(obj: PhysicalObject, dt: Double): PhysicsVector = {
    val x = obj.location.x + (dt * obj.velocity.x)
    val y = obj.location.y + (dt * obj.velocity.y)
    new PhysicsVector(x, y)
  }


  def detectBoundaryCollision(pl: PhysicsVector, game: Game): Boolean = {
    if (pl.x <= 0 | pl.x >= game.width) {
      true
    } else if (pl.y <= 0 | pl.y >= game.height) {
      true
    } else {
      false
    }
  }


  def updateGame(game: Game, dt: Double): Unit = {
    val world: World = game.world

    for (obj <- world.objects) {
      val potentialLocation = computePotentialLocation(obj, dt)
      val boundaryCollision = detectBoundaryCollision(potentialLocation, game)
      if (!boundaryCollision) {
        obj.location = potentialLocation
        println(obj.location.toString)
      } else {
        obj.collide()
      }
    }

    world.objects = world.objects.filter(obj => obj.exists)
  }


  def slope(p1: PhysicsVector, p2: PhysicsVector): Double = {
    if (p1.x == p2.x) {
      10000000000.0
    }
    else {
      (p2.y - p1.y) / (p2.x - p1.x)
    }
  }


  def yIntercept(p1: PhysicsVector, m: Double): Double = {
    p1.y - m * p1.x
  }


}
