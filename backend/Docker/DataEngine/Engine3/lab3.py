import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from engine3 import Engine3
# from engine2 import Engine2
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64
from easydict import EasyDict as edict

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000

colors = ["#006D2C", "#31A354", "#74C476"]
# print('creating enginsql')
# engine1 = Engine1()
# print('creating engine3')

stationToutes = 'all'
station6202 = 6202
station5003 = 5003
squareVic = 6043
year2014 = 2014
year2015 = 2015
year2016 = 2016
year2017 = 2017
perHour = "perHour"
perWeekDay = "perWeekDay"
perMonth = "perMonth"
perDate = "perDate"
startDate = '27/05/2017'
endDate = '27/08/2017'


print('constructing engine3')
engine3 = Engine3()

print('MONTH ******************************8')
# predictions_df = engine3.load_prediction_df()
# filtered_pred_df = engine3.filter_prediction(predictions_df, 6043, perHour, startDate, endDate)
# # print('HOUR ******************************8')
# # predictions_df = engine3.filter_prediction(6043, perHour)
# # print('WEEKDAY ******************************8')
# # predictions_df = engine3.filter_prediction(6043, perWeekDay)
# # print('DATE ******************************8')
# # predictions_df = engine3.filter_prediction(6043, perDate, startDate, endDate)

# x = engine3.get_graph_X(filtered_pred_df, perHour)
# y = engine3.get_graph_Y(filtered_pred_df)
# pred_graph = engine3.get_prediction_graph( perHour, x, y)
# forAndroid = engine3.datatoJSON(pred_graph, x, y)
# print('GG')
# print(forAndroid)
station = 5003
groupby = perMonth
# startDate = '27-05-2017'
# endDate = '27-08-2017'

startDate = '27-05-2017'
endDate = '27-08-2017'
predictions_df = engine3.load_prediction_df()
print('predictions_df: ', predictions_df)
filtered_pred_df = engine3.filter_prediction(predictions_df, station, groupby, startDate, endDate)
print('filtered_pred_df: ', filtered_pred_df)
x = engine3.get_graph_X(filtered_pred_df, groupby)
print('x: ', x)
y = engine3.get_graph_Y(filtered_pred_df)
print('y: ', y)
pred_graph = engine3.get_prediction_graph( groupby, x, y)
print(engine3.datatoJSON(pred_graph, x, y))