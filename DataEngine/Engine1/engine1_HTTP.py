from flask import Flask
from engine1 import Engine1

app = Flask(__name__)

engine1 = Engine1()

@app.route('/engin1')
def hello_world():
    return 'Hello World from Engin 1'

@app.route('/engin1/station/<code>')
def stationCode(code):
    return engine1.getStationCode(code)
    
@app.route('/engin1/station/all')
def allStation():
    return engine1.getAllStations() 
