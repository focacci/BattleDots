package battleDots.model.game_objects

import battleDots.model.physics.PhysicsVector

class PhysicalObject(
                    var location: PhysicsVector,
                    var velocity: PhysicsVector
                    ) extends GameObject {

  def collide(): Unit = {}
  //def onGround(): Unit = {}

  override def toString = s"PhysicalObject($location, $velocity)"

}
