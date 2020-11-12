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



# print('reading Stations_2015')
# Stations_2015 = pd.read_csv('../kaggleData/Stations_2015.csv', low_memory = False)

# print('reading od2015')



# #Import Data
print('importing bixi csv data')
# csv_file_list = ["../kaggleData/OD_2014.csv", "../kaggleData/OD_2015.csv","../kaggleData/OD_2016.csv"]
csv_file_list = ["../kaggleData/OD_2014.csv"]
list_of_dataframes = []
for filename in csv_file_list:
    list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
 
                                                           'end_station_code':str,'duration_sec':int,'is_member':int})  )
print('concatenation bixi data')
merged_df = pd.concat(list_of_dataframes)
# print('shape of merge')
# print(merged_df.shape)

print('importing weather data')
weather_description = pd.read_csv('../kaggleData/historical-hourly-weather-data/weather_description.csv', low_memory=False)
temperature = pd.read_csv('../kaggleData/historical-hourly-weather-data/temperature.csv', low_memory=False)
pressure = pd.read_csv('../kaggleData/historical-hourly-weather-data/pressure.csv', low_memory=False)
wind_speed = pd.read_csv('../kaggleData/historical-hourly-weather-data/wind_speed.csv', low_memory=False)

# convert bixi data dateTime
print('converting bixi data to datetiime')
merged_df['start_date'] = pd.to_datetime(merged_df['start_date'])
merged_df['end_date'] = pd.to_datetime(merged_df['end_date'])
merged_df[['start_date', 'end_date']].dtypes
print('bixiData to Pandas OK')


#filter and manipulate weather
print('filter only montreal')
weather_description = weather_description.filter(items=['datetime','Montreal'])
temperature = temperature.filter(items=['datetime','Montreal']) #temp in  Kelvin
pressure = pressure.filter(items=['datetime','Montreal'])
wind_speed = wind_speed.filter(items=['datetime','Montreal'])
print(weather_description.shape)

#merge data by datetime
print('merging all weather data')
weather = (weather_description.merge(temperature, on='datetime').
                              merge(pressure, on='datetime').
                              merge(wind_speed, on='datetime'))
weather.columns = ['Datetime','Description', 'Temperature', 'Pressure', 'Wind_speed']
weather['Temperature_C'] = weather['Temperature'] - 273.15 #to adjust to Celcius

print('convert weather to datetime')
weather['Datetime'] = pd.to_datetime(weather['Datetime'])

#now include weather data with bixi data based on datetime
print('df_complete 1 sorting values by startdate')
df_complete = merged_df.sort_values(by = ['start_date'])
print('df_complete 2 adding merging of weather')
df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', 
                            right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)
# print(df_complete.info())
print('all done gg')


