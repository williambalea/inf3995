FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/engin1/
WORKDIR /usr/src/engin1/
RUN pip3 install --upgrade pip
RUN pip3 install Flask Flask-API
COPY ./Engine1 .
COPY ./kaggleData ./kaggleData

ENV FLASK_APP=engine1_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin1 server

CMD ["flask", "run"]
