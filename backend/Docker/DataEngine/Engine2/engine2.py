import json
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import base64
import logging

class Engine2:

    hourLabel = ['0h', '1h','2h', '3h', '4h', '5h', '6h','7h','8h','9h','10h','11h','12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h']
    monthLabel = ['Jan', 'Fev', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    monthLabel2 = ['Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov']
    weekDayLabel = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    firstDayMonday = 3
    new_list = []

    logging.basicConfig(filename='engine2.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

    def __init__(self):
        return None 
                      
    def getPerTimeCountStart(self, df, station, time):
        logging.info('Entering getTimeCountStart()')
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
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+self.firstDayMonday, 7)
            label = self.weekDayLabel
        elif time == "perMonth":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        timeCount = np.bincount(timeNumber, None, len(label))
        logging.info('TimeCount Start: ')
        logging.info(timeCount)
        return timeCount
    
    def getPerTimeCountEnd(self, df, station, time):
        logging.info('Entering getPerTimeCountEnd()')
        dfEnd = df[['end_date', 'end_station_code']].copy()
        #filter rows
        if station != 'all':
            #Get indexes where name column has value stationCode
            indexNames = dfEnd[dfEnd['end_station_code'] != str(station)].index
            #Delete these row indexes from dataFrame
            dfEnd.drop(indexNames , inplace=True)
        startDateSeries = dfEnd['end_date'].values.astype('datetime64[m]')
        if time == "perHour":
            timeNumber = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
            label = self.hourLabel
        elif time == "perWeekDay":
            timeNumber = np.remainder(startDateSeries.astype("M8[D]").astype("int")+self.firstDayMonday, 7)
            label = self.weekDayLabel
        elif time == "perMonth":
            timeNumber = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
            label = self.monthLabel
        timeCount = np.bincount(timeNumber, None, len(label))
        logging.info('TimeCount End: ')
        logging.info(timeCount)
        return timeCount
    
    def getGraphPerTime(self, countStart, countEnd, station, time):
        logging.info('Entering getGraphPerTime()')
        if time == 'perMonth':
            countStart = countStart[3:len(countStart)-1]
            countEnd = countEnd[3:len(countEnd)-1]
        # set width of bar
        barWidth = 0.25
        # Set position of bar on X axis
        r1 = np.arange(len(countStart))
        r2 = [x + barWidth for x in r1]
        # Make the plot
        plt.clf()
        plt.bar(r1, countStart, color='#0A6BF3', width=barWidth, edgecolor='white', label='Start')
        plt.bar(r2, countEnd, color='#F3380A', width=barWidth, edgecolor='white', label='End')
        # Add xticks on the middle of the group bars
        ('adding xticks')
        if time == "perHour":
            plt.xlabel('Per Hour', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.hourLabel[:len(countStart)], rotation="vertical")
            plt.title('Bixi usage per Hour of the Day for Station#{}'.format(station))
            plt.tight_layout()
        elif time == "perWeekDay":
            plt.xlabel('Per Week Day', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.weekDayLabel[:len(countStart)])
            plt.title('Bixi usage per Weekday for Station#{}'.format(station))
            plt.tight_layout()
        elif time == "perMonth":
            plt.xlabel('Per Month', fontweight='bold')
            plt.ylabel('User Count', fontweight='bold')
            plt.xticks([r + barWidth for r in range(len(countStart))], self.monthLabel[3:len(countStart)+3])
            plt.title('Bixi usage per Month for Station#{}'.format(station))
            plt.tight_layout()
        # Create legend & Show graphic
        plt.legend()
        plt.savefig('bar.png')
        logging.info('Graph generated')
        return plt


    def toBase64(self):
        logging.info('Graph toBase64()')
        with open("bar.png", "rb") as imageFile:
            graphBase64 = base64.b64encode(imageFile.read()).decode('utf-8')
            while graphBase64[-1] == '=':
                graphBase64 = graphBase64[:-1]
        return graphBase64

    def datatoJSON(self, year, time, station):
        logging.info('Entering datatoJSON()')
        ye = int(year)
        ti = str(time)
        if str(station) == 'all':
            st = str(station)
        else:
            st = station

        path = "./kaggleData/OD_{}".format(ye)
        path += ".csv"
        df = pd.read_csv(path, dtype={
            'start_date':str,
            'start_station_code':int,
            'end_date':str,
            'end_station_code':str,
            'duration_sec':int,
            'is_member':int})
        logging.info('dataframe: ')
        logging.info(df)
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
        
        logging.info('Label used: ')
        logging.info(label)
        return json.dumps(o)
    
    def octectList(self, octect):
        list = []
        list.append(int(octect))
        j = 0
        for i in range(0, len(list)):
            j+=list[i]
            self.new_list.append(j)
        return self.cumlativeList(self.new_list)

    def cumlativeList(self, list):
        cum_list = []
        length = len(list)
        cum_list = [sum(list[0:x:1]) for x in range(0, length+1)]
        return cum_list[1:]

    def logsToJSON(self, octect):
        logs = {}
        list = self.octectList(octect)
        lenght = len(list)
        with open('engine1.log') as f:
            c = f.read()[list[lenght-1]:]
            r = c.split('\n')
            logs = {'logs': r[0]}
        return logs
