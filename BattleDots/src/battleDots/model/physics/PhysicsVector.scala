package battleDots.model.physics

class PhysicsVector(
                   var x: Double,
                   var y: Double) {

  def distanceFrom(vector: PhysicsVector): Double = {
    Math.sqrt(Math.pow(this.x - vector.x, 2) + Math.pow(this.y - vector.y, 2))
  }

  def unitVector(): PhysicsVector = {
    if (x == 0 & y == 0) {
      new PhysicsVector(0, 0)
    }
    else {
      val magnitude =Math.sqrt(Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0))
      new PhysicsVector(x / magnitude, y / magnitude)
    }
  }

  override def toString: String = {
    "(" + this.x + ", " + this.y + ")"
  }

  def correctMagnitude(magnitude: Double): PhysicsVector = {
    val vector = this.unitVector()
    vector.x *= magnitude
    vector.y *= magnitude
    vector
  }

}
