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
        weather['Temperature_C'] = weather['Temperature'] - 273.15 #Añadimos columna de temperatura en grados centígrados

        weather['Datetime'] = pd.to_datetime(weather['Datetime'])
        weather = weather.sort_values(by = ['Datetime'])
        print('Weather_df done')
        print(weather.shape)
        return weather

    def merge_bixi_weather_df(self, bixi, weather):
        print('df_complete 1 sorting values by startdate')
        df_complete = bixi.sort_values(by = ['start_date'])
        print('df_complete 2 adding merging of weather')
        df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', 
                                    right_on = 'Datetime', direction = 'nearest').drop('Datetime',  axis=1)
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
                        'is_member', axis=1)
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
        df.insert(5, "weekday", df['start_date'].dt.isocalendar().week)
        df.insert(6, "weekofyear", df['start_date'].dt.weekofyear)
        df = df.drop( ['start_date',
                                           'end_date',
                                           'end_station_code',
                                           'duration_sec',
                                           'is_member'], axis=1)
        # Group by hour
        df_grouped = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).agg('first')
        column = df.groupby(['year','month', 'day', 'hour']).count()['Description']
        
        df_grouped['num_trips']= column
        df_grouped = df_grouped.reset_index()
        
        return df




#TODELETE
    # def importBixiData(self):
    #     # csv_file_list = ["../kaggleData/OD_2014.csv", "../kaggleData/OD_2015.csv","../kaggleData/OD_2016.csv"]sv"]
    #     list_of_dataframes = []
    #     for filename in self.csv_file_list:
    #         list_of_dataframes.append(pd.read_csv(filename, dtype={'start_date':str,'start_station_code':int,'end_date':str,
    #                                                             'end_station_code':str,'duration_sec':int,'is_member':int})  )
    #     return list_of_dataframes

#TODELETE
    # def convertBixiDataToDatetime(self, list_df):
    #     list_df[0]['start_date'] = pd.to_datetime(list_df[0]['start_date'])
    #     list_df[0]['end_date'] = pd.to_datetime(list_df[0]['end_date'])
    #     list_df[1]['start_date'] = pd.to_datetime(list_df[1]['start_date'])
    #     list_df[1]['end_date'] = pd.to_datetime(list_df[1]['end_date'])
    #     # list_df[2]['start_date'] = pd.to_datetime(list_df[2]['start_date'])
    #     # list_df[2]['end_date'] = pd.to_datetime(list_df[2]['end_date'])
    #     return list_df


#TODELETE
    # def get_bixi_data2014to2016(self):
    #     # #Import Data
    #     print('importing bixi csv data')
    #     bixidf2014 = pd.read_csv("../kaggleData/OD_2014.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
    #                                                             'end_station_code':str,'duration_sec':int,'is_member':int})
    #     bixidf2015 = pd.read_csv("../kaggleData/OD_2015.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
    #                                                             'end_station_code':str,'duration_sec':int,'is_member':int})
    #     bixidf2016 = pd.read_csv("../kaggleData/OD_2016.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
    #                                                     'end_station_code':str,'duration_sec':int,'is_member':int})

    #     print('converting bixi data to datetime')
    #     bixidf2014['start_date'] = pd.to_datetime(bixidf2014['start_date'])
    #     bixidf2014['end_date'] = pd.to_datetime(bixidf2014['end_date'])
    #     bixidf2015['start_date'] = pd.to_datetime(bixidf2015['start_date'])
    #     bixidf2015['end_date'] = pd.to_datetime(bixidf2015['end_date'])
    #     bixidf2016['start_date'] = pd.to_datetime(bixidf2016['start_date'])
    #     bixidf2016['end_date'] = pd.to_datetime(bixidf2016['end_date'])

    #     print('concatenation')
    #     list_of_pandas = []
    #     list_of_pandas.append(bixidf2014)
    #     list_of_pandas.append(bixidf2015)
    #     list_of_pandas.append(bixidf2016)

    #     big_bixi_df = pd.concat(list_of_pandas)

        
    #     print('merged_df')
    #     print(big_bixi_df)
    #     print(big_bixi_df.shape)
    #     return big_bixi_df
     


    def prep_training_data(self, df):
        
        df['start_date'] = pd.to_datetime(df['start_date'])
        df['weekday'] = df.start_date.dt.dayofweek
        df['hour'] = df.start_date.dt.hour
        df['num_week'] = df.start_date.dt.weekofyear
        df = df.drop(['start_date','start_station_code', 'end_date', 'end_station_code', 'duration_sec', 'is_member', 'Temperature_C'], axis=1)
        
        # Group by hour
        training_df = df.groupby(['num_week','weekday', 'hour']).agg('first')
        columna = df.groupby(['num_week','weekday', 'hour']).count()['Description']
        training_df['num_trips']= columna
        training_df=training_df.reset_index(level=[0,1,2])

        return training_df



    def testSeparateTime(self):
        # #Import Data
        print('importing bixi csv data')
        bixidf2014 = pd.read_csv("../kaggleData/OD_2014.csv", dtype={'start_date':str,'start_station_code':int,'end_date':str,
                                                                'end_station_code':str,'duration_sec':int,'is_member':int})
        
        print('converting bixi data to datetime')
        bixidf2014['start_date'] = pd.to_datetime(bixidf2014['start_date'])
        bixidf2014['end_date'] = pd.to_datetime(bixidf2014['end_date'])
        bixidf2014.insert(1, "start_month", bixidf2014['start_date'].dt.month)

        return bixidf2014
