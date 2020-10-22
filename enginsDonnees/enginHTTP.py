from flask import Flask
from enginSQL import EnginSQL
from engin2 import Engin2
app = Flask(__name__)




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

@app.route('/donnees/usage/<annee>/<temps>/<station>')
def dataUsage(annee, temps, station):
    engin2 = Engin2()
    path = "./kaggleData/OD_'{}'".format(annee)
    path += ".csv"
    df = pd.read_csv(path)
    countstart = 
    countend
    graph = 
    #conversion construction json

    json1 = 
    return json1

    
# @app.route('/test/<year>')
# def test(year):
#     return enginsql.getAllYearRaw(year)