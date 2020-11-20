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

#define CHECK_ENGINE_INTERVALL 10

using namespace Pistache;
using namespace std;
using json = nlohmann::json;


static const char characters[] =
    "0123456789"
    "!@#$%^&*"
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    "abcdefghijklmnopqrstuvwxyz";

static volatile bool keepRunning = true;

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
    signal(SIGINT, interruptHandler);

    httpEndpoint->serveThreaded();
    cout << getTime() << "server is running on " << addr.host() << ":" << addr.port() << endl;

    // listening for a ctl+C
    while (keepRunning) {
        checkEnginesStatus();
        sleep(CHECK_ENGINE_INTERVALL);
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
    Routes::Get(router, "/server/", Routes::bind(&Server::newConn, this));
    Routes::Put(router, "/server/survey", Routes::bind(&Server::sendSurvey, this));
    Routes::Get(router, "/server/survey", Routes::bind(&Server::getSurvey, this));
    Routes::Get(router, "/server/user/login", Routes::bind(&Server::login, this));
    Routes::Get(router, "/server/status", Routes::bind(&Server::getStatus, this));
    Routes::Put(router, "/server/user/password", Routes::bind(&Server::changePass, this));
}

void Server::log(string msg) {
    cout << getTime() << msg << endl;
}

bool Server::checkEngine(string engineAddr) {
    bool status = false;
    http::Response res;
    try {
        http::Request request(engineAddr);
        res = request.send("GET");
    } catch (const std::exception& e) {
        cout << getTime() << "Engine at " << engineAddr << " is offline! "
        "Trying to reconnect... " << endl;    
    }

    if (res.status == http::Response::Ok)
        status = true;

    return status;
}

void Server::checkEnginesStatus() {
    enginesStatus[0] = checkEngine(ENGINE1_ADDR);
    enginesStatus[1] = checkEngine(ENGINE2_ADDR);
#if ENGINE3_ADDR
    enginesStatus[2] = checkEngine(ENGINE3_ADDR);
#endif
}

void Server::updatePass(string user, string pw) {
    string salt = generateSalt(20);
    string hashedPass = sha512(salt + pw);
    db.updatePass(user, salt, hashedPass);
}

bool Server::auth(shared_ptr<Http::Header::Authorization> authHeader) {
    bool authorized = false;
    string user = "";
    json credentials;

    if (authHeader != NULL) {
        user = authHeader.get()->getBasicUser();
        credentials = db.getUser(user);
    }

    if (!credentials.is_null()) {
        string userPass = authHeader.get()->getBasicPassword();
        string salt = credentials["salt"].get<string>();
        string dbPass = credentials["pw"].get<string>();
        string hashedPass = sha512(salt + userPass);
        authorized = (hashedPass == dbPass);
    }

    return authorized;

}

bool Server::expectedJSON(int keyCount, string keyList[], json j) {
    bool hasAllArgs = true;
    for (int i = 0; i < keyCount; i++) {
        hasAllArgs = hasAllArgs && j.contains(keyList[i]);
    }
    return hasAllArgs;
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
    auto authHeader = headers.tryGet<Http::Header::Authorization>();
    if (auth(authHeader)) {
        res.send(Http::Code::Ok, "authentified");
        log("admin authentified");
    } else {
        res.send(Http::Code::Forbidden, "Unauthorized connection!");
        log("authentification attempt failed when loging in");
    }
}

void Server::sendSurvey(const Rest::Request& req, Http::ResponseWriter res) {
    string keys[] = {"email", "firstName", "lastName", "age", "interest"};
    json body = json::parse(req.body());
    if (expectedJSON(5, keys, body)) {
        db.sendSurvey(body);
        res.send(Http::Code::Ok, "Server got your survey!");
        log("sent a survey");
    } else {
        res.send(Http::Code::Bad_Request, "Survey json format is wrong!");
        log("failed to send survey : json is wrong");
    }
}

void Server::getSurvey(const Rest::Request& req, Http::ResponseWriter res) {
    auto headers = req.headers();
    auto authHeader = headers.tryGet<Http::Header::Authorization>();
    if (auth(authHeader)) {
        json data = db.getSurvey();
        res.send(Http::Code::Ok, data.dump());
        log("sending surveys");
    } else {
        res.send(Http::Code::Bad_Request, "Unauthorized connection!");
        log("authentification attempt failed when getting surveys");
    }
}

//TODO: maybe change for a json instead of string
void Server::getStatus(const Rest::Request& req, Http::ResponseWriter res) {
    string buffer = "";
    json body;
    bool problemFlag = false;
    for(const auto& status : enginesStatus) {
        if (status) {
            buffer += "UP ";
        } else {
            buffer += "DOWN ";
            problemFlag = true;
        }
    }
    body["message"] = buffer;
    auto mime = Http::Mime::MediaType::fromString("application/json");

    if (!problemFlag)
        res.send(Http::Code::Ok, body.dump(), mime);
    else
        res.send(Http::Code::Internal_Server_Error, body.dump(), mime);
}

void Server::changePass(const Rest::Request& req, Http::ResponseWriter res) {
    auto headers = req.headers();
    auto authHeader = headers.tryGet<Http::Header::Authorization>();
    json body;
    string newPass = "";

    if (auth(authHeader)) {
        body = json::parse(req.body());
    } else {
        res.send(Http::Code::Unauthorized, "Unauthorized connection!");
        log("password change attempt failed");
        return;
    }

    string keys[] = {"new"};
    if (expectedJSON(1, keys, body)) {
        newPass = body["new"].get<string>();
    } else {
        res.send(Http::Code::Bad_Request, "Cannot get new password");
        log("failed to change password : wrong JSON");
        return;
    }

    if (!newPass.empty()) {
        string user = authHeader.get()->getBasicUser();
        updatePass(user, newPass);
        res.send(Http::Code::Ok, "Password was changed");
        log("admin changed password");
    } else {
        res.send(Http::Code::Bad_Request, "New password is blank!");
        log("failed to change password : new password is blank");
    }
}

string Server::getTime() {
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

string Server::generateSalt(int len) {
    
    string tmp_s;    
    srand(time(NULL));
    
    for (int i = 0; i < len; ++i) 
        tmp_s += characters[rand() % (sizeof(characters) - 1)];
    
    return tmp_s;
}

/*---------------------------
    Global Functions
---------------------------*/
void interruptHandler(int signum) {
    keepRunning = false;
}