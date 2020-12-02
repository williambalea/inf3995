FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/Engine1/
RUN mkdir /usr/src/Logs

WORKDIR /usr/src/Logs
COPY ./Logs .

WORKDIR /usr/src/Engine1/
RUN pip3 install --upgrade pip
RUN pip3 install Flask Flask-API mysql-connector-python 
COPY ./Engine1 .
COPY ./kaggleData/Stations_2017.csv ./kaggleData/Stations_2017.csv
ENV FLASK_APP=engine1_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin1 server

CMD ["flask", "run"]
