package battleDots.model.game_objects

class GameObject {

  var exists: Boolean = true

  def destroy(): Unit = {
    this.exists = false
  }

}
