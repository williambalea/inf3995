import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64

class Engine3:

    # csv_file_list = ["../kaggleData/OD_2014.csv","../kaggleData/OD_2015.csv", "../kaggleData/OD_2016.csv"]

    csv_file_list = ["../kaggleData/OD_2015.csv", "../kaggleData/OD_2016.csv"]

    def __init__(self):
        return None 
    
    def importBixiData(self):
        # csv_file_list = ["../kaggleData/OD_2014.csv", "../kaggleData/OD_2015.csv","../kaggleData/OD_2016.csv"]sv"]
        list_of_dataframes = []
        for filename in self.csv_file_list:
            list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                                'end_station_code':str,'duration_sec':int,'is_member':int})  )
        return list_of_dataframes

    def convertBixiDataToDatetime(self, list_df):
        list_df[0]['start_date'] = pd.to_datetime(list_df[0]['start_date'])
        list_df[0]['end_date'] = pd.to_datetime(list_df[0]['end_date'])
        list_df[1]['start_date'] = pd.to_datetime(list_df[1]['start_date'])
        list_df[1]['end_date'] = pd.to_datetime(list_df[1]['end_date'])
        # list_df[2]['start_date'] = pd.to_datetime(list_df[2]['start_date'])
        # list_df[2]['end_date'] = pd.to_datetime(list_df[2]['end_date'])
        return list_df

    def get_weather_df(self):
        print('importing weather data')
        weather_description = pd.read_csv('../kaggleData/historical-hourly-weather-data/weather_description.csv', low_memory=False)
        temperature = pd.read_csv('../kaggleData/historical-hourly-weather-data/temperature.csv', low_memory=False)
        pressure = pd.read_csv('../kaggleData/historical-hourly-weather-data/pressure.csv', low_memory=False)
        wind_speed = pd.read_csv('../kaggleData/historical-hourly-weather-data/wind_speed.csv', low_memory=False)

        print(weather_description.shape)
        print('filter only montreal')
        weather_description = weather_description.filter(items=['datetime','Montreal'])
        temperature = temperature.filter(items=['datetime','Montreal']) #temp in  Kelvin
        pressure = pressure.filter(items=['datetime','Montreal'])
        wind_speed = wind_speed.filter(items=['datetime','Montreal'])

        weather = (weather_description.merge(temperature, on='datetime').
                                    merge(pressure, on='datetime').
                                    merge(wind_speed, on='datetime'))
        weather.columns = ['Datetime','Description', 'Temperature', 'Pressure', 'Wind_speed']
        weather['Temperature_C'] = weather['Temperature'] - 273.15 #Añadimos columna de temperatura en grados centígrados

        weather['Datetime'] = pd.to_datetime(weather['Datetime'])
        weather = weather.sort_values(by = ['Datetime'])
        print('Weather_df done')
        print(weather.shape)
        return weather

