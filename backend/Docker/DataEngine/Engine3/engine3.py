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
import datetime

class Engine3:

    PREDICTION_DF_PATH = "./all_pred6.pkl"
    RF_MODEL_PATH = "./finalized_model.sav"
    CSV_PATH_TEMPERATURE = '../kaggleData/historical-hourly-weather-data/temperature.csv'
    CSV_PATH_WEATHER_DESC = '../kaggleData/historical-hourly-weather-data/weather_description.csv'
    CSV_PATH_WINDSPEED = '../kaggleData/historical-hourly-weather-data/wind_speed.csv'
    RF_N_ESTIMATORS = 30
    RF_RANDOM_STATE = 42

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
        weather_description = pd.read_csv(self.CSV_PATH_WEATHER_DESC, low_memory=False)
        temperature = pd.read_csv(self.CSV_PATH_TEMPERATURE, low_memory=False, dtype={'Montreal':np.float32})
        wind_speed = pd.read_csv(self.CSV_PATH_WINDSPEED, low_memory=False, dtype={'Montreal':np.float32})

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
        print(test_features)
        predictions = loaded_rf.predict(test_features)
        print('prediction: ', predictions)
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


    ### ##Loading or making Prediction and RandomFor Model
    def get_random_forest_model(self, train_l, train_f):
        my_file = Path(self.RF_MODEL_PATH)
        if my_file.is_file():
            print('loading model...')
            # load the model from disk
            loaded_rf = pickle.load(open(my_file, 'rb'))
            print('loading model DONE')
        else:
            #instantiate model with 1000 decision trees
            loaded_rf = RandomForestRegressor(n_estimators = self.RF_N_ESTIMATORS, random_state = self.RF_RANDOM_STATE, n_jobs=-1)
            print('Fit calculating...')
            loaded_rf.fit(train_f, train_l)
            print('Fit DONE')

            print('saving model')
            # save the model to disk
            pickle.dump(loaded_rf, open(my_file, 'wb'))

        return loaded_rf

    def load_prediction_df(self):#load dataframe from file or generate it if desn't exist
        my_file = Path(self.PREDICTION_DF_PATH)
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
        return df
        
    ##### Prediction Dataframe Filters
    def filter_prediction(self, df,  station, groupby, startDate, endDate):
        print('filtering...')
        #Filter station
        print('station filter')
        if str(station) != 'all':
            df = df[df.start_station_code == int(station)].copy()
        elif str(station) == 'all':
            print('TODO')
            #groupby jusqua l<heure
            #Filter Period


        print('period filter')
        df = self.period_filter(df, startDate, endDate)
        #Filter groupby
        print('groupby filter')      
        df = self.groupby_filter(df, groupby)

        print('filtering DONE')
        return df


    def period_filter(self, df, startDate, endDate):
        print('period filtering...')
        startDateObj = datetime.datetime.strptime(startDate, '%d-%m-%Y')
        endDateObj = datetime.datetime.strptime(endDate, '%d-%m-%Y')

        minDate = datetime.datetime(2017, 4, 15)
        maxDate = datetime.datetime(2017, 9, 30)
        if startDateObj < minDate:
            startDateObj = minDate
        if endDateObj > maxDate:
            endDateObj = maxDate

        startYear = startDateObj.year
        startMonth = startDateObj.month
        startDay = startDateObj.day
        endYear = endDateObj.year
        endMonth = endDateObj.month
        endDay = endDateObj.day

        # Get names of indexes for which column Age has value 30
        indexNames = df[ df['month'] < startMonth ].index
        print(indexNames)
        df.drop(indexNames , inplace=True)
        # print('indexNames: ', indexNames)
        indexNames = (df[ (df['month'] == startMonth) & (df['day'] < startDay)].index)
        df.drop(indexNames , inplace=True)
        indexNames = df[ df['month'] > endMonth ].index
        df.drop(indexNames , inplace=True)
        # print('indexNames: ', indexNames)
        indexNames = (df[ (df['month'] == endMonth) & (df['day'] > endDay)].index)
        df.drop(indexNames , inplace=True)

        print('period filtering DONE')
        return df

    def groupby_filter(self, df, groupby):
        print('groupby filtering...')
        #filter groupby
        if(groupby == "perHour"):
            print('filtering perHour')
            df = df.groupby(['hour']).agg({'predictions': 'sum', 'test_labels': 'sum'})

        elif(groupby == "perWeekDay"):
            print('filtering perWeekDay')
            df = df.groupby(['weekday']).agg({'predictions': 'sum', 'test_labels': 'sum'})

        elif(groupby == "perMonth"):
            print('filtering perMonth')
            df = df.groupby(['month']).agg({'predictions': 'sum', 'test_labels': 'sum'})

        elif(groupby == "perDate"):
            print('filtering perDate')
            df = df.groupby(['month', 'day']).agg({'predictions': 'sum', 'test_labels': 'sum'})
            
        print('groupby filtering DONE')
        return df


    #####Graph generation and convertion for HTTP to Android
    def get_prediction_graph(self, groupby, x, y):
        
        print('x: ', x)
        print('y: ', y)

        # Make the plot
        # set width of bar
        if groupby == "perDate":
            barWidth = 0.25
            plt.clf()
            plt.scatter(x, y, color='#D52B1E',  edgecolor='white', label='Predictions')
            # Add xticks on the middle of the group bars
            ('adding xticks')
            plt.xlabel('GroupBy')
            plt.ylabel('Predictions')
            # plt.xaxis.set_minor_locator(MultipleLocator(5))
            # Create legend & Show graphic
            plt.legend()
            plt.savefig('bar3.png')
            # plt.show()
            print('graph generated', flush=True)
            # plt.show()
        elif groupby != "perDate":
            barWidth = 0.25
            plt.clf()
            plt.bar(x, y, color='#D52B1E', width=barWidth, edgecolor='white', label='Predictions')
            # Add xticks on the middle of the group bars
            ('adding xticks')
            plt.xlabel('GroupBy', fontweight='bold')
            plt.ylabel('Predictions', fontweight='bold')
            plt.tight_layout()
            # Create legend & Show graphic
            plt.legend()
            plt.savefig('bar3.png')
            # plt.show()
            print('graph generated', flush=True)
            # plt.show()
            
        return plt
    
    def get_graph_X(self, df, groupby):
        print('getting Xaxis data')
        xAxisDate = []
        if groupby == "perDate":
            for i in range(len(df.index.values)):
                xAxisDate.append(str(df.index.values[i][0]) + "-" + str(df.index.values[i][1]))
                xAxis = pd.Series(xAxisDate)
        else:
            xAxis = df.index.values
        
        return xAxis

    def get_graph_Y(self, df):
        print('getting Yaxis data')
        yAxis = df['predictions'].values
        return yAxis


    def toBase64(self):
        print('toBase64()', flush=True)
        with open("bar3.png", "rb") as imageFile:
            strg = base64.b64encode(imageFile.read()).decode('utf-8')
            while strg[-1] == '=':
                strg = strg[:-1]           
            # print(strg)
        return strg

    def datatoJSON(self, graph, x, y):
        print('datatoJSON()', flush=True)
        graphString = self.toBase64()

        myJson =  '{  "data":{ "time":[], "predictions":[] }, "graph":[] }'

        o = json.loads(myJson)
        o["data"]["time"] = x[0:len(x)].tolist()
        o["data"]["predictions"] = y.tolist()
        o["graph"] = graphString
        
        # print('Label used: ', flush=True)
        # print(y, flush=True)
        return json.dumps(o)