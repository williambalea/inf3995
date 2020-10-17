import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from enginSQL import EnginSQL
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000



enginsql = EnginSQL()

station = 'toutes'
year = 2015
weekDays = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
months = [ 'April', 'May', 'June', 'July','August',
            'September', 'October', 'November']
monthsNumber = [ '4/', '5/', '6/', '7/', '8/',
                 '9/', '10/', '11/']
columnNames = ['id', 'startDate', 'startStationCode', 'endDate', 'endStationCode', 'durationSec', 'isMember']
count24h = np.zeros(24)


#create dataframe
amijson = enginsql.getDataUsage(2015, "5003")
df = enginsql.jsonToPandas(amijson)

#convert string to datetime type
df['endDate'] = pd.to_datetime(df['endDate'])


def getPerHourCountEnd(dataframe):
    dataframe['endDate'] = pd.to_datetime(dataframe['endDate'])
    startDateSeries = dataframe['endDate'].values.astype('datetime64[m]')
    hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
    hourCount = np.bincount(hours)
    # return dict(zip(range(24), counts))
    return hourCount

def getPerWeekDayCount(dataframe):
    dataframe['startDate'] = pd.to_datetime(dataframe['startDate'])
    startDateSeries = dataframe['startDate'].values.astype('datetime64[m]')
    #0=thursday, 2=saturday, 4=monday, 6=wednesday
    #with +4, 0=sunday, 2=tuesday, 5=friday, 6-saturday 
    days = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
    dayCount = np.bincount(days)
    # return dict(zip(range(24), counts))
    return dayCount

def getPerMonthCount(dataframe):
    dataframe['startDate'] = pd.to_datetime(dataframe['startDate'])
    startDateSeries = dataframe['startDate'].values.astype('datetime64[m]')
    months = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
    monthCount = np.bincount(months)
    return monthCount

def getPerHourCountStart(dataframe):
    dataframe['startDate'] = pd.to_datetime(dataframe['startDate'])
    startDateSeries = dataframe['startDate'].values.astype('datetime64[m]')
    hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
    hourCount = np.bincount(hours)
    # return dict(zip(range(24), hourCount))
    return hourCount
print('Par mois:')
print(getPerMonthCount(df))
print('Par jour de la semaine:')
print(getPerWeekDayCount(df))
print('Par heure:')
print(getPerHourCountStart(df))


# sns.displot(list(startCount), list(endCount),  multiple="stack", rug=True)
# sns.displot(list(endCount), multiple="stack", rug=True)
# plt.show()


#potential solution
# df = df.set_index('startDate')
# test = df.groupby(df.index.hour).count()
# print(test)




# startDateDf = startDateDf.set_index('startDate')
# df.set_index(StartDate)
df.to_csv('out2.csv', index=False) 
# a scatter plot comparing num_children and num_pets
# print(test)
# df.plot(kind='bar',x='id',y='durationSec',color='red')
# plt.show()



