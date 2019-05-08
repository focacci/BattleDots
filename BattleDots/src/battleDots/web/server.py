import json
import socket
from threading import Thread

from flask import Flask, send_from_directory, request
from flask_socketio import SocketIO

import eventlet

eventlet.monkey_patch()

app = Flask(__name__)
server = SocketIO(app)

# ** Connect to Scala TCP socket server **

scala_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
scala_socket.connect(('localhost', 4242))


delimiter = "~"


def listen_to_scala(the_socket):
    buffer = ""
    while True:
        buffer += the_socket.recv(1024).decode()
        while delimiter in buffer:
            message = buffer[:buffer.find(delimiter)]
            buffer = buffer[buffer.find(delimiter) + 1:]
            send_to_clients(message)


def send_to_clients(data):
    server.emit('gameState', data, broadcast=True)


def send_to_scala(data):
    scala_socket.sendall((json.dumps(data) + delimiter).encode())


Thread(target=listen_to_scala, args=(scala_socket,)).start()


# ** Setup and start Python web server **

@server.on('connect')
def got_message():
    print(request.sid + " connected")
    message = {"username": request.sid, "action": "connected"}
    send_to_scala(message)


@server.on('disconnect')
def disconnect():
    print(request.sid + " disconnected")
    message = {"username": request.sid, "action": "disconnected"}
    send_to_scala(message)


@server.on('inputs')
def key_state(json_inputs):
    inputs = json.loads(json_inputs)
    print("Received inputs: " + json_inputs)

    x = 0.0
    y = 0.0
    fire = False

    if inputs["a"] and not inputs["d"]:
        x = -1.0
    elif inputs["d"] and not inputs["a"]:
        x = 1.0

    if inputs["w"] and not inputs["s"]:
        y = -1.0
    elif inputs["s"] and not inputs["w"]:
        y = 1.0

    if inputs["p"]:
        fire = True

    message = {"username": request.sid, "action": "move", "x": x, "y": y, "fire": fire}
    send_to_scala(message)


@app.route('/')
def index():
    print("sending: index.html")
    return send_from_directory('staticfiles', 'index.html')


@app.route('/<path:filename>')
def static_files(filename):
    print("sending: " + filename)
    return send_from_directory('staticfiles', filename)


print("Listening on port 1234")
server.run(app, port=1234)
