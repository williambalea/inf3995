import mysql.connector
from mysql.connector import Error
import json
import socket
import requests as rq
from enginSQL import EnginSQL
from engin2 import Engin2
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

HOST_NAME = "34.70.117.28"
USER_NAME = "root"
USER_PASSWORD = "jerome"
MY_DATABASE = "Bixi"
SQL_REQ1 = "SELECT * FROM Stations"
CODE_STATION = "5003"
URL_POST_TEMP = "/serveur/code"
PORT = 2000

colors = ["#006D2C", "#31A354", "#74C476"]

enginsql = EnginSQL()
engin2 = Engin2()

station = 'toutes'
year = 2015


#create dataframe
amijson = enginsql.getDataUsage(2015, "5003")
df = enginsql.jsonToPandas(amijson)

#convert string to datetime type
df['endDate'] = pd.to_datetime(df['endDate'])



print('Par heures:')
hourCountStart = engin2.getPerHourCountStart(df)
hourCountEnd = engin2.getPerHourCountEnd(df)
# print(hourCountEnd)
# print(hourCountStart)
# new = np.stack((hourCountStart, hourCountEnd), axis=1)
# print("new: ")
# print(new)
# sns.displot(list(new), multiple="stack", rug=True)
# plt.show()

# set width of bar
barWidth = 0.25
 
# set height of bar
bars1 = [12, 30, 1, 8, 22]
bars2 = [28, 6, 16, 5, 10]
bars3 = [29, 3, 24, 25, 17]
 
# Set position of bar on X axis
r1 = np.arange(len(hourCountStart))
r2 = [x + barWidth for x in r1]
 
# Make the plot
plt.bar(r1, hourCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
plt.bar(r2, hourCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
 
# Add xticks on the middle of the group bars
plt.xlabel('Per Hour', fontweight='bold')
plt.ylabel('User Count', fontweight='bold')
plt.xticks([r + barWidth for r in range(len(hourCountStart))], ['0h', '1h', '02', '03', '04', '05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'])
 
# Create legend & Show graphic
plt.legend()
plt.show()
# print('Par jour de la semaine:')
# print(getPerWeekDayCount(df))
# print('Par heure:')
# print(getPerHourCountStart(df))


# sns.displot(list(startCount), list(endCount),  multiple="stack", rug=True)
# sns.displot(list(endCount), multiple="stack", rug=True)
# plt.show()


#potential solution
# df = df.set_index('startDate')
# test = df.groupby(df.index.hour).count()
# print(test)




# startDateDf = startDateDf.set_index('startDate')
# df.set_index(StartDate)
df.to_csv('out2.csv', index=False) 
# a scatter plot comparing num_children and num_pets
# print(test)
# df.plot(kind='bar',x='id',y='durationSec',color='red')
# plt.show()



