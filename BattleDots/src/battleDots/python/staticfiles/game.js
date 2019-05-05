var socket = io.connect({transports: ['websocket']});
socket.on('gameState', parseGame);

var game = document.getElementById("game");
var context = game.getContext("2d");
context.globalCompositeOperation = 'source-over';

function parseGame(json_game) {
  var game = JSON.parse(json_game);
  var width = game["gridDimensions"]["width"];
  var height = game["gridDimensions"]["height"];

  makeGameGrid(width, height);

  var players = game["players"];
  var bullets = game["bullets"];

  for (var p = 0; p < players.length(); p ++) {
    circle(players[p]["location"]["x"], players[p]["location"]["y"], 3, players[p]["username"] === socket.id ? '#0ec51f' : '#ce000d');
  }

  for (var b = 0; b < bullets.length(); b ++) {
    circle(bullets[b]["location"]["x"], bullets[b]["location"]["y"], 1, '#000000')
  }
}

function circle(x, y, size, color) {
  context.fillStyle = color;
  context.beginPath();
  context.arc(
    x,
    y,
    size / 10.0,
    0,
    2 * Math.PI);
  context.fill();
  context.strokeStyle = 'black';
  context.stroke();
}

function makeGameGrid(width, height) {
  context.clearRect(0, 0, width, height)
  context.strokeStyle = '#000000';

  context.beginPath();
  context.moveTo(0, 0);
  context.lineTo(width, 0);
  context.stroke();

  context.beginPath();
  context.moveTo(width, 0);
  context.lineTo(width, height);
  context.stroke();

  context.beginPath();
  context.moveTo(width, height);
  context.lineTo(0, height);
  context.stroke();

  context.beginPath();
  context.moveTo(0, height);
  context.lineTo(0, 0);
  context.stroke();
}