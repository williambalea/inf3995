import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64
from sklearn.ensemble import RandomForestRegressor
import scipy as sps

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
        pressure = pd.read_csv('../kaggleData/historical-hourly-weather-data/pressure.csv', low_memory=False, dtype={'Montreal':np.float32})
        wind_speed = pd.read_csv('../kaggleData/historical-hourly-weather-data/wind_speed.csv', low_memory=False, dtype={'Montreal':np.float32})

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
                        'Unnamed: 0','Pressure'], axis=1)
                        # 'Pressure', 'Temperature_C'

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

        # df['year'] = df['year'].astype(np.int32)
        print('testing 2 df col: ', df_grouped.dtypes)

        return df_grouped


    def random_forest_algo(self):
        print('testing---------------------') 
        test_features = self.get_testing_df()

        #one-hot encoding weather description
        test_features = pd.get_dummies(test_features)
        # , columns=[ 'Description_broken clouds',
        #                                                     'Description_few clouds', 'Description_fog', 'Description_haze',
        #                                                     'Description_heavy intensity rain',
        #                                                     'Description_heavy intensity shower rain',
        #                                                     'Description_light intensity drizzle',
        #                                                     'Description_light intensity shower rain', 'Description_light rain',
        #                                                     'Description_light shower snow', 'Description_light snow',
        #                                                     'Description_mist', 'Description_moderate rain',
        #                                                     'Description_overcast clouds', 'Description_proximity shower rain',
        #                                                     'Description_proximity thunderstorm', 'Description_scattered clouds',
        #                                                     'Description_shower rain', 'Description_sky is clear',
        #                                                     'Description_smoke', 'Description_thunderstorm',
        #                                                     'Description_thunderstorm with heavy rain',
        #                                                     'Description_thunderstorm with light rain',
        #                                                     'Description_thunderstorm with rain', 'Description_very heavy rain',
        #                                                     'Description_light intensity drizzle rain' ])


        print('traning------------------')
        train_features = self.get_traning_df()

        print(train_features)
        #one-hot encoding weather description
        train_features = pd.get_dummies(train_features)


        # Now add the missing different columns to each dataframe
        col_list = (test_features.append([train_features])).columns.tolist()
        test_features = test_features.reindex(columns = col_list,  fill_value=0)
        train_features = train_features.reindex(columns = col_list,  fill_value=0)

        # col_list = list(set().union(dfA.columns, dfB.columns, dfC.columns))

        print(test_features)
        print(train_features)

        print('testing df col before : ', test_features.columns)
        test_labels = np.array(test_features['num_trips'])
        train_labels = np.array(train_features['num_trips'])


        test_features = test_features.drop(['num_trips'], axis=1)
        train_features = train_features.drop(['num_trips'], axis=1)

        print('Training Features Shape:', train_features.shape)
        print('Training Labels Shape:', train_labels.shape)
        print('Testing Features Shape:', test_features.shape)
        print('Testing Labels Shape:', test_labels.shape)

        feature_list = list(test_features.columns)
        
        ## not gonna do for now
        # # The baseline predictions are the historical averages
        # baseline_preds = test_features[:, feature_list.index('num_trips')]
        # # Baseline errors, and display average baseline error
        # baseline_errors = abs(baseline_preds - test_labels)
        # print('Average baseline error: ', round(np.mean(baseline_errors), 2))


        #instantiate model with 1000 decision trees
        rf = RandomForestRegressor(n_estimators = 20, random_state = 42, n_jobs=-1)


        print(train_features.dtypes)
        # train_features.dropna()
        # print(train_features)
        #Train the model on training data
        print('Fit calculating...')

        print('look here --------------------------------------------------------------')
        # dense_df = sps.sparse.csr_matrix(train_features.values)
        # dense_df = sps.coo_matrix((train_features.freq, (train_features.index.labels[0], train_features.index.labels[1])))
        # print(dense_df)
        # sps.sparse.csr_matrix(train_features.values)
        print('look here --------------------------------------------------------------')

        rf.fit(train_features, train_labels)
        print('Fit DONE')


        print('test_features col before : ', test_features.columns)
        print('train_features col before : ', train_features.columns)

        # Use the forest's predict method on the test data
        print('prediction...')
        predictions = rf.predict(test_features)
        # Calculate the absolute errors
        errors = abs(predictions - test_labels)
        # Print out the mean absolute error (mae)
        print('Mean Absolute Error:', round(np.mean(errors), 2), 'degrees.')
        return 0