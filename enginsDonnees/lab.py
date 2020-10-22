import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from enginSQL import EnginSQL
from engin2 import Engin2
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000

colors = ["#006D2C", "#31A354", "#74C476"]
print('creating enginsql')
enginsql = EnginSQL()
print('creating engin2')
engin2 = Engin2()

stationToutes = 'toutes'
station6202 = 6202
station5003 = 5003
squareVic = 6043
year2014 = 2014
year2015 = 2015
year2016 = 2016
year2017 = 2017
timeHour = "parheure"
timeWeek = "parjourdelasemaine"
timeMonth = "parmois"

# query = "SELECT * FROM Stations"
# query = "SELECT BixiRentals2014.* FROM BixiRentals2014"

# print('3. reading csv')
# dfcsv = pd.read_csv('../../kaggleData/OD_2016.csv')
# # dfcsv = pd.read_csv('./kaggleData/OD_test.csv')
# # dfcsv = pd.read_csv('./kaggleData/OD_20   16.csv')
# print(dfcsv)
# print('countStart)')
# station = squareVic
# time = timeHour
# countStart = engin2.getPerTimeCountStart(dfcsv, station, time)
# print('countEnd')
# countEnd = engin2.getPerTimeCountEnd(dfcsv, station, time)
# print('entering engin2.getgraphperTime func')
# plt2 = engin2.getGraphPerTime(countStart, countEnd, station, time)

# print(engin2.toBase64(plt2))


def dataUsage(year, time, station):
    ye = int(year)
    ti = str(time)
    if str(station) == 'toutes':
        st = st(station)
    else:
        st = int(station)

    engin2 = Engin2()
    path = "./kaggleData/OD_{}".format(ye)
    path += ".csv"
    df = pd.read_csv(path)
    countStart = engin2.getPerTimeCountStart(df, st, ti)
    countEnd = engin2.getPerTimeCountEnd(df, st, ti)
    plt = engin2.getGraphPerTime(countStart, countEnd, st, ti)
    return engin2.toBase64(plt)

print(dataUsage(2016, 'parmois', 6043))
# plt2.show()

# plt2.savefig('foo.png')

# print('this is string')
# with open("foo.png", "rb") as imageFile:
#     str = base64.b64encode(imageFile.read())
#     # print(str)
# text_file = open("sample.txt", "w")
# n = text_file.write(str)
# text_file.close()

# # assume your plot is saved to tfile
# with open(plt2) as fin:
#     read_in = fin.read()
#     b64_data = base64.urlsafe_b64encode(read_in.decode('utf-8'))
# # now send over the network
