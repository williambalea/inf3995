import mysql.connector
import json
import socket
import requests as rq
from Engine1.engine1 import Engine1
from Engine2.engine2 import Engine2
from MySqlDB.mysql import MySqlDB
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64
from easydict import EasyDict as edict

# HOST_NAME = "34.70.117.28"
# USER_NAME = "root"
# USER_PASSWORD = "jerome"
# MY_DATABASE = "Bixi"
# SQL_REQ1 = "SELECT * FROM Stations"
# CODE_STATION = "5003"
# URL_POST_TEMP = "/serveur/code"
# PORT = 2000

# colors = ["#006D2C", "#31A354", "#74C476"]
# print('creating enginsql')
# engine1 = Engine1()
# print('creating engin2')
# engine2 = Engine2()
mysql = MySQLDB()

mysql.authentify()


