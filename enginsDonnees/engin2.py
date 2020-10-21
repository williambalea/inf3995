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
             

    # def getPerHourCountStart(self, df, station):
    #     # print(station)
    #     if station != 'toutes':
    #         #Get indexes where name column has value stationCode
    #         indexNames = df[df['startStationCode'] != int(station)].index
    #         #Delete these row indexes from dataFrame
    #         df.drop(indexNames , inplace=True)
    #     # print(df)
    #     print('line27')
    #     df['startDate'] = pd.to_datetime(df['startDate'])
    #     print('line29')
    #     # df.to_csv('after1.csv', index=False) 
    #     startDateSeries = df['startDate'].values.astype('datetime64[m]')
    #     print('line32')
    #     hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
    #     print('5. beginning bincount')
    #     hourCount = np.bincount(hours)
    #     # return dict(zip(range(24), hourCount))
    #     return hourCount
    
    # def getPerHourCountEnd(self,df, station):
    #     if station != 'toutes':
    #         # Get indexes where name column has value john
    #         indexNames = df[df['endStationCode'] != int(station)].index
    #         # Delete these row indexes from dataFrame
    #         df.drop(indexNames , inplace=True)
    #     # df['endDate'] = pd.to_datetime(df['endDate'], infer_datetime_format=True)
    #     print('allo')
    #     startDateSeries = df['endDate'].values.astype('datetime64[m]')
    #     hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
    #     hourCount = np.bincount(hours)
    #     # return dict(zip(range(24), counts))
    #     return hourCount
          

    def getPerHourCountStart(self, df, station):
        # print(station)
        if station != 'toutes':
            #Get indexes where name column has value stationCode
            indexNames = df[df['startStationCode'] != int(station)].index
            #Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        # print(df)
        print('line27')
        df['start_date'] = pd.to_datetime(df['start_date'])
        print('line29')
        # df.to_csv('after1.csv', index=False) 
        startDateSeries = df['start_date'].values.astype('datetime64[m]')
        print('line32')
        hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
        print('5. beginning bincount')
        hourCount = np.bincount(hours)
        # return dict(zip(range(24), hourCount))
        return hourCount
    
    def getPerHourCountEnd(self,df, station):
        if station != 'toutes':
            # Get indexes where name column has value john
            indexNames = df[df['endStationCode'] != int(station)].index
            # Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        # df['endDate'] = pd.to_datetime(df['endDate'], infer_datetime_format=True)
        print('allo')
        startDateSeries = df['end_date'].values.astype('datetime64[m]')
        hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
        hourCount = np.bincount(hours)
        # return dict(zip(range(24), counts))
        return hourCount

    def getPerWeekDayCountStart(self, df, station):
        if station != 'toutes':
            # Get indexes where name column has value john
            indexNames = df[df['start_station_code'] != int(station)].index
            # Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        df['start_date'] = pd.to_datetime(df['start_date'])
        startDateSeries = df['start_date'].values.astype('datetime64[m]')
        #0=thursday, 2=saturday, 4=monday, 6=wednesday
        #with +4, 0=sunday, 2=tuesday, 5=friday, 6-saturday 
        days = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
        dayCount = np.bincount(days)
        # return dict(zip(range(24), counts))
        return dayCount

    def getPerWeekDayCountEnd(self, df, station):
        if station != 'toutes':
            # Get indexes where name column has value john
            indexNames = df[df['end_station_code'] != int(station)].index
            # Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        df['end_date'] = pd.to_datetime(df['end_date'])
        startDateSeries = df['end_date'].values.astype('datetime64[m]')
        #0=thursday, 2=saturday, 4=monday, 6=wednesday
        #with +4, 0=sunday, 2=tuesday, 5=friday, 6-saturday 
        days = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
        dayCount = np.bincount(days)
        # return dict(zip(range(24), counts))
        return dayCount

    def getPerMonthCountStart(self, df, station):
        if station != 'toutes':
            # Get indexes where name column has value john
            indexNames = df[df['start_station_code'] != int(station)].index
            # Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        print(df)
        df['start_date'] = pd.to_datetime(df['start_date'])
        startDateSeries = df['start_date'].values.astype('datetime64[m]')
        print('startDateSeries: ')
        print(startDateSeries)
        months = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
        print('months: ')
        print(months)
        monthCount = np.bincount(months)
        return monthCount
    
    def getPerMonthCountEnd(self, df, station):
        if station != 'toutes':
            # Get indexes where name column has value john
            indexNames = df[df['end_station_code'] != int(station)].index
            # Delete these row indexes from dataFrame
            df.drop(indexNames , inplace=True)
        df['end_date'] = pd.to_datetime(df['end_date'])
        startDateSeries = df['end_date'].values.astype('datetime64[m]')
        months = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
        monthCount = np.bincount(months)
        return monthCount

    def getGraphHour(self, df, station):
        print('2. entering getgraph')
        hourCountStart = self.getPerHourCountStart(df, station)
        print('6. done with getcountstart ')
        hourCountEnd = self.getPerHourCountEnd(df, station)
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

    def getGraphWeekDay(self, df, station):
        weekCountStart = self.getPerWeekDayCountStart(df, station)
        weekCountEnd = self.getPerWeekDayCountEnd(df, station)
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
        plt.savefig('foo.png')
        # plt.show()

    def getGraphMonth(self, df, station):
        monthCountStart = self.getPerMonthCountStart(df, station)
        monthCountEnd = self.getPerMonthCountEnd(df, station)
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
