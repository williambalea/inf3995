from flask import Flask
from flask_api import status
from Engine2.engine2 import Engine2
from Logs.logs import Logs
import logging

app = Flask(__name__)
log = logging.getLogger('werkzeug')
log.disabled = True
engine2 = Engine2()
logs = Logs()

logging.info("Engine 2 has started")

@app.route('/engine2')
def hello_world():
    return 'Hello World from Engin 2', status.HTTP_200_OK

@app.route('/engine2/data/usage/<year>/<time>/<station>')
def data_usage(year, time, station):
    return engine2.data_to_JSON(year, time, station), status.HTTP_200_OK

@app.route('/<engine2>/logs/<byte>')
def logs_engine2(engine2, byte):
    return logs.authorization_logs(engine2, byte)
