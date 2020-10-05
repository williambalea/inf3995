#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <iostream>
#include <string>
#include <vector>
#include "mysql.hpp"
#include "json.hpp"

using namespace Pistache;
using namespace std;
using json = nlohmann::json;

class Server {
public :
    explicit Server (Address addr) {
        httpEndpoint = make_shared<Http::Endpoint>(addr);
        _addr = addr;
        
        init();
    }

    void run() {
        httpEndpoint->setHandler(router.handler());
        httpEndpoint->serveThreaded();
        cout << "Server is running on " << _addr.host() << ":" << _addr.port() << " ..." << endl;
        string command = "";
        cin >> command;
        cout << "Stopping server.\n" ;
        httpEndpoint->shutdown();
    }

private:
    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address _addr;
    vector<string> msgs; // Pour livrable 1 seulement.

    void init() {
        auto opts = Http::Endpoint::options().threads(1);
        httpEndpoint->init(opts);
        setupRoutes();
    }

    void setupRoutes() {
        using namespace Rest;
        Routes::Get(router, "/", Routes::bind(&Server::welcome, this));
        Routes::Post(router, "/ip_server", Routes::bind(&Server::postIP, this));
        Routes::Get(router, "/messages", Routes::bind(&Server::getAllMsg, this));
        Routes::Put(router, "/sondage", Routes::bind(&Server::sendPoll, this));

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

    void sendPoll(const Rest::Request& req, Http::ResponseWriter res) {
        json j = json::parse(req.body());
        MySQL db;
        db.sendPoll(
            j["courriel"].get<string>(),
            j["prenom"].get<string>(),
            j["nom"].get<string>(),
            j["age"].get<int>(),
            j["interet"].get<bool>()
        );
        res.send(Http::Code::Ok, "Server got poll");
        // TODO: Http::Code::Bad_Request
    }
};

int main() {
    Address addr(Ipv4::any(), Port(2000));
    Server server(addr);
    server.run();

    //TODO: make a makefile to compile g++ server.cpp -o server -lpistache -lpthread -lmysqlcppconn
    //TODO: make .gitigore
    
    return 0;
}