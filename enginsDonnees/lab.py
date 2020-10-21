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
year2014 = 2014
year2015 = 2015
year2016 = 2016
year2017 = 2017
timeHour = "parheure"
timeWeek = "parjourdelasemaine"
timeMonth = "parmois"

# query = "SELECT * FROM Stations"
# query = "SELECT BixiRentals2014.* FROM BixiRentals2014"

print('3. reading csv')
# dfcsv = pd.read_csv('../../kaggleData/OD_2014.csv')
dfcsv = pd.read_csv('./OD_2016.csv')

print('entering engin2.getgraphperTime func')
engin2.getGraphPerTime(dfcsv, stationToutes, timeMonth)
