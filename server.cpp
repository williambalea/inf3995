#include "pistache/endpoint.h"

using namespace Pistache;
using namespace Http;

class HelloHandler : public Http::Handler {
public:

    HTTP_PROTOTYPE(HelloHandler)

    void onRequest(const Http::Request& req, Http::ResponseWriter response) override {
            if (req.resource() == "/ip_server") {
                if(req.method() == Http::Method::Post) {
                    response.send(Http::Code::Ok, "Server received IP:" + req.body() + " " + "from Android");
                }
            } else {
                response.send(Http::Code::Ok, "Hello World From Server");
            }
        }
};

int main() {
    Address addr(Ipv4::any(), Port(2000));
    auto opts = Http::Endpoint::options().threads(1);

    Http::Endpoint server(addr);
    server.init(opts);
    server.setHandler(Http::make_handler<HelloHandler>());
    server.serve();
    server.shutdown();
}