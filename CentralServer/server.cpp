#include "server.hpp"

int main() {
    Address addr(Ipv4::any(), Port(2000));
    Server server(addr);
    server.init();
    server.run();
    return 0;
}