from flask import Flask

app = Flask(__name__)

@app.route('/')
def helle_world():
    return 'Hello, World!'
    