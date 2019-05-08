package battleDots.model


case class AddPlayer(username: String)
case class RemovePlayer(username: String)
case class MovePlayer(username: String, x: Double, y: Double)
case class StopPlayer(username: String)
case class GameState(state: String)
case object Update
case object SendGameState
case class Fire(username: String)
