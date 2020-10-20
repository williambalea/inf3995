#ifndef SERVER_HPP
#define SERVER_HPP

#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <iostream>
#include <string>
#include <signal.h>
#include "mysql.hpp"
#include "json.hpp"

using namespace Pistache;
using namespace std;
using json = nlohmann::json;

volatile sig_atomic_t stop;

void intHandler(int signum) {
    stop = 1;
}

class Server {
public :
    explicit Server (Address &addr_, MySQL &db_);
    void run();

private:
    void init();
    void setupRoutes();
    void createDummies();

    // Routes fonctions
    void newConn  (const Rest::Request& req, Http::ResponseWriter res);
    void sendPoll (const Rest::Request& req, Http::ResponseWriter res);
    void getPolls  (const Rest::Request& req, Http::ResponseWriter res);

    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address addr;
    MySQL db;
};


Server::Server(Address &addr_, MySQL &db_) {
    httpEndpoint = make_shared<Http::Endpoint>(addr_);
    addr = addr_;
    db = db_;
    
    init();
}

void Server::run() {
    // For ctl+C
    signal(SIGINT, intHandler);
    stop = 0;

    httpEndpoint->setHandler(router.handler());
    httpEndpoint->serveThreaded();
    cout << "Server is running on " << addr.host() << ":" << addr.port() << endl;
    
    // TODO: remove dummies
    createDummies();

    // listening for a ctl+C
    while (!stop) {}

    cout << "shutting down...\n";
    httpEndpoint->shutdown();
}

void Server::init() {
    auto opts = Http::Endpoint::options().threads(1);
    httpEndpoint->init(opts);
    setupRoutes();
}

void Server::setupRoutes() {
    using namespace Rest;
    Routes::Get (router, "/server/", Routes::bind(&Server::newConn, this));
    Routes::Post(router, "/server/survey", Routes::bind(&Server::sendPoll, this));
    Routes::Get (router, "/server/survey", Routes::bind(&Server::getPolls, this)); 
}

// Routes fonctions
void Server::newConn (const Rest::Request& req, Http::ResponseWriter res) {
    res.send(Http::Code::Ok, "Connected to server!");
    cout << "new connection established" << endl;
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

void Server::getPolls(const Rest::Request& req, Http::ResponseWriter res) {
    json data = db.getPolls();
    cout << "sending polls to PC" << endl;
    res.send(Http::Code::Ok, data.dump());
}

// TODO: remove
void Server::createDummies() {
    cout << "creating dummies ";
    cout.flush();
    for (int i = 0; i < 20; i++) {
        if (stop) break;
        json j;
        j["email"] = to_string(i) + "@poly.ca";
        j["firstName"] = to_string('a' + i);
        j["lastName"] = to_string('A' + i);
        j["age"] = i;
        j["interest"] = (i % 2) ? true : false;
        db.sendPoll(
            j["email"].get<string>(),
            j["firstName"].get<string>(),
            j["lastName"].get<string>(),
            j["age"].get<int>(),
            j["interest"].get<bool>()
        );
        cout << ".";
        cout.flush();
    }
    cout << " Done!\n";
} // createDummies

#endif // SERVER_HPP