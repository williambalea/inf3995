MYSQL_LIBS=-lmysqlcppconn
SERVER_LIBS=-lpistache -lpthread
ALL_LIBS=$(MYSQL_LIBS) $(SERVER_LIBS)
CFLAGS=-Wall -Wextra -g -Wno-unused-parameter

output: main.o server.o mysql.o
	g++ main.o server.o mysql.o -o server $(CFLAGS) $(ALL_LIBS)

main.o: main.cpp
	g++ -c $(CFLAGS) main.cpp

server.o: server.cpp server.hpp
	g++ -c $(CFLAGS) server.cpp $(SERVER_LIBS)

mysql.o: mysql.cpp mysql.hpp
	g++ -c $(CFLAGS) mysql.cpp $(MYSQL_LIBS)

clean:
	rm *.o server
