package battleDots.model.physics

class PhysicsVector(
                   var x: Double,
                   var y: Double) {

  def distanceFrom(vector: PhysicsVector): Double = {
    Math.sqrt(Math.pow(this.x - vector.x, 2) + Math.pow(this.y - vector.y, 2))
  }

  def unitVector(): PhysicsVector = {
    if (this.x == 0 & this.y == 0) {
      new PhysicsVector(0, 0)
    }
    else {
      val magnitude =Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2))
      new PhysicsVector(this.x / magnitude, this.y / magnitude)
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
