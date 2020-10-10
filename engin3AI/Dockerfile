FROM python:3.8-alpine

#Install dependencies
RUN mkdir /usr/src/engin1/
RUN pip3 install Flask

COPY . /usr/src/engin1/

ENV FLASK_APP=engin1.py
ENV FLASK_RUN_HOST=0.0.0.0
WORKDIR /usr/src/engin1/

#Expose port
EXPOSE 5000

#Start engin1 server

CMD ["flask", "run"]