package battleDots.model.physics

import battleDots.model.game_objects.{Boundary, PhysicalObject}

class World(var g: Double) {

  var players: List[PhysicalObject] = List()
  var bullets: List[PhysicalObject] = List()
  var boundaries: List[Boundary] = List()

  def objects(): List[PhysicalObject] = {
    players ::: bullets
  }
}
