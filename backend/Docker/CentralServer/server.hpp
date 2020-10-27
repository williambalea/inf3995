#ifndef SERVER_HPP
#define SERVER_HPP

#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <iostream>
#include <string>
#include <signal.h>
#include <ctime>
#include "mysql.hpp"
#include "json.hpp"

using namespace Pistache;
using namespace std;
using json = nlohmann::json;

volatile sig_atomic_t sigint;
void intHandler(int signum);
string getTime();

class Server {
public :
    explicit Server (Address &addr_, MySQL &db_);
    void run();

private:
    void init();
    void setupRoutes();
    void setSIGINTListener();
    void createDummies();
    void log(string msg);

    // Routes fonctions
    void newConn(const Rest::Request& req, Http::ResponseWriter res);
    void login(const Rest::Request& req, Http::ResponseWriter res);
    void sendPoll(const Rest::Request& req, Http::ResponseWriter res);
    void getPolls(const Rest::Request& req, Http::ResponseWriter res);

    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address addr;
    MySQL db;
};

/*---------------------------
    Server Class Functions
---------------------------*/
Server::Server(Address &addr_, MySQL &db_) {
    httpEndpoint = make_shared<Http::Endpoint>(addr_);
    addr = addr_;
    db = db_;
    
    init();
}

void Server::run() {
    setSIGINTListener();

    httpEndpoint->serveThreaded();
    cout << getTime() << "server is running on " << addr.host() << ":" << addr.port() << endl;
    
    // TODO: remove dummies
    createDummies();

    // listening for a ctl+C
    while (!sigint) {}

    httpEndpoint->shutdown();
    cout << "server closed\n";
}

void Server::init() {
    auto opts = Http::Endpoint::options().threads(1);
    httpEndpoint->init(opts);
    setupRoutes();
    httpEndpoint->setHandler(router.handler());    
}

void Server::setupRoutes() {
    using namespace Rest;
    Routes::Get (router, "/server/", Routes::bind(&Server::newConn, this));
    Routes::Post(router, "/server/survey", Routes::bind(&Server::sendPoll, this));
    Routes::Get (router, "/server/survey", Routes::bind(&Server::getPolls, this));
    Routes::Get (router, "/server/user/login", Routes::bind(&Server::login, this));
}

void Server::setSIGINTListener() {
    signal(SIGINT, intHandler);
    sigint = 0;
}

void Server::log(string msg) {
    cout << getTime() << msg << endl;
}

/*---------------------------
    Routes Fonction
---------------------------*/
void Server::newConn(const Rest::Request& req, Http::ResponseWriter res) {
    res.send(Http::Code::Ok, "Connected to server!");
    log("new connection");
}

void Server::login(const Rest::Request& req, Http::ResponseWriter res) {
    auto headers = req.headers();
    auto auth = headers.tryGet<Http::Header::Authorization>();
    bool access = false;
    if (auth != NULL) {
        string user = auth.get()->getBasicUser();
        string pass = auth.get()->getBasicPassword();
        if (user == "admin" && pass == "admin") {
            res.send(Http::Code::Ok, "authentified");
            log("admin authentified");
            return;
        }
    }
    res.send(Http::Code::Unauthorized, "Unauthorized connection!");
    log("authentification attempt failed");
}

void Server::sendPoll(const Rest::Request& req, Http::ResponseWriter res) {
    try {
        json j = json::parse(req.body());
        db.sendPoll(
            j["email"].get<string>(),
            j["firstName"].get<string>(),
            j["lastName"].get<string>(),
            j["age"].get<int>(),
            j["interest"].get<bool>()
        );
        res.send(Http::Code::Ok, "Server got your survey!");
        log("sent a survey");

    } catch (json::exception &e) {
        res.send(Http::Code::Bad_Request, e.what());
        string msg = e.what();
        log("failed to send survey " + msg);
    }
    
}

void Server::getPolls(const Rest::Request& req, Http::ResponseWriter res) {
    bool err;
    json data = db.getPolls(err);
    if (!err) {
        res.send(Http::Code::Ok, data.dump());
        log("requested surveys");
    } else {
        res.send(Http::Code::Bad_Request, "server couldn't connect to database.");
        log("failed to request surveys. Can't connect to database.");
    }

}

/*---------------------------
    Global Functions
---------------------------*/
void intHandler(int signum) {
    sigint = 1;
}

string getTime() {
    time_t now;
    struct tm local;
    now = time(NULL);
    local = *localtime(&now);

    string h = to_string(local.tm_hour);
    string m = to_string(local.tm_min);
    string s = local.tm_sec < 10 ? '0' + to_string(local.tm_sec) : to_string(local.tm_sec);
    string d = to_string(local.tm_mday);
    string t = to_string(local.tm_mon + 1);
    string y = to_string(local.tm_year + 1900);


    string buffer = "";
    buffer += '[' + h + ':' + m + ':' + s + "] " + d + '/' + t + '/' + y + " > ";
    return buffer;
}

// TODO: remove
void Server::createDummies() {
    cout << getTime() << "creating dummies ";
    cout.flush();
    for (int i = 0; i < 20; i++) {
        if (sigint) break;
        json j;
        j["email"] = to_string(i) + "@poly.ca";
        j["firstName"] = to_string('a' + i);
        j["lastName"] = to_string('A' + i);
        j["age"] = i + 100;
        j["interest"] = (i % 2) ? true : false;
        db.sendPoll(
            j.at("email"),
            j.at("firstName"),
            j.at("lastName"),
            j.at("age"),
            j.at("interest")
        );
        cout << ".";
        cout.flush();
    }
    cout << " Done!\n";
} // createDummies

#endif // SERVER_HPP