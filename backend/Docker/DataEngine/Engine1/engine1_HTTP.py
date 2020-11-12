from flask import Flask
from engine1 import Engine1
from flask_api import status
import logging

app = Flask(__name__)
engine1 = Engine1()

@app.route('/engine1')
def hello_world():
    return 'Hello World from Engin 1', status.HTTP_200_OK
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations() 

@app.route('/engine1/logs/<octect>')
def logs(octect):
    return engine1.logsToJSON(octect)
