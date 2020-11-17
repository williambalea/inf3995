import mysql.connector
from mysql.connector import Error
import json
import logging
import csv
import re
from flask import request
import hashlib
from unittest import case

class MySqlDB:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Server"
    CONNECTION = None
    ACCOUNTS = "SELECT * FROM Accounts"
    ENGINE1_LOGS = "../Engine1/engine.log"
    ENGINE2_LOGS = "../Engine2/engine.log"
    ANSI_COLOR = r'(\x9B|\x1B\[)[0-?]*[ -\/]*[@-~]'
    ANSI_ESPACE = r'\\x1b[^m]*m'

    def __init__(self):
        return None
    
    #create connection to DB
    def create_connection(self, host_name, user_name, user_password, db_name):
        self.CONNECTION = None
        try:
            self.CONNECTION = mysql.connector.connect(
                host=host_name,
                user=user_name,
                passwd=user_password,
                database=db_name
            )
            logging.info("Connection to MySQL DB successful")
        except Error as e:
            logging.info(f"The error '{e}' occurred")
        return self.CONNECTION

    def account_db(self):
        self.CONNECTION = self.create_connection(self.DB_HOSTNAME, self.DB_USERNAME, self.DB_PASSWORD, self.DB_NAMEOFBD)
        row = []
        myCursor = self.CONNECTION.cursor(buffered=True)
        myCursor.execute(self.ACCOUNTS)
        row = myCursor.fetchone()
        self.CONNECTION.close()
        return row

    def authorizationLogs(self, engine, byte):
        row = self.account_db()
        auth = request.authorization
        authPW = self.secureHashPW(row[1], auth.password).hexdigest()
        if auth and auth.username == row[0] and authPW == row[2] :
            logging.info("Successful Authentication")
            return self.logsToJSON(engine, byte)
        else:
            logging.info("The user name or password is incorrect")
    
    def secureHashPW(self, salt, password):
        hashPW = hashlib.sha512((str(salt) + str(password)).encode('utf-8'))
        return hashPW

    def logsToJSON(self, engine, byte):
        logs = {}
        removeAnsi = []
        if (engine == "engine1"):
            logsFile= open(self.ENGINE1_LOGS, 'r')
        else:
            logsFile= open(self.ENGINE2_LOGS, 'r')
        logs = logsFile.read()[int(byte):].splitlines()
        logsFile.close()
        for i in range(0, len(logs)):
            removeAnsi.append(self.espace_ansi(logs[i]))
        logs = {'logs': removeAnsi}
        return self.toJson(logs)
        
    def espace_ansi(self, line):
        ansi_espace = re.sub(self.ANSI_COLOR, '', line)
        ansi_espace = re.sub(self.ANSI_ESPACE, "", ansi_espace)
        return ansi_espace

    def toJson(self, data):
        logging.info("Data to JSON")
        return json.dumps(data)
        