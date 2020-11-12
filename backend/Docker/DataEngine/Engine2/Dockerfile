FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/engine2/
WORKDIR /usr/src/engine2/
RUN pip3 install --upgrade pip
RUN pip3 install Flask Flask-API matplotlib pandas numpy easydict 
COPY ./Engine2 .
COPY ./kaggleData ./kaggleData

ENV FLASK_APP=engine2_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin2 server

CMD ["flask", "run"]
