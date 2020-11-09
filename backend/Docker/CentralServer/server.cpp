#include "server.hpp"
#include <iostream>
#include <string>
#include "json.hpp"
#include "customUtilities.hpp"

using namespace Pistache;
using namespace std;
using json = nlohmann::json;

volatile sig_atomic_t sigint = 0;

/*---------------------------
    Server Class Functions
---------------------------*/
Server::Server(Address &_addr, MySQL &_db) 
    : ws(enginesStatus)
    , httpEndpoint(make_shared<Http::Endpoint>(_addr))
    , addr(_addr)
    , db(_db) 
{    
    init();
}

void Server::run() {
    setSIGINTListener();

    httpEndpoint->serveThreaded();
    cout << Custom::getTime() << "server is running on " << addr.host() << ":" << addr.port() << endl;
    
    // TODO: remove dummies
    //createDummies();

    // listening for a ctl+C
    while (!sigint) {
        maintainWsConnection();
    }

    httpEndpoint->shutdown();
    cout << Custom::getTime() << "server closed\n" << endl;
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
    Routes::Get (router, "/server/engine/status", Routes::bind(&Server::getEngineStatus, this));
}

void Server::setSIGINTListener() {
    signal(SIGINT, intHandler);
}

void Server::log(string msg) {
    cout << Custom::getTime() << msg << endl;
}

void Server::maintainWsConnection() {
    sleep(2);
    if (!enginesStatus[0])
        ws.reconnectEngine(0);
    if (!enginesStatus[1])
        ws.reconnectEngine(1);
    if (!enginesStatus[2])
        ws.reconnectEngine(2);
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

// TODO: ajust
void Server::getEngineStatus(const Rest::Request& req, Http::ResponseWriter res) {
    string buffer;
    for(const auto& it : enginesStatus) {
        buffer += it ? "true " : "false ";
    } 
    res.send(Http::Code::Ok, buffer);
}

/*---------------------------
    Global Functions
---------------------------*/
void intHandler(int signum) {
    sigint = 1;
}

// TODO: remove
void Server::createDummies() {
    cout << Custom::getTime() << "creating dummies ";
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