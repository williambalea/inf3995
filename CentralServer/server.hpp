#ifndef SERVER_HPP
#define SERVER_HPP

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
    explicit Server (Address &addr_, MySQL &db_);
    void run();

private:
    void init();
    void setupRoutes();

    // Routes fonctions
    void welcome  (const Rest::Request& req, Http::ResponseWriter res);
    void postIP   (const Rest::Request& req, Http::ResponseWriter res);
    void getAllMsg(const Rest::Request& req, Http::ResponseWriter res);
    void sendPoll (const Rest::Request& req, Http::ResponseWriter res);

    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address addr;
    MySQL db;
    vector<string> msgs; // Pour livrable 1 seulement.
};


Server::Server(Address &addr_, MySQL &db_) {
    httpEndpoint = make_shared<Http::Endpoint>(addr_);
    addr = addr_;
    db = db_;
    
    init();
}

void Server::run() {
    httpEndpoint->setHandler(router.handler());
    httpEndpoint->serveThreaded();
    cout << "Server is running on " << addr.host() << ":" << addr.port() << " ..." << endl;
    string command = "";
    while (command != "stop")
        cin >> command;
    cout << "Stopping server.\n" ;
    httpEndpoint->shutdown();
}

void Server::init() {
    auto opts = Http::Endpoint::options().threads(1);
    httpEndpoint->init(opts);
    setupRoutes();
}

void Server::setupRoutes() {
    using namespace Rest;
    Routes::Get(router, "/server/", Routes::bind(&Server::welcome, this));
    Routes::Post(router, "/server/ip_server", Routes::bind(&Server::postIP, this));
    Routes::Get(router, "/server/messages", Routes::bind(&Server::getAllMsg, this));
    Routes::Post(router, "/server/survey", Routes::bind(&Server::sendPoll, this));

}

// Routes fonctions
void Server::welcome (const Rest::Request& req, Http::ResponseWriter res) {
    res.send(Http::Code::Ok, "Hello from server");
    cout << "connection established" << endl;
}

void Server::postIP (const Rest::Request& req, Http::ResponseWriter res) {
    string newMsg = req.body();
    msgs.push_back(newMsg);
    res.send(Http::Code::Ok, "Server received IP : " + newMsg + " from Android");
    cout << newMsg << endl;
}

void Server::getAllMsg(const Rest::Request& req, Http::ResponseWriter res) {
    string buffer = "";
    for (int i = 0; i < msgs.size(); i++)
        buffer += msgs.at(i) + '\n';
    res.send(Http::Code::Ok, buffer);
}

void Server::sendPoll(const Rest::Request& req, Http::ResponseWriter res) {
    json j = json::parse(req.body());
    db.sendPoll(
        j["email"].get<string>(),
        j["firstName"].get<string>(),
        j["lastName"].get<string>(),
        j["age"].get<int>(),
        j["interest"].get<bool>()
    );
    res.send(Http::Code::Ok, "Server got poll");
    // TODO: Http::Code::Bad_Request
}

#endif // SERVER_HPP