import mysql.connector
from mysql.connector import Error
import json
from matplotlib import pyplot as plt
import pandas as pd
from flask.json import jsonify

class Engine1:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Bixi"
    connection = None
    

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
            print("Connection to MySQL DB successful")
        except Error as e:
            print(f"The error '{e}' occurred")
        return self.connection

    #query db then to kind of json with ' instead of "
    def query_db(self,query, args=(), one=False):
        myCursor = self.connection.cursor(buffered=True)
        myCursor.execute(query)
        r = [dict((myCursor.description[i][0], value) \
                for i, value in enumerate(row)) for row in myCursor.fetchall()]
        # cur.connection.close()
        return (r[0] if r else None) if one else r

    #query to db and returns pandas object directly
    def query_pd(self, query):
        df = pd.read_sql_query(query, self.connection)
        df['start_date'] = pd.to_datetime(df['start_date'], infer_datetime_format=True)
        df['end_date'] = pd.to_datetime(df['end_date'], infer_datetime_format=True)
        return df


    def toJson(self, data):
        return json.dumps(data)
    
    def jsonToPandas(self, jsonObj):
        return pd.read_json(jsonObj)

    #queries 
    def getStationCode(self, code):
        query = "SELECT S.name, S.latitude, S.longitude FROM Stations2017 S WHERE code='{}'".format(code)
        return self.toJson(self.query_db(query))
        
    def getAllStations(self):
        query = "SELECT * FROM Stations2017"
        return self.toJson(self.query_db(query))
    
    def getDataUsage(self,year, station):
        query = "SELECT * FROM BixiRentals"
        query += str(year)
        if station != "all":
            query += " WHERE startStationCode='{}'".format(station)
            query += " OR endStationCode='{}' ".format(station)
        return self.toJson(self.query_db(query))