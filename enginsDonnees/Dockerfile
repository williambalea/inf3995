FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/engin1/
WORKDIR /usr/src/engin1/
RUN pip3 install --upgrade pip
RUN pip3 install Flask mysql-connector-python matplotlib pandas
COPY . .

ENV FLASK_APP=enginHTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin1 server

CMD ["flask", "run"]
