FROM ubuntu:latest

#Install dependencies
RUN apt-get update
RUN apt-get install -y build-essential
RUN apt-get install -y curl
RUN apt-get install -y git
RUN curl -s "https://cmake.org/files/v3.18/cmake-3.18.2-Linux-x86_64.tar.gz" | tar --strip-components=1 -xz -C /usr/local

#Install mysql cppconn
RUN apt-get install -y libmysqlcppconn-dev

#Install Pistache
WORKDIR /home
RUN git clone https://github.com/oktal/pistache.git
WORKDIR /home/pistache
RUN git submodule update --init
WORKDIR /home/pistache/build
RUN cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
RUN make && make install

#Set directory for the server
WORKDIR /usr/src/bixi

#Copy files from current directory to this image
COPY . . 

# Compile server
RUN g++ server.cpp -o server -lpistache -pthread -lmysqlcppconn


#Expose port
EXPOSE 2000

#Start server
CMD ["./server"]
