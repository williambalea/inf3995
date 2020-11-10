import socketio

sio = socketio.Client()
sio.connect("http://localhost:2001")


@sio.on('connect')
def connect():
    print("Socket client connected")
    print("Im connected")

@sio.on
def connect_error():
    print("Connection failed")

@sio.on
def disconnect():
    print("Im disconnected")

@sio.on('logs')
def get_json(data):
    print('received json:' + data)

    


