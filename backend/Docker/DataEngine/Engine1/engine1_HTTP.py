from flask import Flask
from engine1 import Engine1
from flask_socketio import SocketIO
import logging
app = Flask(__name__)
socketio = SocketIO(app)

engine1 = Engine1()

@app.route('/engine1')
def hello_world():
    return 'Hello World from Engin 1'

@app.route('/engine1/station/<code>')
def stationCode(code):
    return engine1.getStationCode(code)
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations() 

@app.route('/engine1/logs')
def logs():
    return  engine1.logsToJSON()

@socketio.on('connect')
def connect():
    socketio.emit('my response', {'data': 'Connected Engine 1'})
    logging.info("Connected Socket")

@socketio.on('disconnect')
def disconnected():
    socketio.emit('my response', {'data': 'Disconnected Engine 1'}, broadcast=True)
    logging.info("Disconnected Socket")
