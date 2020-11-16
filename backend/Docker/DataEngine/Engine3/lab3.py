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
import dask.dataframe as dd

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
timeHour = "perHour"
timeWeek = "perWeekDay"
timeMonth = "perMonth"



# print('reading Stations_2015')
# Stations_2015 = pd.read_csv('../kaggleData/Stations_2015.csv', low_memory = False)

# print('reading od2015')

print('constructing engine3')
engine3 = Engine3()

# #Import Data
# print('importing bixi csv data')
# csv_file_list = ["../kaggleData/OD_2014.csv","../kaggleData/OD_2015.csv", "../kaggleData/OD_2016.csv"]
# list_of_dataframes = []
# for filename in csv_file_list:
#     list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
#                                                          'end_station_code':str,'duration_sec':int,'is_member':int})  )
# list_of_dataframes = []
# list_of_dataframes = engine3.importBixiData()



#################start here#################

#get all data


# bigPandas = engine3.merge_bixi_weather_df(bigBixidf, weather_df)
# training_set = engine3.prep_training_data(bigPandas)
training_df = engine3.get_traning_df()
print(training_df)
print(training_df.columns)

# testing_df = engine3.get_testing_df()
# print(testing_df)
# print(testing_df.columns)


# print(training_set.info)
# print(training_set.dtypes)
# print(training_set.shape)

# # print(df_complete.info())
# print('all done gg')
