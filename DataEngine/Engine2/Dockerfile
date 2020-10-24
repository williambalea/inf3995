FROM python:3.8.5

#Install dependencies
RUN mkdir /usr/src/engine2/
WORKDIR /usr/src/engine2/
RUN pip3 install --upgrade pip
RUN pip3 install Flask mysql-connector-python matplotlib pandas numpy easydict seaborn
COPY . .

ENV FLASK_APP=engine2_HTTP.py
ENV FLASK_RUN_HOST=0.0.0.0

EXPOSE 5000
#Start engin1 server

CMD ["flask", "run"]
