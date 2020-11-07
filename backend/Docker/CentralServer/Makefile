LIBS=-lmysqlcppconn -lpistache -lpthread -lboost_system
CFLAGS=-Wall -Wextra -g -Wno-unused-parameter
CC=g++

output: main.o server.o mysql.o wsmeta.o websocket.o
	$(CC) main.o server.o mysql.o -o server $(CFLAGS) $(LIBS)

main.o: main.cpp
	$(CC) -c $(CFLAGS) main.cpp

server.o: server.cpp server.hpp
	$(CC) -c $(CFLAGS) server.cpp

mysql.o: mysql.cpp mysql.hpp
	$(CC) -c $(CFLAGS) mysql.cpp

wsmeta.o: WSMeta.cpp WSMeta.hpp
	$(CC) -c $(CFLAGS) WSMeta.cpp

websocket.o: websocket.cpp websocket.hpp
	$(CC) -c $(CFLAGS) websocket.cpp

clean:
	rm *.o server
