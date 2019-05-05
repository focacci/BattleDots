var keyPressed = {
  "w": false,
  "a": false,
  "s": false,
  "d": false,
  "ArrowUp": false,
  "ArrowDown": false,
  "ArrowLeft": false,
  "ArrowRight": false
};

function setState(key, value) {
  keyPressed[key] = value;
  socket.emit("inputs", JSON.stringify(keyPressed));
}

function handleEvent(event, toSet) {
  if (event.key === "w") {
    setState("w", toSet);
  } else if (event.key === "a") {
    setState("a", toSet);
  } else if (event.key === "s") {
    setState("s", toSet);
  } else if (event.key === "d") {
    setState("d", toSet);
  } else if (event.key === "ArrowUp") {
    setState("ArrowUp", toSet);
  } else if (event.key === "ArrowDown") {
    setState("ArrowDown", toSet);
  } else if (event.key === "ArrowLeft") {
    setState("ArrowLeft", toSet);
  } else if (event.key === "ArrowRight") {
    setState("ArrowRight, toSet");
  }
}

document.addEventListener("keydown", function (event) {
  handleEvent(event, true);
});

document.addEventListener("keyup", function (event) {
  handleEvent(event, false);
});
