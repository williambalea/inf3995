import json
from enginSQL import EnginSQL
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

class Engin2:

    hourLabel = ['0h', '1h','2h', '3h', '4h', '5h', '6h','7h','8h','9h','10h','11h','12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h']
    monthLabel = ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov']#, 'Dec']
    weekDayLabel = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

    def __init__(self):
        return None 
                      
    def getPerTimeCountStart(self, df, station, time):
            #filter rows
            print('filtering timecountstart')
            if station != 'toutes':
                #Get indexes where name column has value stationCode
                indexNames = df[df['start_station_code'] != int(station)].index
                #Delete these row indexes from dataFrame
                df.drop(indexNames , inplace=True)
            print('line25')
            startDateSeries = df['start_date'].values.astype('datetime64[m]')
            print('line27') 
            if time == "parheure":
                timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            elif time == "parjourdelasemaine":
                timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            elif time == "parmois":
                timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            print('line 34')
            timeCount = np.bincount(timeNumber)
            # return dict(zip(range(24), hourCount))
            return timeCount
    
    def getPerTimeCountEnd(self, df, station, time):
            #filter rows
            print('line41')
            if station != 'toutes':
                #Get indexes where name column has value stationCode
                indexNames = df[df['end_station_code'] != int(station)].index
                #Delete these row indexes from dataFrame
                df.drop(indexNames , inplace=True)
            print('line47')
            startDateSeries = df['end_date'].values.astype('datetime64[m]')
            if time == "parheure":
                timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            elif time == "parjourdelasemaine":
                timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            elif time == "parmois":
                timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            print('line55')
            timeCount = np.bincount(timeNumber)
            # return dict(zip(range(24), hourCount))
            return timeCount
    
    def getGraphPerTime(self, df, station, time):
        dfStart = df[['start_date', 'start_station_code']].copy()
        dfEnd = df[['end_date', 'end_station_code']].copy()
        print(dfStart)
        print('allo1 creating')
        print('2. entering getgraph')
        TimeCountStart = self.getPerTimeCountStart(dfStart, station, time)
        TimeCountEnd = self.getPerTimeCountEnd(dfEnd, station, time)
        print('6. done with getcountstart ')
        print('7. done with getcountend')
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        print('setting position of bar')
        r1 = np.arange(len(TimeCountStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        print('making the plot')
        plt.bar(r1, TimeCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, TimeCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        ('adding xticks')
        if time == "parheure":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(TimeCountStart))], self.hourLabel)
            plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
        elif time == "parjourdelasemaine":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(TimeCountStart))], self.weekDayLabel)
            plt.title('Bixi usage per Weekday for Station#{}'.format(station))
        elif time == "parmois":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(TimeCountStart))], self.monthLabel)
            plt.title('Bixi usage per Month for Station#{}'.format(station))
        
        # Create legend & Show graphic
        print('adding legend')
        plt.legend()
        print('showing plot')
        plt.show()
