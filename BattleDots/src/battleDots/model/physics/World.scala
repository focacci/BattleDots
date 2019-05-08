package battleDots.model.physics

import battleDots.model.game_objects.{Bullet, PhysicalObject, Player}

class World {

  var players: List[Player] = List()
  var bullets: List[Bullet] = List()
  var objects: List[PhysicalObject] = all_objects()

  def all_objects(): List[PhysicalObject] = {
    this.players ::: this.bullets
  }


}
