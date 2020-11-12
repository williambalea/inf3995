import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
# from Engine3.engine3 import Engine3
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
# engine3 = Engine3()

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



print('reading Stations_2015')
# Stations_2014 = pd.read_csv('../kaggleData/Stations_2014.csv', low_memory = False)
Stations_2015 = pd.read_csv('../kaggleData/Stations_2015.csv', low_memory = False)
Stations_2016 = pd.read_csv('../kaggleData/Stations_2016.csv', low_memory = False)
# Stations_2017 = pd.read_csv('../kaggleData/Stations_2017.csv', low_memory = False)

print('reading od2015')
#Import Data
# OD_2014 = pd.read_csv('../kaggleData/OD_2014.csv', low_memory=False, index_col=0)
# OD_2015 = pd.read_csv('../kaggleData/OD_2015.csv',  dtype={'start_date':str,'start_station_code':int,'end_date':str,
#                                                            'end_station_code':str,'duration_sec':int,'is_member':int})
# OD_2016 = pd.read_csv('../kaggleData/OD_2016.csv',  dtype={'start_date':str,'start_station_code':int,'end_date':str,
#                                                            'end_station_code':str,'duration_sec':int,'is_member':int})
# OD_2017 = pd.read_csv('../kaggleData/OD_2017.csv', low_memory=False, index_col=0)


csv_file_list = ["../kaggleData/OD_2014.csv", "../kaggleData/OD_2015.csv","../kaggleData/OD_2016.csv"]
csv_file_list = ["../kaggleData/OD_2014.csv"]
list_of_dataframes = []
for filename in csv_file_list:
    list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                           'end_station_code':str,'duration_sec':int,'is_member':int})  )
merged_df = pd.concat(list_of_dataframes)
print('shape')
print(merged_df.shape)

#import weather data
print('reading temperature')
temperature = pd.read_csv('../kaggleData/historical-hourly-weather-data/temperature.csv', low_memory=False)
pressure = pd.read_csv('../kaggleData/historical-hourly-weather-data/pressure.csv', low_memory=False)
weather_description = pd.read_csv('../kaggleData/historical-hourly-weather-data/weather_description.csv', low_memory=False)
wind_speed = pd.read_csv('../kaggleData/historical-hourly-weather-data/wind_speed.csv', low_memory=False)

# print(OD_2015.shape)
# print(temperature.shape)
# print(pressure.shape)

# #Changer type to Datetime
merged_df['start_date'] = pd.to_datetime(merged_df['start_date'])
merged_df['end_date'] = pd.to_datetime(merged_df['end_date'])
# OD_2016['start_date'] = pd.to_datetime(OD_2016['start_date'])
# OD_2016['end_date'] = pd.to_datetime(OD_2016['end_date'])
# print(OD_2015[['start_date', 'end_date']].dtypes)

#filter and manipulate weather
print(weather_description.head())
weather_description = weather_description.filter(items=['datetime','Montreal'])
print('after weather filter')
print(weather_description.head())
temperature = temperature.filter(items=['datetime','Montreal'])
pressure = pressure.filter(items=['datetime','Montreal'])
wind_speed = wind_speed.filter(items=['datetime','Montreal'])
#weather dataframe merge
weather = (weather_description.merge(temperature, on='datetime').
                              merge(pressure, on='datetime').
                              merge(wind_speed, on='datetime'))
weather.columns = ['Datetime','Description', 'Temperature', 'Pressure', 'Wind_speed']
weather['Temperature_C'] = weather['Temperature'] - 273.15 
#weather datetime Type
weather['Datetime'] = pd.to_datetime(weather['Datetime'])
print('weather head')
print(weather.head())


print('types')
print(weather.dtypes)
print(merged_df.dtypes)
#include weather data in the bike usage data
print('merging df complete')
df_complete = merged_df.sort_values(by = ['start_date'])
print('first sort')
df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)
print('done')
print(df_complete.head())