import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from enginSQL import EnginSQL
from engin2 import Engin2
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000

colors = ["#006D2C", "#31A354", "#74C476"]

enginsql = EnginSQL()
engin2 = Engin2()

station = 'toutes'
year = 2017

# query = "SELECT * FROM Stations"
query = "SELECT * FROM BixiRentals2014"
# query = 'SELECT startDate FROM Bixi.BixiRentals2014 WHERE str_to_date(startDate, %m/%d/%Y %H:%i).HOUR;'
df2 = enginsql.query_pd("SELECT * FROM BixiRentals2017")
engin2.getGraphHour(df2, 'toutes')
print(df2)

# 'id': 1048567, 'startDate': '6/11/2017 23:49', 'startStationCode': '6202', 'endDate': '6/12/2017 0:01', 'endStationCode': '6227', 'durationSec': 703, 'isMember': 1
#create dataframe
# notjson = enginsql.getDataUsage(year, station)
# df = Dataframe (myjson, columns= ['id', 'startDate', 'startStationCode','endDate', 'endStationCode', 'durationSec', 'isMember'])
# print(type(myjson))
# print(myjson)
# df = pd.read_json(notjson)
# print(df)
# hourCountStart = engin2.getPerHourCountStart(myjson, station)
# print(hourCountStart)
# monthCountStart = engin2.getPerMonthCountStart(df, station)
# print(monthCountStart)
# engin2.getGraphHour(df, station)

# # Get indexes where name column has value john
# indexNames = df[df['startStationCode'] != int(station)].index
# # Delete these row indexes from dataFrame
# df.drop(indexNames , inplace=True)
# # indexNames = df[df['endStationCode'] != int(station)].index
# # indexNames = df[(df['startStationCode'] == int(station)) & (df['endStationCode'] == int(station))].index
# print(indexNames)

# print(df)





# df.to_csv('bothReal.csv', index=False) 


