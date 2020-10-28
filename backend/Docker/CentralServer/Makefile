LIBS=-lmysqlcppconn -lpistache -lpthread
CFLAGS=-Wall -Wextra -g -Wno-unused-parameter
CC=g++

output: main.o server.o mysql.o
	$(CC) main.o server.o mysql.o -o server $(CFLAGS) $(LIBS)

main.o: main.cpp
	$(CC) -c $(CFLAGS) main.cpp

server.o: server.cpp server.hpp
	$(CC) -c $(CFLAGS) server.cpp

mysql.o: mysql.cpp mysql.hpp
	$(CC) -c $(CFLAGS) mysql.cpp

clean:
	rm *.o server
