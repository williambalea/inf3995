import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from Engine1.engine1 import Engine1
from Engine2.engine2 import Engine2
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64
from easydict import EasyDict as edict

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
engine1 = Engine1()
print('creating engin2')
engine2 = Engine2()

stationToutes = 'all'
station6202 = 6202
station5003 = 5003
squareVic = 6043
year2014 = 2014
year2015 = 2015
year2016 = 2016
year2017 = 2017
timeHour = "perHour"
timeWeek = "perWeekDay"
timeMonth = "perMonth"


print(engine2.datatoJSON(year2017, timeHour, station5003))