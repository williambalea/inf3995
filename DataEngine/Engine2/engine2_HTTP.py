from flask import Flask
from engine2 import Engine2
app = Flask(__name__)

engine2 = Engine2()

@app.route('/engin2')
def hello_world():
    return 'Hello World from Engin 2'

@app.route('/engin2/data/usage/<year>/<time>/<station>')
def dataUsage(year, time, station):
    return engine2.dataGraphtoJSON(year, time, station)
