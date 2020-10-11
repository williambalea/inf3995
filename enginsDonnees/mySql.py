import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from enginSQL import EnginSQL

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000



enginsql = EnginSQL()
station5003 = enginsql.getStationCode(CODE_STATION)
# allStations = enginsql.getAllStations
print(station5003)



