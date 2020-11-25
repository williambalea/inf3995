import json
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.pyplot as plt2
import numpy as np
import base64
from sklearn.ensemble import RandomForestRegressor
import scipy as sps
import pickle
from pathlib import Path
import datetime

class Engine3:

    prediction_errors = []

    PREDICTION_DF_PATH = "./tempFiles/prediction_df.pkl"
    PREDICTION_DF_ALL_2017_PATH = "./tempFiles/pred_df_all_2017.pkl"
    TESTING_DF_PATH = "./tempFiles/testing_df.pkl"
    RF_MODEL_PATH = "./tempFiles/rf_model3.sav"
    CSV_PATH_TEMPERATURE = '../kaggleData/historical-hourly-weather-data/temperature.csv'
    NEW_CSV_WEATHER_PATH = '../kaggleData/historical-hourly-weather-data/weatherstats_montreal_hourly.csv'
    # CSV_PATH_WEATHER_DESC = '../kaggleData/historical-hourly-weather-data/weather_description.csv'
    CSV_PATH_WINDSPEED = '../kaggleData/historical-hourly-weather-data/wind_speed.csv'
    PRED_GRAPH_PATH = './tempFiles/predGraph.png'
    ERROR_JSON_PATH = './tempFiles/error_data_and_graph.json'
    ERROR_GRAPH_PATH = './tempFiles/errorGraph2.png'
    RF_N_ESTIMATORS = 10
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
        print('reading weather csv')
        weather = pd.read_csv(self.NEW_CSV_WEATHER_PATH, low_memory=False, dtype={'date_time_local':str,
                                                                                  'wind_speed':np.float32,
                                                                                  'temperature':np.float32})
        weather = weather.filter(items=['date_time_local','wind_speed', 'temperature'])
        weather['date_time_local'] = pd.to_datetime(weather['date_time_local'])
        weather = weather.sort_values(by = ['date_time_local'])
        # weather['date_time_local'] = pd.to_datetime(weather['date_time_local'], format='%Y-%m-%d %H:%M:%S %Z')
        return weather

    def merge_bixi_weather_df(self, bixi, weather):
        print('bixi columns')
        print(bixi.columns)
        df_complete = bixi.sort_values(by = ['start_date'])
        print('Merging bixi and weather df')
        df_complete = pd.merge_asof(df_complete, weather, left_on = 'start_date', 
                                    right_on = 'date_time_local', direction = 'nearest').drop('date_time_local',  axis=1)
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
        

    def get_testing_df(self, station, startDate, endDate):
        startDateObj = datetime.datetime.strptime(startDate, '%d-%m-%Y')
        startYear = startDateObj.year

        path_year = self.TESTING_DF_PATH
        path_year = path_year.replace('.pkl', '_' + str(startYear) + '.pkl')
        my_file = Path(path_year)
        if my_file.is_file():
            print('loading testing df', str(startYear))
            # load the pred_df from disk
            testing_df_unfiltered = pd.read_pickle(my_file)
        else:
            print('generating testing df', str(startYear))
            print('fetching bixi 2017 data')
            bixi2017 = self.get_bixi_data_year(2017)

            #changing year for testing prediction
            if str(startYear) != '2017':
                year_offset = startYear - 2017
                bixi2017['start_date'] = bixi2017['start_date'] + pd.DateOffset(years=year_offset)
            
            weather = self.get_weather_df()
            testing_df_unfiltered = self.prep_df_for_rf(bixi2017, weather)
            print(testing_df_unfiltered)
            testing_df_unfiltered.to_pickle(my_file)
        

        print(testing_df_unfiltered)
        #filtre de station
        print('station filtering...')
        if str(station) != 'all':
            testing_df_unfiltered = testing_df_unfiltered[testing_df_unfiltered.start_station_code == int(station)].copy()
        elif str(station) == 'all':
            print('TODO')


        print(testing_df_unfiltered)
        #filtre de periode
        print('filtering bixi_period... ')
        testing_df = self.period_filter(testing_df_unfiltered, startDate, endDate)

        print('station: ', station)
        print(testing_df)
        return testing_df


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
        column = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).count()['temperature']
        
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


    def get_prediction(self, station, startDate, endDate):
        print('testing---------------------') 
        test_features = self.get_testing_df(station, startDate, endDate)
        print('testing DONE---------------------') 

        print('test_feature: ', test_features)
        print('getting train/test labels')
        test_labels = np.array(test_features['num_trips'])
        test_features = test_features.drop(['num_trips'], axis=1)

        print('Testing Features Shape:', test_features.shape)
        print('Testing Labels Shape:', test_labels.shape)
        
        #get Random forest model        
        loaded_rf = self.get_random_forest_model()

        # Use the forest's predict method on the test data
        predictions = loaded_rf.predict(test_features)
        print('prediction: ', predictions)
        # Calculate the absolute errors
        errors = abs(predictions - test_labels)
        self.prediction_errors.append(errors)
        # Print out the mean absolute error (mae)
        print('Prediction:')
        print(predictions)
        print('errors:')
        print(errors)

        print('Accuracy in get_pred 1-------------------------------------------------------1')
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

        return test_features


    #####Loading or making Prediction and RandomFor Model
    def get_random_forest_model(self):
        my_file = Path(self.RF_MODEL_PATH)
        if my_file.is_file():
            print('loading model...')
            # load the model from disk
            loaded_rf = pickle.load(open(my_file, 'rb'))
            print('loading model DONE')
        else:
            print('getting training df------------------')
            train_features = self.get_traning_df()
            print('getting training df DONE---------------------') 

            print('Convert NaN values to empty string')
            nan_value = float("NaN")
            train_features.replace("", nan_value, inplace=True)
            train_features.dropna(subset = ["temperature"], inplace=True)
            print('train_feature: ', train_features)
            train_labels = np.array(train_features['num_trips'])
            train_features = train_features.drop(['num_trips'], axis=1)

            #instantiate model with some decision trees
            loaded_rf = RandomForestRegressor(n_estimators = self.RF_N_ESTIMATORS, random_state = self.RF_RANDOM_STATE, n_jobs=1)
            print('Fit calculating...')

            print(train_features.dtypes)
            print(train_features.shape)
            #removing NotANumber
            #print(train_f.isnull().sum().sum())
            count_nan_in_df = train_features.isnull().sum()
            print(count_nan_in_df)

            print('fitting...')
            loaded_rf.fit(train_features, train_labels)
            print('Fit DONE')

            print('saving model')
            # save the model to disk
            pickle.dump(loaded_rf, open(my_file, 'wb'))

        return loaded_rf

    def load_prediction_df(self, station, startDate, endDate):#load dataframe from file or generate it if desn't exist

        my_file = Path(self.PREDICTION_DF_PATH)
        if my_file.is_file():
            print('loading Pred_df')
            # load the pred_df from disk
            df = pd.read_pickle(my_file)
        else:
            print('getting pred_df')
            df = self.get_prediction(station, startDate, endDate)
            print('this is the pred df:', df)
            print('saving pred_df')
            # df.to_pickle(my_file)
        return df
        
    ##### Prediction Dataframe Filters
    def period_filter(self, df, startDate, endDate):
        print('period filtering...')
        startDateObj = datetime.datetime.strptime(startDate, '%d-%m-%Y')
        endDateObj = datetime.datetime.strptime(endDate, '%d-%m-%Y')

        minDate = datetime.datetime(startDateObj.year, 4, 15)
        maxDate = datetime.datetime(endDateObj.year, 9, 30)
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
            ##############plt.xlim
            plt.ylabel('Predictions')
            # plt.xaxis.set_minor_locator(MultipleLocator(5))
            # Create legend & Show graphic
            plt.legend()
            plt.savefig(self.PRED_GRAPH_PATH)
            # plt.show()
            print('graph generated', flush=True)
            plt.show()
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
            plt.savefig(self.PRED_GRAPH_PATH)
            # plt.show()
            print('graph generated', flush=True)
            plt.show()
            
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
        with open(self.PRED_GRAPH_PATH, "rb") as imageFile:
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
