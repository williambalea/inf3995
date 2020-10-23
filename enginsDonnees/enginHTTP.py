from flask import Flask
from engin1 import Engin1
from engin2 import Engin2
import pandas as pd
app = Flask(__name__)

# - flask http request
# - curl localhost:5000
# run server
# $ export FLASK_APP=enginHTTP.py
# $ flask run
# $ curl 127.0.0.1:5000/route


engin1 = Engin1()
engin2 = Engin2()
# with app.app_context():
#     EnginSQL()


@app.route('/engin1')
def hello_world():
    return 'Hello World from Engin 1'

@app.route('/engin1/station/<code>')
def stationCode(code):
    return enginsql.getStationCode(code)
    
@app.route('/engin1/station/all')
def allStation():
    return enginsql.getAllStations() 

@app.route('/engin2/data/usage/<year>/<time>/<station>')
def dataUsage(year, time, station):
    return engin2.dataGraphtoJSON(year, time, station)
