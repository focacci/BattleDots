package battleDots.model

import battleDots.model.game_objects.{Bullet, Player}
import battleDots.model.physics.{Physics, PhysicsVector, World}
import play.api.libs.json.{JsValue, Json}

class Game {

  val world: World = new World

  var players: Map[String, Player] = Map()
  var player_size: Double = 3.0
  var time: Long = System.nanoTime()

  var width: Int = 500
  var height: Int = 300

  var bulletSpeed: Int = 2
  val sightRange: Int = 10

  def addPlayer(username: String): Unit = {
    val player: Player = new Player(username, startVector(), new PhysicsVector(0,0))
    this.players += (username -> player)
    this.world.players = player :: this.world.players
  }

  def removePlayer(username: String): Unit = {
    this.players(username).destroy()
    this.players -= username
  }

  def startVector(): PhysicsVector = {
    new PhysicsVector(250, 150)
  }

  def update(): Unit = {
    val t: Long = System.nanoTime()
    val dt = (t - this.time) / 1000000000.0
    Physics.updateGame(this, dt)
    checkForHits()
  }

  def gameState(): String = {
    val gameState: Map[String, JsValue] = Map(
      "gridDimensions" -> Json.toJson(Map(
        "width" -> width,
        "height" -> height)),
      "players" -> Json.toJson(this.players.map({ case (name, player) => Json.toJson(Map(
        "location" -> Json.toJson(Map(
          "x" -> Json.toJson(player.location.x),
          "y" -> Json.toJson(player.location.y))),
        "health" -> Json.toJson(this.players(name).health),
        "username" -> Json.toJson(name)))})),
      "bullets" -> Json.toJson(this.world.bullets.map({bullet =>Json.toJson(Map(
        "x" -> bullet.location.x,
        "y" -> bullet.location.y))}))
    )
    Json.stringify(Json.toJson(gameState))
  }

  def fire(username: String): Unit = { // fires a bullet at each player in range
    val playerLocation: PhysicsVector = this.players(username).location
    for (player <- playersInRange(username)) {
      val bullet: Bullet = new Bullet(username, playerLocation,
        new PhysicsVector(player.x - playerLocation.x, player.y - playerLocation.y).correctMagnitude(bulletSpeed)
      )
      this.world.bullets = bullet :: this.world.bullets
    }
  }


  def playersInRange(username: String): List[PhysicsVector] = {
    val player: PhysicsVector = this.players(username).location
    val otherPlayers = this.players - username
    var playersInRange: List[PhysicsVector] = List()

    for ((_,otherPlayer) <- otherPlayers) {
      if (player.distanceFrom(otherPlayer.location) <= this.sightRange) {
        playersInRange = otherPlayer.location :: playersInRange
      }
    }
    playersInRange
  }

  def checkForHits(): Unit = {
    for (player <- this.world.players) {
      for (bullet <- this.world.bullets) {
        if (player.location.distanceFrom(bullet.location) < this.player_size) {
          player.hitBy(bullet)
          bullet.hit(player)
        }
      }
    }
  }



}
