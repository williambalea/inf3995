import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="projet3"
)

print(mydb)