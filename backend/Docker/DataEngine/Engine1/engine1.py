import mysql.connector
from mysql.connector import Error
import json
import logging
import csv
import re
from flask import request
import hashlib
class Engine1:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Server"
    connection = None
    ACCOUNTS = "SELECT * FROM Accounts"
    logging.basicConfig(filename='engine1.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')
    PATH = "./kaggleData/Stations_2017.csv"

    #constructor
    def __init__(self):
        self.connection = self.create_connection(self.DB_HOSTNAME, self.DB_USERNAME, self.DB_PASSWORD, self.DB_NAMEOFBD)
        # return None

    #create connection to DB
    def create_connection(self, host_name, user_name, user_password, db_name):
        self.connection = None
        try:
            self.connection = mysql.connector.connect(
                host=host_name,
                user=user_name,
                passwd=user_password,
                database=db_name
            )
            logging.info("Connection to MySQL DB successful")
        except Error as e:
            logging.info(f"The error '{e}' occurred")
        return self.connection

    def account_db(self):
        row = []
        myCursor = self.connection.cursor(buffered=True)
        myCursor.execute(self.ACCOUNTS)
        row = myCursor.fetchone()
        return row

    def authentify(self, byte):
        row = self.account_db()
        auth = request.authorization
        authPW = self.secureHashPW(row[1], auth.password).hexdigest()
        if auth and auth.username == row[0] and authPW == row[2] :
            return self.logsToJSON(byte)
        else:
            return "No"
    
    def secureHashPW(self, salt, password):
        hashPW = hashlib.sha512((str(salt) + str(password)).encode('utf-8'))
        return hashPW

    
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

    def toJson(self, data):
        logging.info("Data to JSON")
        return json.dumps(data)

