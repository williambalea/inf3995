import json
import logging
import csv

class Engine1:

    logging.basicConfig(filename='engine.log', filemode='w', level=logging.INFO, format='[%(asctime)s] %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')
    PATH = "./CSVData/Stations_2017.csv"

    #constructor
    def __init__(self):
        return None
    
    def get_all_stations(self):
        logging.info("Getting all stations for Android App")
        data = []
        csvFile = open(self.PATH, 'r', encoding='ISO-8859-1')
        csvReader = csv.DictReader(csvFile)
        data = json.dumps([row for row in csvReader])
        return data



