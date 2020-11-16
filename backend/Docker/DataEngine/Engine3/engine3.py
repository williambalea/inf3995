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
    
    def get_bixi_data_year(self, year):
        path = "../kaggleData/OD_"
        path += str(year)
        path += ".csv"
        bixidf = pd.read_csv(path, dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                        'end_station_code':str,'duration_sec':int,'is_member':int})
        bixidf['start_date'] = pd.to_datetime(bixidf['start_date'])
        return bixidf


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
        weather['Temperature_C'] = weather['Temperature'] - 273.15 #temp in celcius
        weather = weather.drop( ['Temperature'], axis=1)

        weather['Datetime'] = pd.to_datetime(weather['Datetime'])
        weather = weather.sort_values(by = ['Datetime'])
        print('Weather_df done')
        print(weather.shape)
        return weather

    def merge_bixi_weather_df(self, bixi, weather):
        print('bixi columns')
        print(bixi.columns)
        print('df_complete 1 sorting values by startdate')
        df_complete = bixi.sort_values(by = ['start_date'])
        print('df_complete 2 adding merging of weather')
        df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', 
                                    right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)
        print(df_complete.columns)
        return df_complete

    def get_traning_df(self):
        print('fetching csv data')
        bixi2016 = self.get_bixi_data_year(2016)
        bixi2015 = self.get_bixi_data_year(2015)
        bixi2014 = self.get_bixi_data_year(2014)
        weather_df = self.get_weather_df()

        print('concatenation')
        list_of_pandas = []
        list_of_pandas.append(bixi2016)
        list_of_pandas.append(bixi2015)
        list_of_pandas.append(bixi2014)

        big_bixi_df = pd.concat(list_of_pandas)
 
        print('merging bixi and weather')
        df = self.merge_bixi_weather_df(big_bixi_df, weather_df)

        print(df.columns)
        print('cleaning columns')
        df.insert(1, "year", df['start_date'].dt.year)
        df.insert(2, "month", df['start_date'].dt.month)
        df.insert(3, "day", df['start_date'].dt.day)
        df.insert(4, "hour", df['start_date'].dt.hour)
        df.insert(5, "weekday", df['start_date'].dt.weekday)
        df.insert(6, "weekofyear", df['start_date'].dt.isocalendar().week)
        df = df.drop( ['start_date',
                        'end_date',
                        'end_station_code',
                        'duration_sec',
                        'is_member', 
                        'Unnamed: 0'], axis=1)
        # Group by hour
        df_grouped = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).agg('first')
        column = df.groupby(['year','month', 'day', 'hour']).count()['Description']
        
        df_grouped['num_trips']= column
        df_grouped = df_grouped.reset_index()
        return df_grouped

    def get_testing_df(self):
        print('fetching csv data')
        bixi2017 = self.get_bixi_data_year(2017)
        weather = self.get_weather_df()

        print('merging bixi and weather')
        df = self.merge_bixi_weather_df(bixi2017, weather)

        print('cleaning columns')
        df.insert(1, "year", df['start_date'].dt.year)
        df.insert(2, "month", df['start_date'].dt.month)
        df.insert(3, "day", df['start_date'].dt.day)
        df.insert(4, "hour", df['start_date'].dt.hour)
        df.insert(5, "weekday", df['start_date'].dt.weekday)
        df.insert(6, "weekofyear", df['start_date'].dt.isocalendar().week)
        df = df.drop( ['start_date',
                        'end_date',
                        'end_station_code',
                        'duration_sec',
                        'is_member', 
                        'Unnamed: 0'], axis=1)
        # Group by hour
        df_grouped = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).agg('first')
        column = df.groupby(['year','month', 'day', 'hour']).count()['Description']
        
        df_grouped['num_trips']= column
        df_grouped = df_grouped.reset_index()
        return df_grouped

    def random_forest_algo(self):
        test_features = self.get_testing_df()
        train_features = self.get_traning_df()

        test_labels = np.array(test_features['num_trips'])
        train_labels = np.array(train_features['num_trips'])

        test_features = test_features.drop('num_trips')
        train_features = train_features.drop('num_trips')

        feature_list = list(test_features.columns)

