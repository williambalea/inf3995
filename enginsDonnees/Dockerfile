FROM python:3.8-alpine

#Install dependencies
RUN mkdir /usr/src/engin1/
WORKDIR /usr/src/engin1/
RUN pip3 install Flask
COPY . .

ENV FLASK_APP=engin1.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers

EXPOSE 5000
#Start engin1 server

CMD ["flask", "run"]