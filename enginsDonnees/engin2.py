import json
from enginSQL import EnginSQL
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

class Engin2:

    def __init__(self):
        return None
    
    def getPerHourCountStart(self, df):
        df['startDate'] = pd.to_datetime(df['startDate'])
        startDateSeries = df['startDate'].values.astype('datetime64[m]')
        hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
        hourCount = np.bincount(hours)
        # return dict(zip(range(24), hourCount))
        return hourCount
    
    def getPerHourCountEnd(self,df):
        df['endDate'] = pd.to_datetime(df['endDate'])
        startDateSeries = df['endDate'].values.astype('datetime64[m]')
        hours = np.remainder(startDateSeries.astype("M8[h]").astype("int"), 24)
        hourCount = np.bincount(hours)
        # return dict(zip(range(24), counts))
        return hourCount

    def getPerWeekDayCount(self, df):
        df['startDate'] = pd.to_datetime(df['startDate'])
        startDateSeries = df['startDate'].values.astype('datetime64[m]')
        #0=thursday, 2=saturday, 4=monday, 6=wednesday
        #with +4, 0=sunday, 2=tuesday, 5=friday, 6-saturday 
        days = np.remainder(startDateSeries.astype("M8[D]").astype("int")+4, 7)
        dayCount = np.bincount(days)
        # return dict(zip(range(24), counts))
        return dayCount

    def getPerMonthCount(self, df):
        df['startDate'] = pd.to_datetime(df['startDate'])
        startDateSeries = df['startDate'].values.astype('datetime64[m]')
        months = np.remainder(startDateSeries.astype("M8[M]").astype("int"), 12)
        monthCount = np.bincount(months)
        return monthCount

