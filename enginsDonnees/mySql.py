import mysql.connector
import json
from mysql.connector import Error

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"

def create_connection(host_name, user_name, user_password):
    connection = None
    try:
        connection = mysql.connector.connect(
            host=host_name,
            user=user_name,
            passwd=user_password,
            database=MY_DATABASE
        )
        print("Connection to MySQL DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")
    return connection


def getStationCode(allo):
    mycursor = connection.cursor()
    mycursor.execute("SELECT S.name, S.latitude, S.longitude FROM Stations S WHERE code='{}'".format(allo))
    row_headers=[x[0] for x in mycursor.description] #extract row headers
    myresult = mycursor.fetchall()
    json_data=[]
    for result in myresult:
        json_data.append(dict(zip(row_headers,result)))
    return json.dumps(json_data)

connection = create_connection(HOST_NAME, USER_NAME, USER_PASSWORD)
station5003 = getStationCode(CODE_STATION)


print(station5003)



