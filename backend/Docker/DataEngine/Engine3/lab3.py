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
from sklearn.tree import export_graphviz
import pydot

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


print('constructing engine3')
engine3 = Engine3()


station = 6043
groupby = perMonth

startDate = '27-05-2017'
endDate = '27-09-2017'

predictions_df = engine3.get_prediction(station, startDate, endDate)


