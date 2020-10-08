import mysql.connector

from mysql.connector import Error

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"

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

connection = create_connection(HOST_NAME, USER_NAME, USER_PASSWORD)

def getStationCode(code):
    mycursor = connection.cursor()
    mycursor.execute(SQL_REQ1)
    myresult = mycursor.fetchone()
    RETUprint(myresult) 
    return(0)



