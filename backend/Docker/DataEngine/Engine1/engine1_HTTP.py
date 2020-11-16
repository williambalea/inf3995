from flask import Flask
from engine1 import Engine1

app = Flask(__name__)

engine1 = Engine1()

@app.route('/engine1')
def hello_world():
    return 'Hello World from Engin 1'

@app.route('/engine1/db')
def test():
    return engine1.account_db()

@app.route('/engine1/station/<code>')
def stationCode(code):
    return engine1.getStationCode(code)
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations() 

@app.route('/engine1/logs/<byte>')
def logs(byte):
    return engine1.logsToJSON(byte)
