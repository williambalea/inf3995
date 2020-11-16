import json
import logging
import csv
import re
from mysql import MySqlDB
# from flask_mysqldb import MySQL
# import MySQLdb.cursors
# import requests as rq
# from requests.auth import HTTPBasicAuth

class Engine1:

    logging.basicConfig(filename='engine1.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')
    PATH = "./kaggleData/Stations_2017.csv"
    
    # DB_HOSTNAME = "34.70.117.28"
    # DB_USERNAME = "root"
    # DB_PASSWORD = "jerome"
    # DB_NAMEOFBD = "Bixi"
    # connection = None
    #constructor
    def __init__(self):
        return None

    def toJson(self, data):
        logging.info("Data to JSON")
        return json.dumps(data)
        
    def getAllStations(self):
        logging.info("Getting all stations")
        data = []
        csvFile = open(self.PATH, 'r', encoding='ISO-8859-1')
        csvReader = csv.DictReader(csvFile)
        data = self.toJson([row for row in csvReader])
        return data

    def logsToJSON(self, byte):
        logs = {}
        logsFile= open('engine1.log', 'r')
        logs = logsFile.read()[int(byte):].splitlines()
        logsFile.close()
        logs = self.espace_ansi(str(logs))
        logs = {'logs': logs}
        return self.toJson(logs)
        
    def espace_ansi(self, line):
        ansi_espace = re.compile(r'(\x9B|\x1B\[)[0-?]*[ -\/]*[@-~]')
        return ansi_espace.sub(" ", line)

    def login(self):
        mysql = MySqlDB()
        return mysql
        # r = rq.get('https://70.80.27.156/server/user/login', auth=('dapak', 'ordinateur'))

        # session = r.Session()
        # print(r.text)
        # res = rq.get('https://70.80.27.156/server/user/login', verify=False, auth=HTTPBasicAuth('admin', 'admin'))
        # print (res.text)

        # mydata = json.dumps({'user': user, 'pw': pw})

        # cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        # cursor.execute('SELECT * FROM Accounts WHERE user = %s AND salt = %s AND pw = %s', (user, salt, pw,))
        # account = cursor.fetchone()
        # if account:
        #     session['loggedin'] = True
        #     session['id'] = account['id']
        #     session['user'] = account['user']
        #     return 1
        # else:
        # return res
