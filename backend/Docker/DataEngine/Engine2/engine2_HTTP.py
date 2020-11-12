from flask import Flask
from engine2 import Engine2
from flask_api import status
app = Flask(__name__)

engine2 = Engine2()

@app.route('/engine2')
def hello_world():
    return 'Hello World from Engin 2', status.HTTP_200_OK

@app.route('/engine2/data/usage/<year>/<time>/<station>')
def dataUsage(year, time, station):
    return engine2.datatoJSON(year, time, station)

@app.route('/engine2/logs/<octect>')
def logs(octect):
    return engine2.logsToJSON(octect)
