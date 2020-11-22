FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/Engine3/
RUN mkdir /usr/src/Mysql

WORKDIR /usr/src/Mysql
COPY ./Mysql .

WORKDIR /usr/src/Engine3/
RUN pip3 install --upgrade pip
RUN pip3 install Flask Flask-API matplotlib pandas numpy easydict mysql-connector-python seaborn sklearn mysql-connector-python-rf
COPY ./Engine3 .
COPY ./kaggleData/historical-hourly-weather-data ./kaggleData/historical-hourly-weather-data
COPY ./kaggleData/OD_2014.csv ./kaggleData/OD_2014.csv
COPY ./kaggleData/OD_2015.csv ./kaggleData/OD_2015.csv
COPY ./kaggleData/OD_2016.csv ./kaggleData/OD_2016.csv
COPY ./kaggleData/OD_2017.csv ./kaggleData/OD_2017.csv

ENV FLASK_APP=engine3_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin2 server

CMD ["flask", "run"]
