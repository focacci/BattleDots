package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector

class Bullet(
            in_location: PhysicsVector,
            in_velocity: PhysicsVector
            ) extends PhysicalObject(in_location, in_velocity) {

  override def hit(): Unit = {
    this.destroy()
  }

  override def collide(): Unit = {
    this.destroy()
  }

}
