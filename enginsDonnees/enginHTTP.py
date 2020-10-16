from flask import Flask
from enginSQL import EnginSQL
app = Flask(__name__)




enginsql = EnginSQL()
# with app.app_context():
#     EnginSQL()


@app.route('/')
def hello_world():
    return 'Hello World'

@app.route('/station/<code>')
def stationCode(code):
    return enginsql.getStationCode(code)
    
@app.route('/allStation')
def allStation():
    return enginsql.getAllStations()

    
# @app.route('/test/<year>')
# def test(year):
#     return enginsql.getAllYearRaw(year)