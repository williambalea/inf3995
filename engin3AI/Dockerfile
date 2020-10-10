FROM python:3.8-alpine

#Install dependencies
RUN mkdir /usr/src/engin1/
WORKDIR /usr/src/engin1/
RUN pip3 install Flask

ENV FLASK_APP=engin1.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers

#Start engin1 server

COPY . .
CMD ["flask", "run"]