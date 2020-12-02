import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import base64
from sklearn.ensemble import RandomForestRegressor
import pickle
from pathlib import Path
import datetime
from sklearn.tree import export_graphviz
import pydot

class Engine3:

    prediction_errors = []

    PREDICTION_DF_PATH = "./tempFiles/prediction_df.pkl"
    PREDICTION_DF_ALL_2017_PATH = "./tempFiles/pred_df_all_2017.pkl"
    TESTING_DF_PATH = "./tempFiles/testingDF.pkl"
    PRED_GRAPH_PATH = './tempFiles/predGraph.png'
    NEW_CSV_WEATHER_PATH = '../kaggleData/historical-hourly-weather-data/weatherstats_montreal_hourly.csv'
    ERROR_JSON_PATH = './tempFiles/error_data_and_graph.json'
    ERROR_GRAPH_PATH = './tempFiles/errorGraph2.png'
    RF_MODEL_PATH = "./tempFiles/rf_model6.sav"
    RF_N_ESTIMATORS = 6
    RF_MAX_DEPTH = 4
    RF_RANDOM_STATE = 42
    RF_N_JOBS = 1
    PER_HOUR_LABEL = ['0h', '1h','2h', '3h', '4h', '5h', '6h','7h','8h','9h','10h','11h',
                      '12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h']
    PER_MONTH_LABEL = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ]
    COMPLETE_MONTH_LABEL = ['January', 'February', 'March', 'April','May', 'June',
                            'July', 'August', 'September', 'October', 'November', 'December']
    PER_WEEKDAY_LABEL = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    COMPLETE_WEEKDAY_LABEL = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    GRAPH_LABEL_ROTATION = '45'
    GRAPH_COLOR = '#1E00FF'
    GRAPH_LABEL_STEP = 10
    


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
        weather = pd.read_csv(self.NEW_CSV_WEATHER_PATH, low_memory=False, dtype={'date_time_local':str,
                                                                                  'wind_speed':np.float32,
                                                                                  'temperature':np.float32})
        weather = weather.filter(items=['date_time_local','wind_speed', 'temperature'])
        weather['date_time_local'] = pd.to_datetime(weather['date_time_local'])
        weather = weather.sort_values(by = ['date_time_local'])
        return weather

    def merge_bixi_weather_df(self, bixi, weather):
        completeDF = bixi.sort_values(by = ['start_date'])
        completeDF = pd.merge_asof(completeDF, weather, left_on = 'start_date', 
                                    right_on = 'date_time_local', direction = 'nearest').drop('date_time_local',  axis=1)
        return completeDF

    def get_traning_df(self):
        print('getting training dataframe...')
        bixi2014 = self.get_bixi_data_year(2014)
        bixi2015 = self.get_bixi_data_year(2015)
        bixi2016 = self.get_bixi_data_year(2016)
        weatherDF = self.get_weather_df()

        listOfBixiDF = []
        listOfBixiDF.append(bixi2014)
        listOfBixiDF.append(bixi2015)
        listOfBixiDF.append(bixi2016)
        allBixiDF = pd.concat(listOfBixiDF)
        
        return self.prep_df_for_rf(allBixiDF, weatherDF)
        

    def get_testing_df(self, station, startDate, endDate):
        print('getting testing dataframe...')
        startDateObj = datetime.datetime.strptime(startDate, '%d-%m-%Y')
        startYear = startDateObj.year

        pathTestingDF = self.TESTING_DF_PATH
        pathTestingDF = pathTestingDF.replace('.pkl', '_' + str(startYear) + '.pkl')
        myFile = Path(pathTestingDF)
        if myFile.is_file():
            #loading existing predicting dataframe for requested year
            unfilteredTestingDF = pd.read_pickle(myFile)
        else:
            bixi2017 = self.get_bixi_data_year(2017)

            #changing year for testing prediction
            if str(startYear) != '2017':
                yearOffset = startYear - 2017
                bixi2017['start_date'] = bixi2017['start_date'] + pd.DateOffset(years=yearOffset)
            
            weather = self.get_weather_df()
            unfilteredTestingDF = self.prep_df_for_rf(bixi2017, weather)
            unfilteredTestingDF.to_pickle(myFile)

        #filtering for requested station
        if str(station) != 'all':
            unfilteredTestingDF = unfilteredTestingDF[unfilteredTestingDF.start_station_code == int(station)].copy()

        #fitlering for requested period
        testingDF = self.period_filter(unfilteredTestingDF, startDate, endDate)

        return testingDF


    def prep_df_for_rf(self, bixi_df, weatherDF):
        print('data preparation for random forest algo')
        df = self.merge_bixi_weather_df(bixi_df, weatherDF)

        #cleaning Columns
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

        # Group by hour and station
        groupedDF = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).agg('first')
        numTripsColumn = df.groupby(['year','month', 'day', 'hour', 'start_station_code']).count()['temperature']
        groupedDF['num_trips']= numTripsColumn
        groupedDF = groupedDF.reset_index()
        #reducing df columns size
        groupedDF['year'] = groupedDF['year'].astype(np.int16)
        groupedDF['month'] = groupedDF['month'].astype(np.int16)
        groupedDF['day'] = groupedDF['day'].astype(np.int16)
        groupedDF['hour'] = groupedDF['hour'].astype(np.int16)
        groupedDF['start_station_code'] = groupedDF['start_station_code'].astype(np.int16)
        groupedDF['weekday'] = groupedDF['weekday'].astype(np.int16)
        groupedDF['weekofyear'] = groupedDF['weekofyear'].astype(np.int16)
        groupedDF['num_trips'] = groupedDF['num_trips'].astype(np.int16)

        #one-hot encoding
        groupedDF = pd.get_dummies(groupedDF)

        return groupedDF


    def get_prediction(self, station, startDate, endDate):

        #get the Random forest model        
        loadedRF = self.get_random_forest_model()
        
        testFeatures = self.get_testing_df(station, startDate, endDate)
        print('getting testing dataframe DONE')

        #removing NaN values in df
        nan_value = float("NaN")
        testFeatures.replace("", nan_value, inplace=True)
        testFeatures.dropna(subset = ["temperature"], inplace=True)

        #splitting testing data in test features and label
        testLabels = np.array(testFeatures['num_trips'])
        testFeatures = testFeatures.drop(['num_trips'], axis=1)

        # Use the forest's predict method on the test data
        predictions = loadedRF.predict(testFeatures)

        #TODO
        #score
        # tempX = predictions.reshape(-1, 1)
        # tempY = testLabels.reshape(-1,1)
        # score = loadedRF.score(testFeatures, testLabels)
        # print('score hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee123123123')
        # print(score)
        print(testFeatures.columns)

        testFeatures.insert(len(testFeatures.columns), 'predictions', predictions)
        testFeatures.insert(len(testFeatures.columns), 'test_labels', testLabels)
        testFeatures.drop(testFeatures.columns.difference(['year', 'month', 'day', 'hour', 'start_station_code',
                                       'weekday', 'weekofyear', 'predictions', 'test_labels'
                                       ]), 1, inplace=True)
        return testFeatures


    #Loading or generating RandomForest Model
    def get_random_forest_model(self):
        myFile = Path(self.RF_MODEL_PATH)
        if myFile.is_file():
            print('loading model...')
            # load the model from disk
            loadedRF = pickle.load(open(myFile, 'rb'))
            print('loading model DONE')
        else:
            trainFeatures = self.get_traning_df()
            print('getting training dataframe DONE')

            #removing NaN values in df
            nan_value = float("NaN")
            trainFeatures.replace("", nan_value, inplace=True)
            trainFeatures.dropna(subset = ["temperature"], inplace=True)

            #splitting testing data in test features and label
            trainLabels = np.array(trainFeatures['num_trips'])
            trainFeatures = trainFeatures.drop(['num_trips'], axis=1)
            
            #instantiate RandomForestRegressoe
            loadedRF = RandomForestRegressor(n_estimators = self.RF_N_ESTIMATORS, 
                                                random_state = self.RF_RANDOM_STATE, 
                                                n_jobs=self.RF_N_JOBS, 
                                                max_depth=self.RF_MAX_DEPTH)
            print('Fitting calculating...')  
            loadedRF.fit(trainFeatures, trainLabels)
            print('Fitting DONE')

            # save the model to disk
            pickle.dump(loadedRF, open(myFile, 'wb'))
        
        #TODO
        #self.get_random_tree_png(loadedRF)
        return loadedRF

    def get_random_tree_png(self, rf):
        featureList = ['year', 'month', 'day', 'hour', 'start_station_code', 'weekday', 'weekofyear', 'wind_speed', 'temperature']
        
        importances = list(rf.featureImportance_)
        # List of tuples with variable and importance
        featureImportance = [(feature, round(importance, 2)) for feature, importance in zip(featureList, importances)]
        # Sort the feature importances by most important first
        featureImportance = sorted(featureImportance, key = lambda x: x[1], reverse = True)
        # Print out the feature and importances 
        [print('Variable: {:20} Importance: {}'.format(*pair)) for pair in featureImportance]

        # Pull out one tree from the forest
        tree = rf.estimators_[3]
        # Export the image to a dot file
        export_graphviz(tree, out_file= 'tree.dot', feature_names = featureList, rounded = True, precision = 1)
        (graph, ) = pydot.graph_from_dot_file('./tree.dot')
        # Write graph to a png file
        graph.write_png('tree.png')
        return None
    
    
    def period_filter(self, df, startDate, endDate):
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
        
        #filtering all dates inside specified period
        indexNames = df[ df['month'] < startMonth ].index
        df.drop(indexNames , inplace=True)
        indexNames = (df[ (df['month'] == startMonth) & (df['day'] < startDay)].index)
        df.drop(indexNames , inplace=True)
        indexNames = df[ df['month'] > endMonth ].index
        df.drop(indexNames , inplace=True)
        indexNames = (df[ (df['month'] == endMonth) & (df['day'] > endDay)].index)
        df.drop(indexNames , inplace=True)

        return df

    def groupby_filter(self, df, groupby):
        if(groupby == "perHour"):
            df = df.groupby(['hour']).agg({'predictions': 'sum', 'test_labels': 'sum'})
        elif(groupby == "perWeekDay"):
            df = df.groupby(['weekday']).agg({'predictions': 'sum', 'test_labels': 'sum'})
        elif(groupby == "perMonth"):
            df = df.groupby(['month']).agg({'predictions': 'sum', 'test_labels': 'sum'})
        elif(groupby == "perDate"):
            df = df.groupby(['month', 'day']).agg({'predictions': 'sum', 'test_labels': 'sum'})
        return df


    def get_prediction_graph(self, groupby, x, y, station):
        barWidth = 0.25
        plt.clf()
        if groupby == "perDate":
            plt.plot(x, y, color=self.GRAPH_COLOR, marker='jsonInString', label='Predictions')
            plt.xticks(x[::self.GRAPH_LABEL_STEP], rotation=self.GRAPH_LABEL_ROTATION)
        elif groupby != "perDate":            
            plt.bar(x, y, color=self.GRAPH_COLOR, width=barWidth, edgecolor='white', label='Predictions')
            plt.xticks( rotation=self.GRAPH_LABEL_ROTATION)

        graphTitle = 'Prediction of Bixi Usage ' + str(groupby)
        graphTitle += ' for '
        if station == 'all':
            graphTitle += 'All Stations'
        else:
            graphTitle += 'station #{}'.format(station)
        plt.title(graphTitle)
        # Add xticks on the middle of the group bars
        ('adding xticks')
        plt.xlabel(str(groupby), fontweight='bold')
        plt.tight_layout()
        plt.ylabel('Predictions', fontweight='bold')
        plt.legend()
        plt.savefig(self.PRED_GRAPH_PATH)
        print('graph generated', flush=True)
        #TODO
        plt.show()
        return plt
    
    def get_graph_X(self, df, groupby):
        if groupby == 'perDate':
            xAxisDate = []
            for i in range(len(df.index.values)):
                xAxisDate.append(str(df.index.values[i][0]) + "-" + str(df.index.values[i][1]))
                tempXAxis = pd.Series(xAxisDate)
            xAxis = tempXAxis.tolist()
            
        else:
            tempXAxis = df.index.values
            if groupby == 'perMonth':
                label = self.PER_MONTH_LABEL
                groupbyAlign = -1
            elif groupby == 'perHour':
                label = self.PER_HOUR_LABEL
                groupbyAlign = 0
            elif groupby == 'perWeekDay':
                #0=mon, 1=tue, 2=wed, 3=thu, 4=fri, 5=sat, 6=sun
                label = self.PER_WEEKDAY_LABEL
                groupbyAlign = 0

            xAxis = []
            for i in tempXAxis:
                xAxis.append(label[i+groupbyAlign])
        
        return xAxis

    
    def get_graph_Y(self, df):
        return df['predictions'].values


    def toBase64(self):
        with open(self.PRED_GRAPH_PATH, "rb") as imageFile:
            strg = base64.b64encode(imageFile.read()).decode('utf-8')
            while strg[-1] == '=':
                strg = strg[:-1]
        return strg

    def datatoJSON(self, graph, x, y, groupby):
        graphString = self.toBase64()
        myJson =  '{  "data":{ "time":[], "predictions":[] }, "graph":[] }'
        if groupby == 'perMonth' or groupby == 'perWeekDay':
            if groupby == 'perMonth':
                completeLabel = self.COMPLETE_MONTH_LABEL
            elif groupby == 'perWeekDay':
                completeLabel = self.COMPLETE_WEEKDAY_LABEL

            for i in range(len(x)):
                for j in range(len(completeLabel)):
                    if x[i] in completeLabel[j]:
                        x[i] = completeLabel[j]
                        
        jsonInString = json.loads(myJson)
        jsonInString["data"]["time"] = x[0:len(x)]
        jsonInString["data"]["predictions"] = y.tolist()
        jsonInString["graph"] = graphString
        
        return json.dumps(jsonInString)
