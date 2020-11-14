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
timeHour = "perHour"
timeWeek = "perWeekDay"
timeMonth = "perMonth"



# print('reading Stations_2015')
# Stations_2015 = pd.read_csv('../kaggleData/Stations_2015.csv', low_memory = False)

# print('reading od2015')

print('constructing engine3')
engine3 = Engine3()

# #Import Data
print('importing bixi csv data')
# csv_file_list = ["../kaggleData/OD_2014.csv", "../kaggleData/OD_2015.csv","../kaggleData/OD_2016.csv"]
bixidf2014 = pd.read_csv("../kaggleData/OD_2014.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                        'end_station_code':str,'duration_sec':int,'is_member':int})
bixidf2015 = pd.read_csv("../kaggleData/OD_2015.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                        'end_station_code':str,'duration_sec':int,'is_member':int})
bixidf2016 = pd.read_csv("../kaggleData/OD_2016.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                        'end_station_code':str,'duration_sec':int,'is_member':int})
# csv_file_list = ["../kaggleData/OD_2014.csv","../kaggleData/OD_2015.csv", "../kaggleData/OD_2016.csv"]
# list_of_dataframes = []
# for filename in csv_file_list:
#     list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
#                                                          'end_station_code':str,'duration_sec':int,'is_member':int})  )
# list_of_dataframes = []
# list_of_dataframes = engine3.importBixiData()

print('concatenation bixi data')
# merged_df = pd.concat(list_of_dataframes)
# print(list_of_dataframes[0].shape)
# print(list_of_dataframes[1].shape)
# print(list_of_dataframes[2].shape)
# print('shape of merge')
# print(merged_df.shape)


# convert bixi data dateTime
# print('converting bixi data to datetiime')
# merged_df['start_date'] = pd.to_datetime(merged_df['start_date'])
# merged_df['end_date'] = pd.to_datetime(merged_df['end_date'])
# merged_df[['start_date', 'end_date']].dtypes
# print('bixiData to Pandas OK' )
print('converting bixi data to datetiime')
bixidf2014['start_date'] = pd.to_datetime(bixidf2014['start_date'])
bixidf2014['end_date'] = pd.to_datetime(bixidf2014['end_date'])
bixidf2015['start_date'] = pd.to_datetime(bixidf2015['start_date'])
bixidf2015['end_date'] = pd.to_datetime(bixidf2015['end_date'])
bixidf2016['start_date'] = pd.to_datetime(bixidf2016['start_date'])
bixidf2016['end_date'] = pd.to_datetime(bixidf2016['end_date'])
# list_of_dataframes[0][['start_date', 'end_date']].dtypes
# list_of_dataframes = engine3.convertBixiDataToDatetime(list_of_dataframes)
print('bixiData to Pandas OK')


weather_df = engine3.get_weather_df()

# #now include weather data with bixi data based on datetime
# print('df_complete 1 sorting values by startdate')
# df_complete = merged_df.sort_values(by = ['start_date'])
# print('df_complete 2 adding merging of weather')
# df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', 
#                             right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

bixidf2014

print('concatenation')
list_of_pandas = []

# print('df_complete 1.1 sorting values by startdate')
# df_complete1 = list_of_dataframes[0].sort_values(by = ['start_date'])
# print('df_complete 1.2 adding merging of weather')
# df_complete1 = pd.merge_asof(df_complete1, weather, left_on = 'start_date', 
#                             right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

print('bixi2014 size')
print(bixidf2014.shape)
print('bixi2015 size')
print(bixidf2015.shape)
print('bixi2016 size')
print(bixidf2016.shape)

list_of_pandas.append(bixidf2014)
list_of_pandas.append(bixidf2015)
list_of_pandas.append(bixidf2016)

merged_df = pd.concat(list_of_pandas)
print('merged_df')
print(merged_df)
print(merged_df.shape)

print('df_complete 1.1 sorting values by startdate')
df_complete = merged_df.sort_values(by = ['start_date'])
print('df_complete 1.2 adding merging of weather')
df_complete = pd.merge_asof(df_complete, weather_df, left_on = 'start_date', 
                            right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

# list_of_pandas.append(df_complete1)

# print('df_complete 2.1 sorting values by startdate')
# df_complete2 = bixidf2015.sort_values(by = ['start_date'])
# print('df_complete 2.2 adding merging of weather')
# df_complete2 = pd.merge_asof(df_complete2, weather_df, left_on = 'start_date', 
#                             right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

# list_of_pandas.append(df_complete2)

# print('df_complete 3.1 sorting values by startdate')
# df_complete3 = bixidf2015.sort_values(by = ['start_date'])
# print('df_complete 3.2 adding merging of weather')
# df_complete3 = pd.merge_asof(df_complete3, weather_df, left_on = 'start_date', 
#                             right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

# list_of_pandas.append(df_complete3)

# print('df_complete 3.1 sorting values by startdate')
# df_complete3 = list_of_dataframes[2].sort_values(by = ['start_date'])
# print('df_complete 3.2 adding merging of weather')
# df_complete3 = pd.merge_asof(df_complete1, weather, left_on = 'start_date', 
#                             right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)

# list_of_pandas.append(df_complete3)

# print('concatenation')
# list_of_pandas = []
# # print('appending')
# list_of_pandas.append(df_complete1)
# list_of_pandas.append(df_complete2)
# list_of_pandas.append(df_complete3)
# # print('merging')
# merged_df = pd.concat(list_of_pandas)
print(df_complete.info)
print(df_complete.dtypes)
# print(df_complete.info())
print('all done gg')


