import mysql.connector
from mysql.connector import Error
import json
from matplotlib import pyplot as plt
import pandas as pd
from flask.json import jsonify
import logging

class Engine1:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Bixi"
    connection = None
    
    logging.basicConfig(filename='engine1.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

    #constructor
    def __init__(self):
        self.connection = self.create_connection(self.DB_HOSTNAME, self.DB_USERNAME, self.DB_PASSWORD, self.DB_NAMEOFBD)

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
            logging.error(f"The error '{e}' occurred")
        return self.connection

    #query db then to kind of json with ' instead of "
    def query_db(self,query, args=(), one=False):
        logging.info("Fetching data from mySQL DB")
        myCursor = self.connection.cursor(buffered=True)
        myCursor.execute(query)
        r = [dict((myCursor.description[i][0], value) \
                for i, value in enumerate(row)) for row in myCursor.fetchall()]
        return (r[0] if r else None) if one else r

    def toJson(self, data):
        logging.info("Data to JSON")
        return json.dumps(data)
    
    def getStationCode(self, code):
        query = "SELECT S.name, S.latitude, S.longitude FROM Stations2017 S WHERE code='{}'".format(code)
        logging.info(query)
        return self.toJson(self.query_db(query))
        
    def getAllStations(self):
        query = "SELECT * FROM Stations2017"
        logging.info(query)
        return self.toJson(self.query_db(query))
    