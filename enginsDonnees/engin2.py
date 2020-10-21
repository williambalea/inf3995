import json
from enginSQL import EnginSQL
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

class Engin2:

    hourLabel = ['0h', '1h', '02', '03', '04', '05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
    monthLabel = ['01','02','03','04','05','06','07','08','09','10','11']
    weekDayLabel = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

    def __init__(self):
        return None 
                      
    def getPerTimeCountStart(self, df, station, time):
            #filter rows
            if station != 'toutes':
                #Get indexes where name column has value stationCode
                indexNames = df[df['start_station_code'] != int(station)].index
                #Delete these row indexes from dataFrame
                df.drop(indexNames , inplace=True)
                
            startDateSeries = df['start_date'].values.astype('datetime64[m]')
            print('line32') 
            if time == "parheure":
                timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            elif time == "parjourdelasemaine":
                timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            elif time == "parmois":
                timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            print('5. beginning bincount')
            timeCount = np.bincount(timeNumber)
            # return dict(zip(range(24), hourCount))
            return timeCount
    
    def getPerTimeCountEnd(self, df, station, time):
            #filter rows
            if station != 'toutes':
                #Get indexes where name column has value stationCode
                indexNames = df[df['end_station_code'] != int(station)].index
                #Delete these row indexes from dataFrame
                df.drop(indexNames , inplace=True)
                
            startDateSeries = df['end_date'].values.astype('datetime64[m]')
            if time == "parheure":
                timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            elif time == "parjourdelasemaine":
                timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            elif time == "parmois":
                timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            timeCount = np.bincount(timeNumber)
            # return dict(zip(range(24), hourCount))
            return timeCount
    
    def getGraphPerTime(self, df, station, time):
        print('2. entering getgraph')
        TimeCountStart = self.getPerTimeCountStart(df, station, time)
        print('6. done with getcountstart ')
        TimeCountEnd = self.getPerTimeCountEnd(df, station, time)
        print('7. done with getcountend')
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(TimeCountStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.bar(r1, TimeCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, TimeCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        plt.xlabel('Per Hour', fontweight='bold')
        plt.ylabel('User Count', fontweight='bold')
        plt.xticks([r + barWidth for r in range(len(TimeCountStart))], self.hourLabel)
        plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
        # Create legend & Show graphic
        plt.legend()
        plt.show()


    def getGraphHour(self, df, station, time):
        print('2. entering getgraph')
        hourCountStart = self.getPerTimeCountStart(df, station, time)
        print('6. done with getcountstart ')
        hourCountEnd = self.getPerTimeCountEnd(df, station, time)
        print('7. done with getcountend')
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(hourCountStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.bar(r1, hourCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, hourCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        plt.xlabel('Per Hour', fontweight='bold')
        plt.ylabel('User Count', fontweight='bold')
        plt.xticks([r + barWidth for r in range(len(hourCountStart))], self.hourLabel)
        plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
        # Create legend & Show graphic
        plt.legend()
        plt.show()



    def getGraphWeekDay(self, df, station, time):
        print('2. entering getgraph')
        weekCountStart = self.getPerTimeCountStart(df, station, time)
        print('6. done with getcountstart ')
        weekCountEnd = self.getPerTimeCountEnd(df, station, time)
        print('7. got all count')
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(weekCountStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.bar(r1, weekCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, weekCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        plt.xlabel('Per Hour', fontweight='bold')
        plt.ylabel('User Count', fontweight='bold')
        plt.xticks([r + barWidth for r in range(len(weekCountStart))], self.weekDayLabel)
        plt.title('Bixi usage per Weekday for Station#{}'.format(station))
        # Create legend & Show graphic
        plt.legend()
        plt.show()

    def getGraphMonth(self, df, station, time):
        print('2. entering getgraph')
        monthCountStart = self.getPerTimeCountStart(df, station, time)
        print('6. done with getcountstart ')
        monthCountEnd = self.getPerTimeCountEnd(df, station, time)
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(monthCountStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.bar(r1, monthCountStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, monthCountEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        plt.xlabel('Per Hour', fontweight='bold')
        plt.ylabel('User Count', fontweight='bold')
        plt.xticks([r + barWidth for r in range(len(monthCountStart))], self.monthLabel)
        plt.title('Bixi usage per Month for Station#{}'.format(station))
        # Create legend & Show graphic
        plt.legend()
        plt.show()
