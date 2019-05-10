package battleDots.model

import battleDots.model.game_objects.{Bullet, Player}
import battleDots.model.physics.{Physics, PhysicsVector, World}
import play.api.libs.json.{JsValue, Json}

class Game {

  val world: World = new World

  var players: Map[String, Player] = Map()
  var bullets: List[Bullet] = List()

  var player_size: Double = 20.0
  var time: Long = System.nanoTime()

  var width: Int = 500
  var height: Int = 300

  var bulletSpeed: Double = .3
  val sightRange: Int = 200

  def addPlayer(username: String): Unit = {
    val player: Player = new Player(username, startVector(), new PhysicsVector(0,0))
    this.players += (username -> player)
    this.world.objects = player :: this.world.objects
  }

  def removePlayer(username: String): Unit = {
    this.players(username).destroy()
    this.players -= username
  }

  def startVector(): PhysicsVector = {
    //starts player at random spot on map
    val r = scala.util.Random
    new PhysicsVector(r.nextInt(500).toDouble, r.nextInt(300).toDouble)
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
        "velocity" -> Json.toJson(Map(
          "vx" -> Json.toJson(player.velocity.x),
          "vy" -> Json.toJson(player.velocity.y))),
        "health" -> Json.toJson(this.players(name).health),
        "username" -> Json.toJson(name)))})),
      "bullets" -> Json.toJson(this.bullets.map({ bullet => Json.toJson(Map(
        "x" -> bullet.location.x,
        "y" -> bullet.location.y))}))
    )
    Json.stringify(Json.toJson(gameState))
  }

  /*
  def fire(username: String): Unit = { // fires a bullet at each player in range
    val playerLocation: PhysicsVector = this.players(username).location
    for (player <- playersInRange(username)) {
      val bullet: Bullet = new Bullet(username, playerLocation,
        new PhysicsVector(player.x - playerLocation.x, player.y - playerLocation.y).correctMagnitude(bulletSpeed)
      )
      this.world.objects = bullet :: this.world.objects
      this.bullets = bullet :: this.bullets
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
  */

  def fire(username: String): Unit = {
    val game = gameState()
    val parsed: JsValue = Json.parse(game)
    val players = (parsed \ "players").as[List[Map[String, JsValue]]]
    var playerMap: Map[String, Player] = Map()

    for (i <- players) {
      val x = i("location")("x").as[Double]
      val y = i("location")("y").as[Double]
      val vx = i("velocity")("vx").as[Double]
      val vy = i("velocity")("vy").as[Double]
      val id = i("username").as[String]
      val player: Player = new Player(id, new PhysicsVector(x, y), new PhysicsVector(vx, vy))
        playerMap += (id -> player)
    }
    var mainPlayerLoc: PhysicsVector = new PhysicsVector(playerMap(username).location.x, playerMap(username).location.y)
    var min: Double = 10000000.0
    var minId: String = ""
    for (j <- playerMap.keys) {
      if(playerMap(j).location.distanceFrom(mainPlayerLoc) < min && (j != username)) {
        min = playerMap(j).location.distanceFrom(mainPlayerLoc)
        minId = j
      }
    }
    if (min < sightRange) {
      val bullet: Bullet = new Bullet(username, mainPlayerLoc,
        new PhysicsVector(playerMap(minId).location.x - mainPlayerLoc.x, playerMap(minId).location.y - mainPlayerLoc.y).correctMagnitude(bulletSpeed)
      )
      this.world.objects = bullet :: this.world.objects
      this.bullets = bullet :: this.bullets
    }
  }

  def checkForHits(): Unit = {
    for ((_,player) <- this.players) {
      for (bullet <- this.bullets) {
        if (player.location.distanceFrom(bullet.location) < this.player_size) {
          player.hitBy(bullet)
          bullet.hit(player)
        }
      }
    }
  }



}
