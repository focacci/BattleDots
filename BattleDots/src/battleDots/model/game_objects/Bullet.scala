package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector

class Bullet(
            val shooter: String,
            in_location: PhysicsVector,
            in_velocity: PhysicsVector,
            ) extends PhysicalObject(in_location, in_velocity) {



  def hit(player: Player): Unit = {
    if (player.name != this.shooter) {
      this.destroy()
    }
  }

  override def collide(): Unit = {
    this.destroy()
  }

}
