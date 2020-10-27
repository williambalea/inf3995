import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import base64

class Engine2:

    hourLabel = ['0h', '1h','2h', '3h', '4h', '5h', '6h','7h','8h','9h','10h','11h','12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h']
    monthLabel = ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    monthLabel2 = ['Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov']
    weekDayLabel = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

    def __init__(self):
        return None 
                      
    def getPerTimeCountStart(self, df, station, time):
        print('getTimeCountStart()')
        dfStart = df[['start_date', 'start_station_code']].copy()
        #filter rows
        if station != 'all':
            #Get indexes where name column has value stationCode
            indexNames = dfStart[dfStart['start_station_code'] != int(station)].index
            #Delete these row indexes from dataFrame
            dfStart.drop(indexNames , inplace=True)
        startDateSeries = dfStart['start_date'].values.astype('datetime64[m]')
        if time == "perHour":
            timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            label = self.hourLabel
        elif time == "perWeekDay":
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            label = self.weekDayLabel
        elif time == "perMonth":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        timeCount = np.bincount(timeNumber, None, len(label))
        print('TimeCount Start:')
        print(timeCount)
        return timeCount
    
    def getPerTimeCountEnd(self, df, station, time):
        dfEnd = df[['end_date', 'end_station_code']].copy()
        #filter rows
        if station != 'all':
            #Get indexes where name column has value stationCode
            indexNames = dfEnd[dfEnd['end_station_code'] != int(station)].index
            #Delete these row indexes from dataFrame
            dfEnd.drop(indexNames , inplace=True)
        startDateSeries = dfEnd['end_date'].values.astype('datetime64[m]')
        if time == "perHour":
            timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            label = self.hourLabel
        elif time == "perWeekDay":
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
            label = self.weekDayLabel
        elif time == "perMonth":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        timeCount = np.bincount(timeNumber, None, len(label))
        print('TimeCount Start:')
        print(timeCount)
        return timeCount
    
    def getGraphPerTime(self, countStart, countEnd, station, time):
        print('entering getGraphPerTime()')
        if time == 'perMonth':
            countStart = countStart[3:len(countStart)-1]
            countEnd = countEnd[3:len(countEnd)-1]
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(countStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.bar(r1, countStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, countEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        ('adding xticks')
        if time == "perHour":
            plt.xlabel('Per Day', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.hourLabel[:len(countStart)])
            plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
        elif time == "perWeekDay":
            plt.xlabel('Per Week Day', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.weekDayLabel[:len(countStart)])
            plt.title('Bixi usage per Weekday for Station#{}'.format(station))
        elif time == "perMonth":
            plt.xlabel('Per Month', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.monthLabel[3:len(countStart)+3])
            plt.title('Bixi usage per Month for Station#{}'.format(station))
        # Create legend & Show graphic
        plt.legend()
        plt.savefig('bar.png')
        # plt.show()
        print('graph generated')
        return plt


    def toBase64(self):
        print('toBase64()')
        # plt.savefig('bar.png')
        with open("bar.png", "rb") as imageFile:
            strg = base64.b64encode(imageFile.read()).decode('utf-8')
            while strg[-1] == '=':
                strg = strg[:-1]
                
            # print(strg)

        return strg

    def datatoJSON(self, year, time, station):
        print('datatoJSON()')
        ye = int(year)
        ti = str(time)
        if str(station) == 'all':
            st = str(station)
        else:
            st = int(station)

        path = "../kaggleData/OD_{}".format(ye)
        path += ".csv"
        df = pd.read_csv(path)
        print('dataframe: ')
        print(df)
        countStart = self.getPerTimeCountStart(df, st, ti)
        countEnd = self.getPerTimeCountEnd(df, st, ti)
        
        self.getGraphPerTime(countStart, countEnd, st, ti)
        graphString = self.toBase64()
        # graphString = self.getGraphPerTime(countStart, countEnd, st, ti)
        if time == "perHour":
            label = self.hourLabel
        elif time == "perWeekDay":
            label = self.weekDayLabel
        elif time == "perMonth":
            label = self.monthLabel

        myJson =  '{  "data":{ "time":[], "departureValue":[], "arrivalValue":[] }, "graph":[] }'

        o = json.loads(myJson)
        o["data"]["time"] = label
        o["data"]["departureValue"] = countStart[0:len(countStart)].tolist()
        o["data"]["arrivalValue"] = countEnd[0:len(countStart)].tolist()
        o["graph"] = graphString
        
        print('Label used: ')
        print(label)
        return json.dumps(o)