
#include "pistache/endpoint.h"

using namespace Pistache;
using namespace Net;


struct HelloHandler : public Http::Handler {
public:
    HTTP_PROTOTYPE(HelloHandler)
    void onRequest(const Http::Request& req, Http::ResponseWriter response) {
        response.send(Http::Code::Ok, "Hello, World");
    }
};

int main() {
    Address addr(Ipv4::any(), Port(9082));
    auto opts = Http::Endpoint::options().threads(1);
    Http::Endpoint server(addr);
    server.init(opts);
    server.setHandler(Http::make_handler<HelloHandler>());
    server.serve();
    server.shutdown();
}