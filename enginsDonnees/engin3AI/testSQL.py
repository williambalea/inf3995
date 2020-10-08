import mysql.connector

mydb = mysql.connector.connect(
	host="localhost",
	user="roots@localhosts",
	password="projet3"
)

print(mydb)
