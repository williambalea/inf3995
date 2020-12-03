FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/Engine2/
RUN mkdir /usr/src/Logs

WORKDIR /usr/src/Logs
COPY ./Logs .

WORKDIR /usr/src/Engine2/
RUN pip3 install --upgrade pip
RUN pip3 install Flask Flask-API matplotlib pandas numpy easydict mysql-connector-python 
COPY ./Engine2 .
COPY ./CSVData/OD_2014.csv ./CSVData/OD_2014.csv
COPY ./CSVData/OD_2015.csv ./CSVData/OD_2015.csv
COPY ./CSVData/OD_2016.csv ./CSVData/OD_2016.csv
COPY ./CSVData/OD_2017.csv ./CSVData/OD_2017.csv

ENV FLASK_APP=engine2_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin2 server

CMD ["flask", "run"]
