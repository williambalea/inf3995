from flask import Flask
from flask_api import status
from Engine1.engine1 import Engine1
from Logs.logs import Logs
import logging

app = Flask(__name__)
log = logging.getLogger('werkzeug')
log.disabled = True
engine1 = Engine1()
logs = Logs()

logging.info("Engine 1 has started")

@app.route('/engine1')
def hello_world():
    return 'Hello World from Engin 1', status.HTTP_200_OK
    
@app.route('/engine1/station/all')
def allStation():
    return engine1.getAllStations(), status.HTTP_200_OK

@app.route('/<engine1>/logs/<byte>')
def logs(engine1, byte):
    return logs.authorizationLogs(engine1, byte)