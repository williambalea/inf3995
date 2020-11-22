from flask import Flask
from engine3 import Engine3
app = Flask(__name__)

engine3 = Engine3()
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

    predictions_df = engine3.load_prediction_df()
    print('predictions_df: ', predictions_df)

    filtered_pred_df = engine3.filter_prediction(predictions_df, station, groupby, startDate, endDate)
    print('filtered_pred_df: ', filtered_pred_df)
    
    print('44444444444444444444444444444444444444444444444444444444444444444444444444')
    print(filtered_pred_df)
    x = engine3.get_graph_X(filtered_pred_df, groupby)
    print('x: ', x)

    y = engine3.get_graph_Y(filtered_pred_df)
    print('y: ', y)

    pred_graph = engine3.get_prediction_graph( groupby, x, y)
    
    return engine3.datatoJSON(pred_graph, x, y)