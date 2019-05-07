package battleDots.model.physics

import battleDots.model.game_objects.{Boundary, Bullet, PhysicalObject, Player}

class World(var g: Double) {

  var players: List[Player] = List()
  var bullets: List[Bullet] = List()
  var boundaries: List[Boundary] = List()

  def objects(): List[PhysicalObject] = {
    players ::: bullets
  }
}
