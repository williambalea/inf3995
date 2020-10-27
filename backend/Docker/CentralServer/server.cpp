#include "server.hpp"

int main() {
    Address addr(Ipv4::any(), Port(2000));
    MySQL db;
    Server server(addr, db);
    server.run(); // while()
    return 0;
}