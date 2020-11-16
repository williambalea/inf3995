from flask import Flask
from engine1 import Engine1
from flask_api import status
import logging
# from flask_mysqldb import MySQL
# import MySQLdb.cursors

app = Flask(__name__)
# mysql = MySQL(app)
engine1 = Engine1()

# app.config['MYSQL_HOST'] = '34.70.117.28'
# app.config['MYSQL_USER'] = 'root'
# app.config['MYSQL_PASSWORD'] = 'jerome'
# app.config['MYSQL_DB'] = 'Server'

@app.route('/engine1')
def hello_world():
    return engine1.login()
    # return 'Hello World from Engin 1', status.HTTP_200_OK
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations() 

@app.route('/engine1/logs/<byte>')
def logs(byte):
    return engine1.logsToJSON(byte)
