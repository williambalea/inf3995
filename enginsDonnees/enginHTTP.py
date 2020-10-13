from flask import Flask
from enginSQL import EnginSQL
app = Flask(__name__)

enginsql = EnginSQL()

@app.route('/')
def hello_world():
    return enginsql.getStationCode(5003)

# @app.route('/test')
# def test():
#     return enginsql.getStationCode(5003)