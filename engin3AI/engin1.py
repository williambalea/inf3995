from flask import Flask

app = Flask(__name__)

@app.route('/engin1')
def hello_world():
    return 'Hello, World from Engin1!'

    