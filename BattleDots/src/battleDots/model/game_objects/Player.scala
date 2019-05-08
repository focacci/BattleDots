package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector

class Player(
            val name: String,
            in_location: PhysicsVector,
            in_velocity: PhysicsVector
            ) extends PhysicalObject(in_location, in_velocity) {

  var speed: Double = 0.3
  var health: Int = 5

  def move(dir: PhysicsVector): Unit = {
    val direction: PhysicsVector = dir.unitVector()
    this.velocity.x = direction.x * this.speed
    this.velocity.y = direction.y * this.speed
    println("New velocity:  " + this.velocity.toString)
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
