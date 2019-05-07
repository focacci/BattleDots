var socket = io.connect({transports: ['websocket']});
socket.on('gameState', displayGame);

var GAME = document.getElementById("game");
var context = game.getContext("2d");
context.globalCompositeOperation = 'source-over';

function displayGame(json_game) {
  console.log(json_game);
  var game = JSON.parse(json_game);
  var width = game["gridDimensions"]["width"];
  var height = game["gridDimensions"]["height"];

  makeGameGrid(width, height);

  var players = game["players"];
  var bullets = game["bullets"];

  for (let p of game["players"]) {
    circle(players[p]["location"]["x"], players[p]["location"]["y"], 50, players[p]["username"] === socket.id ? '#0ec51f' : '#ce000d');
  }

  for (const b of bullets) {
    circle(bullets[b]["location"]["x"], bullets[b]["location"]["y"], 20, '#000000')
  }
}

function circle(x, y, size, color) {
  context.fillStyle = color;
  context.beginPath();
  context.arc(
    x,
    y,
    size,
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