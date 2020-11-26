import mysql.connector
from mysql.connector import Error
import json
import logging
import csv
import re
from flask import request
import hashlib
from unittest import case
from flask_api import status

class MySqlDB:


    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "CentralServer"
    CONNECTION = None
    ACCOUNTS = "SELECT * FROM CentralServer.Accounts"
    ENGINE1_LOGS = "../Engine1/engine.log"
    ENGINE2_LOGS = "../Engine2/engine.log"
    ENGINE3_LOGS = "../Engine3/engine.log"
    ANSI_COLOR = r'(\x9B|\x1B\[)[0-?]*[ -\/]*[@-~]'
    ANSI_ESPACE = r'\\x1b[^m]*m'
    ENGINE1 = "engine1"
    ENGINE2 = "engine2"

    def __init__(self):
        return None
    
    #create connection to DB
    def create_connection(self, host_name, user_name, user_password, db_name):
        self.CONNECTION = None
        self.CONNECTION = mysql.connector.connect(
            host=host_name,
            user=user_name,
            passwd=user_password,
            database=db_name
        )
        if (self.CONNECTION == None):
            logging.info("Failed to connect to MYSQL DB")
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
            return self.engineLogs(engine, byte)
        else:
            logging.info("The user name or password is incorrect"), status.HTTP_401_UNAUTHORIZED
    
    def secureHashPW(self, salt, password):
        hashPW = hashlib.sha512((str(salt) + str(password)).encode('utf-8'))
        return hashPW

    def logsToJSON(self, engine, byte):
        logs = {}
        removeAnsi = []
        lenght = 0
        byte = int(byte)
        if (engine == self.ENGINE1):
            logsFile= open(self.ENGINE1_LOGS, "r")
        elif (engine == self.ENGINE2):
            logsFile= open(self.ENGINE2_LOGS, "r")
        else:
            logsFile= open(self.ENGINE3_LOGS, "r")

        read = logsFile.read()
        logsFile.close()

        if (byte < len(read)):
            logs = read[byte:].splitlines()
        else:
            logs = ''
            return logs, byte
        for i in range(0, len(logs)):
            removeAnsi.append(self.espace_ansi(logs[i]))
        logs = {"logs": removeAnsi}
        lenght = len(read) - byte
        return logs, lenght + 1
        
    def espace_ansi(self, line):
        ansi_espace = re.sub(self.ANSI_COLOR, "", line)
        ansi_espace = re.sub(self.ANSI_ESPACE, "", ansi_espace)
        return ansi_espace
    
    def engineLogs(self, engine, byte):
        logsList = []
        logs, lenght = self.logsToJSON(engine, byte)
        myJson =  '{"byte": []}'
        sendJson = json.loads(myJson)
        sendJson["byte"] = lenght
        if (len(logs) == 0):
            return '', status.HTTP_204_NO_CONTENT
        logsList.append(sendJson)
        for i in range(0, len(logs["logs"])):
            myJson =  '{ "text": [], "logs": [] }'
            sendJson = json.loads(myJson)
            sendJson["text"] = (len(logs["logs"][i]) < 200)
            sendJson["logs"] = logs["logs"][i]
            logsList.append(sendJson)
        return json.dumps(logsList), status.HTTP_200_OK


    
