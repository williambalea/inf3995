import json
from enginSQL import EnginSQL
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64

class Engin2:

    hourLabel = ['0h', '1h','2h', '3h', '4h', '5h', '6h','7h','8h','9h','10h','11h','12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h']
    monthLabel = ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    weekDayLabel = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

    def __init__(self):
        return None 
                      
    def getPerTimeCountStart(self, df, station, time):
        dfStart = df[['start_date', 'start_station_code']].copy()
        #filter rows
        print('filtering timecountstart')
        if station != 'toutes':
            #Get indexes where name column has value stationCode
            indexNames = dfStart[dfStart['start_station_code'] != int(station)].index
            #Delete these row indexes from dataFrame
            dfStart.drop(indexNames , inplace=True)
        print('line25')
        startDateSeries = dfStart['start_date'].values.astype('datetime64[m]')
        print('line27') 
        if time == "parheure":
            timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            label = self.hourLabel
        elif time == "parjourdelasemaine":
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            label = self.weekDayLabel
        elif time == "parmois":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        print('line 34')
        print(timeNumber)
        timeCount = np.bincount(timeNumber, None, len(label))
        print(timeCount)
        print('dict')
        # print(dict(zip(range(12), hourCount)))
        return timeCount
    
    def getPerTimeCountEnd(self, df, station, time):
        dfEnd = df[['end_date', 'end_station_code']].copy()
        #filter rows
        print('line41')
        if station != 'toutes':
            #Get indexes where name column has value stationCode
            indexNames = dfEnd[dfEnd['end_station_code'] != int(station)].index
            #Delete these row indexes from dataFrame
            dfEnd.drop(indexNames , inplace=True)
        print('line47')
        startDateSeries = dfEnd['end_date'].values.astype('datetime64[m]')
        if time == "parheure":
            timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            label = self.hourLabel
        elif time == "parjourdelasemaine":
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            label = self.weekDayLabel
        elif time == "parmois":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        print('line55')
        timeCount = np.bincount(timeNumber, None, len(label))
        # return dict(zip(range(24), hourCount))
        return timeCount
    
    def getGraphPerTime(self, countStart, countEnd, station, time):
        print('2. entering getgraph')
        # print('oldcountstart')
        # print(oldcountStart)
        # print('countEnd')
        # print(countEnd)
        if time == 'parmois':
            countStart = countStart[3:len(countStart)-1]
            countEnd = countEnd[3:len(countEnd)-1]
        print('countstart')
        print(countStart)
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        print('setting position of bar')
        r1 = np.arange(len(countStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        print('making the plot')
        plt.bar(r1, countStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, countEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        ('adding xticks')
        if time == "parheure":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.hourLabel[:len(countStart)])
            plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
        elif time == "parjourdelasemaine":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.weekDayLabel[:len(countStart)])
            plt.title('Bixi usage per Weekday for Station#{}'.format(station))
        elif time == "parmois":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.monthLabel[3:len(countStart)+3])
            plt.title('Bixi usage per Month for Station#{}'.format(station))
        print(len(countStart))
        print(self.monthLabel[3:len(countStart)-1])
        # Create legend & Show graphic
        print('adding legend')
        plt.legend()
        print('showing plot')
        # plt.show()
        return plt


    def toBase64(self, plt):
        plt.savefig('bar.png')
        with open("bar.png", "rb") as imageFile:
            str = base64.b64encode(imageFile.read()).decode('utf-8')
            strg = str[:-1]
            print(strg)
        return strg