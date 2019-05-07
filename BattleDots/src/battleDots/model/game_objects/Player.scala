package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector
import play.api.libs.json.{JsValue, Json}

class Player(
            val name: String,
            in_location: PhysicsVector,
            in_velocity: PhysicsVector
            ) extends PhysicalObject(in_location, in_velocity) {

  var speed: Double = 5
  var health: Int = 5

  def move(dir: PhysicsVector): Unit = {
    val unit_direction = dir.unitVector()
    this.velocity.x = unit_direction.x * speed
    this.velocity.y = unit_direction.y * speed
  }

  def stop(): Unit = {
    this.velocity = new PhysicsVector(0, 0)
  }

  def hitBy(bullet: Bullet): Unit = {
    if (this.name != bullet.shooter) {
      this.health -= 1
    }
    if (this.health < 1) {
      this.location = this.in_location
    }
  }

  override def collide(): Unit = {
    this.stop()
  }


}
