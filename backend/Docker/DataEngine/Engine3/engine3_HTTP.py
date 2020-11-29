from flask import Flask
from Engine3.engine3 import Engine3
from Engine3.engine3_pred_error import Engine3_Pred_Error
from pathlib import Path
import pandas as pd
app = Flask(__name__)

engine3 = Engine3()
engine3_pred_error = Engine3_Pred_Error()

@app.route('/engine3')
def hello_world():
    return 'Hello World from Engin 3'

@app.route('/engine3/prediction/usage/<station>/<groupby>/<startDate>/<endDate>')
def predictionUsage(station, groupby, startDate,endDate):
    startDate = str(startDate)
    endDate = str(endDate)
    predictions_df = engine3.get_prediction(station, startDate, endDate)
    filtered_pred_df = engine3.groupby_filter(predictions_df, groupby)
    x = engine3.get_graph_X(filtered_pred_df, groupby)
    y = engine3.get_graph_Y(filtered_pred_df)
    pred_graph = engine3.get_prediction_graph( groupby, x, y)
    return engine3.datatoJSON(pred_graph, x, y)

@app.route('/engine3/prediction/error')
def predictionError():
    rf_model_file = Path(engine3.RF_MODEL_PATH)
    if rf_model_file.is_file():
        pred_with_error_path = Path(engine3.PREDICTION_DF_ALL_2017_PATH)
        if pred_with_error_path.is_file():
            pred_df = pd.read_pickle(pred_with_error_path)
        else:
            pred_df = engine3.get_prediction('all', '15-04-2017', '30-09-2017')

            pred_df.to_pickle(pred_with_error_path)
        
        return engine3_pred_error.get_error_json(pred_df)
    else:
        return "Training not done, please come back later"
