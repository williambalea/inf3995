
#include "./pistache/"

using namespace Pistache;
using namespace Net;


struct HelloHandler : public Http::Handler {
    void onRequest(const Http::Request& req, Http::ResponseWriter response) {
        response.send(Http::Code::Ok, "Hello, World");
    }
};

int main() {
    Http::listenAndServe<HelloHandler>("*:9080");
}