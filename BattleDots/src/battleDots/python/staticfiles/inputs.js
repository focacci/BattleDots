var keyPressed = {
  "w": false,
  "a": false,
  "s": false,
  "d": false,
  "space": false,
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
  } else if (event.key === "space") {
    setState("space", toSet);
  }
}

document.addEventListener("keydown", function (event) {
  handleEvent(event, true);
});

document.addEventListener("keyup", function (event) {
  handleEvent(event, false);
});
