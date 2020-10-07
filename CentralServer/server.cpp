#include "server.hpp"

int main() {
    Pistache::Address addr(Ipv4::any(), Port(2000));
    MySQL db;
    Server server(addr, db);
    server.run();

    //TODO: make a makefile to compile g++ server.cpp -o server -lpistache -lpthread -lmysqlcppconn
    //TODO: make .gitigore
    
    return 0;
}