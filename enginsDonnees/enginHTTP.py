from flask import Flask
from enginSQL import EnginSQL
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

    
# @app.route('/test/<year>')
# def test(year):
#     return enginsql.getAllYearRaw(year)