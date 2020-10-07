import mysql.connector

mydb = mysql.connector.connect(
    host="34.70.117.28",
    user="root",
    password="jerome"
)

print(mydb)
