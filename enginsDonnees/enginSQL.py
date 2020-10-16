import mysql.connector
from mysql.connector import Error
import json
from matplotlib import pyplot as plt

class EnginSQL:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Bixi"
    connection = None
    


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


    def query_db(self,query, args=(), one=False):
        myCursor = self.connection.cursor(buffered=True)
        myCursor.execute(query)
        r = [dict((myCursor.description[i][0], value) \
                for i, value in enumerate(row)) for row in myCursor.fetchall()]
        # cur.connection.close()
        return (r[0] if r else None) if one else r

    def toJson(self, data):
        return json.dumps(data)

    def getStationCode(self, code):
        query = "SELECT S.name, S.latitude, S.longitude FROM Stations S WHERE code='{}'".format(code)
        return self.toJson(self.query_db(query))
        


    def getAllStations(self):
        query = "SELECT * FROM Stations"
        return self.toJson(self.query_db(query))
    
    # def getAllYearRaw(self, year):
    #     query = 'SELECT * FROM BixiRentals'
    #     query += str(year)
    #     myCursor = self.connection.cursor()
    #     myCursor.execute(query)
        
    #     return self.toJson(myCursor)