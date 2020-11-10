from flask import Flask
from engine2 import Engine2
app = Flask(__name__)

engine2 = Engine2()

@app.route('/engine2')
def hello_world():
    return 'Hello World from Engin 2'

@app.route('/engine2/data/usage/<year>/<time>/<station>')
def dataUsage(year, time, station):
    return engine2.datatoJSON(year, time, station)

@app.route('/engine2/logs')
def logs():
    return  engine2.logsToJSON()