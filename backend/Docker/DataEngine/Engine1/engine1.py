import json
import logging
import csv

class Engine1:

    logging.basicConfig(filename='engine1.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')
    PATH = "./kaggleData/Stations_2017.csv"
    new_list = []

    def __init__(self):
        return None 

    def toJson(self, data):
        logging.info("Data to JSON")
        return json.dumps(data)
        
    def getAllStations(self):
        logging.info("Getting all stations")
        data = []
        csvFile = open(self.PATH, 'r', encoding='ISO-8859-1')
        csvReader = csv.DictReader(csvFile)
        data = self.toJson([row for row in csvReader])
        return data

    def octectList(self, octect):
        list = []
        list.append(0)
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
            c = f.read()[list[lenght-2]:list[lenght-1]]
            r = c.split('\n')
            logs = {'logs': r[0]}
        return logs

        
