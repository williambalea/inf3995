from flask import Flask
from engine3 import Engine3
app = Flask(__name__)

engine3 = Engine3()

@app.route('/engine3')
def hello_world():
    return 'Hello World from Engin 3'

