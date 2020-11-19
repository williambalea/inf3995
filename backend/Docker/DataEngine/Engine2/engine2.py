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

    logging.basicConfig(filename='engine.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

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
        logging.info('TimeCount Start: {}'.format(timeCount))
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
        logging.info('TimeCount End: {}'.format(timeCount))
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
        logging.info('Graph generated in PNG')
        return plt


    def toBase64(self):
        with open("bar.png", "rb") as imageFile:
            graphBase64 = base64.b64encode(imageFile.read()).decode('utf-8')
            while graphBase64[-1] == '=':
                graphBase64 = graphBase64[:-1]
        logging.info('Graph toBase64(): {}'.format(graphBase64))
        return graphBase64

    def datatoJSON(self, year, time, station):
        logging.info('Entering datatoJSON()')
        ye = int(year)
        ti = str(time)
        if str(station) == 'all':
            st = str(station)
        else:
            st = station

        path = "../kaggleData/OD_{}".format(ye)
        path += ".csv"
        df = pd.read_csv(path, dtype={
            'start_date':str,
            'start_station_code':int,
            'end_date':str,
            'end_station_code':str,
            'duration_sec':int,
            'is_member':int})
        logging.info('dataframe: {}'.format(df))
        countStart = self.getPerTimeCountStart(df, st, ti)
        countEnd = self.getPerTimeCountEnd(df, st, ti)
        
        self.getGraphPerTime(countStart, countEnd, st, ti)
        graphString = self.toBase64()
        if time == "perHour":
            label = self.hourLabel
        elif time == "perWeekDay":
            label = self.weekDayLabel
        elif time == "perMonth":
            label = self.monthLabel

        myJson =  '{  "data":{ "time":[], "departureValue":[], "arrivalValue":[] }, "graph":[] }'

        sendJson = json.loads(myJson)
        sendJson["data"]["time"] = label
        sendJson["data"]["departureValue"] = countStart[0:len(countStart)].tolist()
        sendJson["data"]["arrivalValue"] = countEnd[0:len(countStart)].tolist()
        sendJson["graph"] = graphString
        
        logging.info('Label used: {}'.format(label))
        return sendJson

    def dataToSend(self, year, time, station):
        return json.dumps(self.datatoJSON(year, time, station))

    def isGraph(self, json):
        if "graph" in json:
            return "False"
        else:
            return "True"