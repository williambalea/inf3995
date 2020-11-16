import json
import logging
import mysql.connector

class MySqlDB:

    PATH = "./kaggleData/Stations_2017.csv"
    
    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAMEOFBD = "Bixi"
    connection = None
    
    #constructor
    def __init__(self):
        # return None
        self.connection = self.create_connection(self.DB_HOSTNAME, self.DB_USERNAME, self.DB_PASSWORD, self.DB_NAMEOFBD)

    #create connection to DB
    def create_connection(self, host_name, user_name, user_password, db_name):
        self.connection = mysql.connector.connect(
            host=host_name,
            user=user_name,
            passwd=user_password,
            database=db_name
        )
        return self.connection

    def authentify(self):
        # self.connection = self.create_connection(self.DB_HOSTNAME, self.DB_USERNAME, self.DB_PASSWORD, self.DB_NAMEOFBD)
        if(self.connection.is_connected()):
            cursor = self.connection.cursor()
            cursor.execute("SELECT * FROM Accounts")
        print(cursor)
        return cursor