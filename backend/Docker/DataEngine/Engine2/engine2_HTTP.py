from flask import Flask
from flask_api import status
from DataEngine.Engine2.engine2 import Engine2
from DataEngine.Mysql.mysql import MySqlDB

app = Flask(__name__)
engine2 = Engine2()
mysql = MySqlDB()

@app.route('/engine2')
def hello_world():
    return 'Hello World from Engin 2', status.HTTP_200_OK

@app.route('/engine2/data/usage/<year>/<time>/<station>')
def dataUsage(year, time, station):
    return engine2.datatoJSON(year, time, station)

@app.route('/<engine2>/logs/<byte>')
def logs(engine2, byte):
    return mysql.authorizationLogs(engine2, byte)
