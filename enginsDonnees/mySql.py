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
# station5003 = enginsql.getStationCode(CODE_STATION)
# allStations = enginsql.getAllStations()

# with open('data.json', 'w') as outfile:
#     json.dump(allStations, outfile)
# print(station5003)
# print(allStations)
# test = enginsql.getAllStations()
# query = enginsql.getquery1()

amijson = enginsql.getStationCode(5003)

# stationCode = enginsql.getStationCode(5003)
# tablejson = enginsql.toJSON2(table)
print(amijson)


