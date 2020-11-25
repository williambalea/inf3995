from flask import Flask
from engine3 import Engine3
from engine3_pred_error import Engine3_Pred_Error
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
    return 'Hello World from Engin 3 :)'

# http://127.0.0.1:5000/engine3/prediction/usage/5003/perMonth/'27-05-2017'/'27-08-2017'
@app.route('/engine3/prediction/usage/<station>/<groupby>/<startDate>/<endDate>')
def predictionUsage(station, groupby, startDate,endDate):
    startDate = str(startDate)
    endDate = str(endDate)
    print('Dates')
    print(startDate)
    print(endDate)
    predictions_df = engine3.load_prediction_df(station, startDate, endDate)

    print('predictions_df: ', predictions_df)
    filtered_pred_df = engine3.groupby_filter(predictions_df, groupby)
    print('filtered_pred_df: ', filtered_pred_df)

    print(filtered_pred_df)
    x = engine3.get_graph_X(filtered_pred_df, groupby)
    print('x: ', x)
    y = engine3.get_graph_Y(filtered_pred_df)
    print('y: ', y)
    pred_graph = engine3.get_prediction_graph( groupby, x, y)
    return engine3.datatoJSON(pred_graph, x, y)

@app.route('/engine3/prediction/error')
def predictionError():
    rf_model_file = Path(engine3.RF_MODEL_PATH)
    if rf_model_file.is_file():
        print('rf_model exist so we can get prediction error')
        pred_with_error_path = Path(engine3.PREDICTION_DF_ALL_2017_PATH)
        if pred_with_error_path.is_file():
            print('pred for 2017 with all station used for pred error exists!')
            pred_df = pd.read_pickle(pred_with_error_path)
        else:
            print('generating pred for 2017 with all station used for pred')
            pred_df = engine3.get_prediction('all', '15-04-2017', '30-09-2017')

            pred_df.to_pickle(pred_with_error_path)
        
        print('getting pred error json')
        return engine3_pred_error.get_error_json(pred_df)
    else:
        return "Training not done, please come back later"

    # my_file = Path(engine3.PREDICTION_DF_PATH)
    # if my_file.is_file():
    #     print('loading Pred_df')
    #     # load the pred_df from disk
    #     pred_df = pd.read_pickle(my_file)
    #     return engine3_pred_error.get_error_json(pred_df)
    # else:
    #     return 'ERROR: prediction not ready yet'


@app.route('/engine3/testing/<station>/<startDate>/<endDate>')
def testing(station, startDate,endDate):
    startDate = str(startDate)
    endDate = str(endDate)
    path = engine3.get_testing_df(station, startDate, endDate)
    return path