#include "server.hpp"
#include <iostream>
#include <string>
#include <ctime>
#include "json.hpp"
#include "HTTPRequest.hpp"
#include "sha512.h"

#define ENGINE1_ADDR "http://engine1:5000/engine1"
#define ENGINE2_ADDR "http://engine2:5000/engine2"

//TODO: Correct address when engine3 will be created
#define ENGINE3_ADDR "http://engine1:5000/engine1"

using namespace Pistache;
using namespace std;
using json = nlohmann::json;

volatile sig_atomic_t sigint = 0;

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
    while (!sigint) {
        checkEnginesStatus();
        sleep(10);
    }

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
    Routes::Get (router, "/server/status", Routes::bind(&Server::getStatus, this));
    Routes::Put (router, "/server/user/password", Routes::bind(&Server::changePass, this));
}

void Server::setSIGINTListener() {
    signal(SIGINT, intHandler);
}

void Server::log(string msg) {
    cout << getTime() << msg << endl;
}

bool Server::checkEngine(string engineAddr) {
    bool status = false;
    try {
        http::Request request(engineAddr);
        const http::Response response = request.send("GET");

        if (response.status == 200) {
            status = true;
        } else {
            cout << getTime() << "Engine at " << engineAddr << " is offline! "
            "Trying to reconnect... " << endl;
        }
    } catch (const std::exception& e) {
        cout << getTime() << "Engine at " << engineAddr << " is offline! "
        "Trying to reconnect... " << endl;    
    }

    return status;
}

void Server::checkEnginesStatus() {
    enginesStatus[0] = checkEngine(ENGINE1_ADDR);
    enginesStatus[1] = checkEngine(ENGINE2_ADDR);
    enginesStatus[2] = checkEngine(ENGINE3_ADDR);
}

void Server::updatePass(string user, string pw) {
    string salt = genRandomString(20);
    string hashedPass = hash10times(salt, pw);
    db.updatePass(user, salt, hashedPass);
}

/*---------------------------
    Routes Fonction
---------------------------*/
void Server::newConn(const Rest::Request& req, Http::ResponseWriter res) {
    res.send(Http::Code::Ok, "Connected to server!");
    log("new connection");
}

void Server::login(const Rest::Request& req, Http::ResponseWriter res) {
    bool err;
    auto headers = req.headers();
    auto auth = headers.tryGet<Http::Header::Authorization>();
    if (auth != NULL) {
        string user = auth.get()->getBasicUser();
        string pass = auth.get()->getBasicPassword();
        json credential = db.getUser(user, err);
        if (err) {
            res.send(Http::Code::Bad_Request, "server couldn't connect to database.");
            log("failed to login user. Can't connect to database.");
            return;
        }
        if (credential.dump() != "null") {
            string hashedPass = hash10times(credential["salt"].get<string>(), pass);
            bool matchingUsers = user == credential["user"].get<string>();
            bool matchingPass = hashedPass == credential["pw"].get<string>();
            if ( matchingUsers && matchingPass) {
                res.send(Http::Code::Ok, "authentified");
                log("admin authentified");
                return;
            }
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

//TODO: maybe change for a json instead of string
void Server::getStatus(const Rest::Request& req, Http::ResponseWriter res) {
    string buffer = "";
    for(const auto& it : enginesStatus) {
        buffer += it ? "true " : "false ";
    }
    res.send(Http::Code::Ok, buffer);
}

void Server::changePass(const Rest::Request& req, Http::ResponseWriter res) {
    bool err;
    auto headers = req.headers();
    auto auth = headers.tryGet<Http::Header::Authorization>();
    if (auth != NULL) {
        string user = auth.get()->getBasicUser();
        string pass = auth.get()->getBasicPassword();
        string newPass = "";
        try {
            json j = json::parse(req.body());
            newPass = j["new"].get<string>();
        } catch (json::exception &e) {
            res.send(Http::Code::Bad_Request, "Cannot get new password");
            string msg = e.what();
            log("failed to changed password " + msg);
            return;
        }
        json credential = db.getUser(user, err);
        if (err) {
            res.send(Http::Code::Bad_Request, "server couldn't connect to database.");
            log("failed to change password. Can't connect to database.");
            return;
        }
        if (credential.dump() != "null") {
            string hashedPass = hash10times(credential["salt"].get<string>(), pass);
            bool matchingUsers = user == credential["user"].get<string>();
            bool matchingPass = hashedPass == credential["pw"].get<string>();
            if ( matchingUsers && matchingPass) {
                updatePass(user, newPass);
                res.send(Http::Code::Ok, "password was changed");
                log("admin changed password");
                return;
            }
        }
    }
    res.send(Http::Code::Unauthorized, "Unauthorized connection!");
    log("password change attempt failed");
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

string genRandomString(int len) {
    
    string tmp_s;    
    srand(time(NULL));
    
    for (int i = 0; i < len; ++i) 
        tmp_s += characters[rand() % (sizeof(characters) - 1)];
    
    
    return tmp_s;
}

string hash10times(string salt, string pass) {
    string hashed = salt + pass;
    for (int i = 0; i < 10; i++ ) {
        hashed = sha512(hashed);
    }
    return hashed;
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