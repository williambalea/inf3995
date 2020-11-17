from flask import Flask
from flask_api import status
from DataEngine.Engine1.engine1 import Engine1
from DataEngine.Mysql.mysql import MySqlDB

app = Flask(__name__)
engine1 = Engine1()
mysql = MySqlDB()

@app.route('/engine1')
def hello_world():
    return 'Hello World from Engin 1', status.HTTP_200_OK
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations() 

@app.route('/<engine1>/logs/<byte>')
def logs(engine1, byte):
    return mysql.authorizationLogs(engine1, byte)
