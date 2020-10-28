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

#endif // SERVER_HPP