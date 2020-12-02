from flask import Flask
from engine3 import Engine3
from engine3_pred_error import Engine3_Pred_Error
from pathlib import Path
import pandas as pd
app = Flask(__name__)

engine3 = Engine3()
engine3_pred_error = Engine3_Pred_Error()

@app.route('/engine3')
def hello_world():
    return 'Hello World from Engin 3 :)'

@app.route('/engine3/prediction/usage/<station>/<groupby>/<startDate>/<endDate>')
def prediction_usage(station, groupby, startDate,endDate):
    startDate = str(startDate)
    endDate = str(endDate)
    predictionsDF = engine3.get_prediction(station, startDate, endDate)
    filteredPredDF = engine3.groupby_filter(predictionsDF, groupby)
    x = engine3.get_graph_X(filteredPredDF, groupby)
    y = engine3.get_graph_Y(filteredPredDF)
    predGraph = engine3.get_prediction_graph( groupby, x, y, station)
    return engine3.datatoJSON(predGraph, x, y, groupby)

@app.route('/engine3/prediction/error')
def prediction_error():
    rfModelFile = Path(engine3.RF_MODEL_PATH)
    if rfModelFile.is_file():
        print('loading saved Random Forest Model')
        errorPredDFPath = Path(engine3.PREDICTION_DF_ALL_2017_PATH)
        #if predictions dataframe for 2017 exist then load else generate it
        if errorPredDFPath.is_file():
            predDF = pd.read_pickle(errorPredDFPath)
        else:
            predDF = engine3.get_prediction('all', '15-04-2017', '30-09-2017')
            predDF.to_pickle(errorPredDFPath)
        return engine3_pred_error.get_error_json(predDF)
    else:
        return "Training not done, please come back later"
