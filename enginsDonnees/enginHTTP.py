from flask import Flask
from enginSQL import EnginSQL
from engin2 import Engin2
import pandas as pd
app = Flask(__name__)

# - flask http request
# - curl localhost:5000
# run server
# $ export FLASK_APP=enginHTTP.py
# $ flask run
# $ curl 127.0.0.1:5000/route


enginsql = EnginSQL()
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
    ye = int(year)
    ti = str(time)
    if str(station) == 'toutes':
        st = st(station)
    else:
        st = int(station)

    engin2 = Engin2()
    path = "./kaggleData/OD_{}".format(ye)
    path += ".csv"
    df = pd.read_csv(path)
    countStart = engin2.getPerTimeCountStart(df, st, ti)
    countEnd = engin2.getPerTimeCountEnd(df, st, ti)
    plt = engin2.getGraphPerTime(countStart, countEnd, st, ti)
    return engin2.toBase64(plt)
