import json
import logging
import csv

class Engine1:

    logging.basicConfig(filename='engine1.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')
    PATH = "./kaggleData/Stations_2017.csv"

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

    def logsToJSON(self, byte):
        logs = {}
        logsFile= open('engine1.log', 'r')
        logs= logsFile.read()[int(byte):].splitlines()
        logsFile.close()
        logs = {'logs': logs}
        return self.toJson(logs)
        
