#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <iostream>
#include <string>
#include <vector>

using namespace Pistache;
using namespace std;

class Server {
public :
    explicit Server (Address addr) : httpEndpoint(make_shared<Http::Endpoint>(addr)) {
        _addr = addr;
    }

    void init() {
        auto opts = Http::Endpoint::options().threads(1);
        httpEndpoint->init(opts);
        setupRoutes();
    }

    void run() {
        httpEndpoint->setHandler(router.handler());
        httpEndpoint->serveThreaded();
        cout << "Server is running on " << _addr.host() << ":" << _addr.port() << " ..." << endl;
        string command = "";
        while (command !="stop")
            cin >> command;
        cout << "Stopping server.\n" ;
        httpEndpoint->shutdown();
    }

private:
    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address _addr;
    vector<string> msgs; // Pour livrable 1 seulement.

    void setupRoutes() {
        using namespace Rest;
        Routes::Get(router, "/", Routes::bind(&Server::welcome, this));
        Routes::Post(router, "/ip_server", Routes::bind(&Server::postIP, this));
        Routes::Get(router, "/messages", Routes::bind(&Server::getAllMsg, this));

    }

    // Routes fonctions
    void welcome (const Rest::Request& req, Http::ResponseWriter res) {
        res.send(Http::Code::Ok, "Hello from server");
    }

    void postIP (const Rest::Request& req, Http::ResponseWriter res) {
        string newMsg = req.body();
        msgs.push_back(newMsg);
        res.send(Http::Code::Ok, "Server received IP : " + newMsg + " from Android");
    }

    void getAllMsg(const Rest::Request& req, Http::ResponseWriter res) {
        string buffer = "";
        for (int i = 0; i < msgs.size(); i++)
            buffer += msgs.at(i) + '\n';
        res.send(Http::Code::Ok, buffer);
    }
};

int main() {
    Address addr(Ipv4::any(), Port(2000));
    Server server(addr);
    server.init();
    server.run();
    return 0;
}