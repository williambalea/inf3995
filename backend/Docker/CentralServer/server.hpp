#ifndef SERVER_HPP
#define SERVER_HPP

#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <signal.h>
#include "mysql.hpp"

using namespace Pistache;
using namespace std;

extern volatile sig_atomic_t sigint;
static const char characters[] =
    "0123456789"
    "!@#$%^&*"
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    "abcdefghijklmnopqrstuvwxyz";

void intHandler(int signum);
string getTime();
string genRandomString(int len);

class Server {
public :
    explicit Server (Address &addr_, MySQL &db_);
    void run();

private:
    void init();
    void setupRoutes();
    void setSIGINTListener();
    void checkEnginesStatus();
    bool checkEngine(string engineAddr);
    void updatePass(string user, string pw);
    void log(string msg);
    bool auth(shared_ptr<Http::Header::Authorization> authHeader);
    bool expectedJSON(int keyCount, string keyList[], json j);

    void newConn(const Rest::Request& req, Http::ResponseWriter res);
    void login(const Rest::Request& req, Http::ResponseWriter res);
    void sendSurvey(const Rest::Request& req, Http::ResponseWriter res);
    void getSurvey(const Rest::Request& req, Http::ResponseWriter res);
    void getStatus(const Rest::Request& req, Http::ResponseWriter res);
    void changePass(const Rest::Request& req, Http::ResponseWriter res);

    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address addr;
    MySQL db;
    bool enginesStatus[3] = {false, false, false};
};

#endif // SERVER_HPP