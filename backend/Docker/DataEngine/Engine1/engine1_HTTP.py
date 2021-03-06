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
def all_station():
    return engine1.get_all_stations(), status.HTTP_200_OK

@app.route('/<engine1>/logs/<byte>')
def logs_engine1(engine1, byte):
    return logs.authorization_logs(engine1, byte)
