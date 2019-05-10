package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector

class Player(
            val name: String,
            var in_location: PhysicsVector,
            var in_velocity: PhysicsVector
            ) extends PhysicalObject(in_location, in_velocity) {

  var speed: Double = 0.2
  var health: Int = 10

  def move(dir: PhysicsVector): Unit = {
    val direction: PhysicsVector = dir.unitVector()
    this.velocity.x = direction.x * this.speed
    this.velocity.y = direction.y * this.speed
    //println("New velocity:  " + this.velocity.toString)
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
      val r = scala.util.Random
      this.location = new PhysicsVector(r.nextInt(500).toDouble, r.nextInt(300).toDouble)
      this.health = 10

    }
  }

  override def collide(): Unit = {
    this.stop()
  }


}
