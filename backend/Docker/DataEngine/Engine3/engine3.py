import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64
from sklearn.ensemble import RandomForestRegressor
import scipy as sps
import pickle
from pathlib import Path

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
        temperature = pd.read_csv('../kaggleData/historical-hourly-weather-data/temperature.csv', low_memory=False, dtype={'Montreal':np.float32})
        wind_speed = pd.read_csv('../kaggleData/historical-hourly-weather-data/wind_speed.csv', low_memory=False, dtype={'Montreal':np.float32})

        print(weather_description.shape)
        print('filter only montreal')
        weather_description = weather_description.filter(items=['datetime','Montreal'])
        temperature = temperature.filter(items=['datetime','Montreal']) #temp in  Kelvin

        wind_speed = wind_speed.filter(items=['datetime','Montreal'])

        weather = (weather_description.merge(temperature, on='datetime').
                                    merge(wind_speed, on='datetime'))
        weather.columns = ['Datetime','Description', 'Temperature', 'Wind_speed']
        weather['Temperature_C'] = weather['Temperature'] - 273.15 #temp in celcius
        weather = weather.drop( ['Temperature'], axis=1)

        weather['Datetime'] = pd.to_datetime(weather['Datetime'])
        print('here -----------------------------')
        print(weather.head())


        # weather['Temparature'] = weather['Temperature_C'].astype(int)
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
        
        return self.prep_df_for_rf(big_bixi_df, weather_df)
        

    def get_testing_df(self):
        print('fetching csv data')
        bixi2017 = self.get_bixi_data_year(2017)
        weather = self.get_weather_df()
        return self.prep_df_for_rf(bixi2017, weather)

  

    def prep_df_for_rf(self, bixi_df, weather_df):
        print('merging bixi and weather')
        df = self.merge_bixi_weather_df(bixi_df, weather_df)

        print('cleaning columns test')
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
        # df_grouped = df.groupby(['year','month', 'day', 'hour']).agg('first')
        # column = df.groupby(['year','month', 'day', 'hour']).count()['Description']
        df_grouped = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).agg('first')
        column = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).count()['Description']
        
        df_grouped['num_trips']= column
        df_grouped = df_grouped.reset_index()
        print('testing 1 df col: ', df_grouped.dtypes)

        df_grouped['year'] = df_grouped['year'].astype(np.int16)
        df_grouped['month'] = df_grouped['month'].astype(np.int16)
        df_grouped['day'] = df_grouped['day'].astype(np.int16)
        df_grouped['hour'] = df_grouped['hour'].astype(np.int16)
        df_grouped['start_station_code'] = df_grouped['start_station_code'].astype(np.int16)
        df_grouped['weekday'] = df_grouped['weekday'].astype(np.int16)
        df_grouped['weekofyear'] = df_grouped['weekofyear'].astype(np.int16)
        df_grouped['num_trips'] = df_grouped['num_trips'].astype(np.int16)

        #one-hot encoding weather description
        df_grouped = pd.get_dummies(df_grouped)

        # df['year'] = df['year'].astype(np.int32)
        print('testing 2 df col: ', df_grouped.dtypes)

        return df_grouped


    def get_prediction(self):
        print('testing---------------------') 
        test_features = self.get_testing_df()

        print('traning------------------')
        train_features = self.get_traning_df()

        # Add missing  columns from/to each dataframe
        col_list = (test_features.append([train_features])).columns.tolist()
        test_features = test_features.reindex(columns = col_list,  fill_value=0)
        train_features = train_features.reindex(columns = col_list,  fill_value=0)

        print('test_feature: ', test_features)
        print('train_feature: ', train_features)

        print('getting train/test labels')
        test_labels = np.array(test_features['num_trips'])
        train_labels = np.array(train_features['num_trips'])
        test_features = test_features.drop(['num_trips'], axis=1)
        train_features = train_features.drop(['num_trips'], axis=1)

        print('Training Features Shape:', train_features.shape)
        print('Training Labels Shape:', train_labels.shape)
        print('Testing Features Shape:', test_features.shape)
        print('Testing Labels Shape:', test_labels.shape)
        #not used yet... prbly to delete
        feature_list = list(test_features.columns)
        
        #get Random forest model        
        loaded_rf = self.get_random_forest_model(train_labels, train_features)


        # Use the forest's predict method on the test data
        print('prediction...')
        predictions = loaded_rf.predict(test_features)
        # Calculate the absolute errors
        errors = abs(predictions - test_labels)
        # Print out the mean absolute error (mae)
        print('Prediction:')
        print(predictions)
        print('errors:')
        print(errors)
        print('Mean Absolute Error:', round(np.mean(errors), 2), 'departs.')

        # Calculate mean absolute percentage error (MAPE)
        mape = 100 * (errors / test_labels)# Calculate and display accuracy
        accuracy = 100 - np.mean(mape)
        print('Accuracy:', round(accuracy, 2), '%.')

        #concat test_features to predictions
        test_features.insert(len(test_features.columns), 'predictions', predictions)
        test_features.insert(len(test_features.columns), 'test_labels', test_labels)
        test_features.drop(test_features.columns.difference(['year', 'month', 'day', 'hour', 'start_station_code',
                                       'weekday', 'weekofyear', 'predictions', 'test_labels'
                                       ]), 1, inplace=True)
        print(test_features)

        return test_features

    def get_random_forest_model(self, train_l, train_f):
        my_file = Path("./finalized_model.sav")
        if my_file.is_file():
            print('loading model')
            # load the model from disk
            loaded_rf = pickle.load(open(my_file, 'rb'))
        else:
            #instantiate model with 1000 decision trees
            loaded_rf = RandomForestRegressor(n_estimators = 20, random_state = 42, n_jobs=-1)
            print('Fit calculating...')
            loaded_rf.fit(train_features, train_labels)
            print('Fit DONE')

            print('saving model')
            # save the model to disk
            pickle.dump(loaded_rf, open(filename, 'wb'))

        return loaded_rf

    #not useful yet
    def filter_prediction(self, station, groupby:
        my_file = Path("./all_pred.pkl")
        if my_file.is_file():
            print('loading Pred_df')
            # load the pred_df from disk
            df = pd.read_pickle(my_file)
        else:
            print('getting pred_df')
            df = self.get_prediction()
            print('this is the pred df:', df)
            print('saving pred_df')
            df.to_pickle(my_file)

        print('this is the pred df:')
        print(df)
        print('READY TO FILTER!!')

        #Filter wanted station
        if str(station) != 'all':
            df = df[df.start_station_code == station]

        #filter groupby
        if(groupby == "perHour"):
        elif(groupby == "perWeekDay"):
        elif(groupby == "perMonth"):
        elif(groupby == "perDate"):

    
        print('AFTER FILTER')
        print(df)
        
        return 0