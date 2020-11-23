from flask import Flask
from Engine3.engine3 import Engine3
from Engine3.engine3_pred_error import Engine3_Pred_Error
from pathlib import Path
import pandas as pd
app = Flask(__name__)

engine3 = Engine3()
engine3_pred_error = Engine3_Pred_Error()
# $ export FLASK_APP=hello.py
# export FLASK_ENV=development
# $ flask run
@app.route('/engine3')
def hello_world():
    return 'Hello World from Engin 3'

@app.route('/engine3/prediction/usage/<station>/<groupby>/<startDate>/<endDate>')
def predictionUsage(station, groupby, startDate,endDate):
    startDate = str(startDate)
    endDate = str(endDate)
    predictions_df = engine3.load_prediction_df()
    filtered_pred_df = engine3.filter_prediction(predictions_df, station, groupby, startDate, endDate)
    x = engine3.get_graph_X(filtered_pred_df, groupby)
    y = engine3.get_graph_Y(filtered_pred_df)
    pred_graph = engine3.get_prediction_graph( groupby, x, y)
    return engine3.datatoJSON(pred_graph, x, y)

@app.route('/engine3/prediction/error')
def predictionError():
    my_file = Path(engine3.PREDICTION_DF_PATH)
    if my_file.is_file():
        # load the pred_df from disk
        pred_df = pd.read_pickle(my_file)
        return engine3_pred_error.get_error_json(pred_df)
    else:
        return 'ERROR: prediction not ready yet'

