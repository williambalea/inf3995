import mysql.connector
from mysql.connector import Error
import json

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

    def getStationCode(self, code):
        mycursor = self.connection.cursor()
        mycursor.execute("SELECT S.name, S.latitude, S.longitude FROM Stations S WHERE code='{}'".format(code))
        return self.toJson(mycursor)


    def getAllStations(self):
        mycursor = self.connection.cursor()
        mycursor.execute("SELECT * FROM Stations")
        return self.toJson(mycursor)


    def toJson(self, cursor):
        row_headers=[x[0] for x in cursor.description] #extract row headers
        myresult = cursor.fetchall()
        json_data=[]
        for result in myresult:
            json_data.append(dict(zip(row_headers,result)))
        return json.dumps(json_data)