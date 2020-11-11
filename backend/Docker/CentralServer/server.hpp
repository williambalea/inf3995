#ifndef SERVER_HPP
#define SERVER_HPP

#include <pistache/endpoint.h>
#include <pistache/router.h>
#include <pistache/http.h>
#include <signal.h>
#include "mysql.hpp"

using namespace Pistache;
using namespace std;

/**
 * Used to track the SIGINT signal that is called when ctl+C to close server
 */
extern volatile sig_atomic_t sigint;

static const char characters[] =
    "0123456789"
    "!@#$%^&*"
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    "abcdefghijklmnopqrstuvwxyz";

/**
 * Handler when ctl+C in command line. Sets the signum to 1 to end the 
 * while loop in run()
 * 
 * @param signum Holds the signal status. Becomes 1 when we ctl+C
 */
void intHandler(int signum);

/**
 * Gets the time and date then formats for the logs like this:
 * [HH:MM:SS] dd/mm/yyyy > 
 * To be used in log() function only
 */
string getTime();

string genRandomString(int len);

string hash10times(string salt, string pass);

class Server {
public :
    /**
     * Makes an endpoint with addr_ then calls init()
     * 
     * @param addr_ The address used for the server
     * @param db_   The mysql database used by the server
     */
    explicit Server (Address &addr_, MySQL &db_);

    /**
     * Opens the server on addr and listens for SIGINT signal from ctl+C
     * with a while loop. When there's a SIGINT, the server is closed. 
     * Because of while loop, this should be called last in main()
     */
    void run();

private:

    /**
     * Sets options for the endpoint to use threads. Sets up the
     * HTTP router and routes.
     */
    void init();

    /**
     * Creates the different HTTP routes and their handlers
     */
    void setupRoutes();

    /**
     * Links the SIGINT signal to a handler
     */
    void setSIGINTListener();

    // TODO: to remove
    void createDummies();

    void checkEnginesStatus();

    bool checkEngine(string engineAddr);

    /**
     * Gets the time from getTime() then appends the msg before cout
     * 
     * @param msg The message the be shown as log in command line
     */
    void log(string msg);

    /**
     * Responds with http 200 "Connected to server!" to know that server is running
     * 
     * @param req The HTTP request containing headers, body, etc.
     * @param res The HTTP response containing the http code and server response
     */
    void newConn(const Rest::Request& req, Http::ResponseWriter res);

    /**
     * Admin auth of the PC application
     * Responds with http 200 "authentified" if successful
     * responds with http 401 "Unauthorized connection!" if not
     * 
     * @param req The HTTP request containing headers, body, etc.
     * @param res The HTTP response containing the http code and server response
     */
    void login(const Rest::Request& req, Http::ResponseWriter res);

    /**
     * Sends completed survey received from Android app to the database.
     * Responds with http 200 "Server got your survey!" when successful
     * Responds with http 400 with the error when not. 
     * 
     * @param req The HTTP request containing headers, body, etc.
     * @param res The HTTP response containing the http code and server response
     */
    void sendPoll(const Rest::Request& req, Http::ResponseWriter res);

    /**
     * Gets all the surveys from the database and returns it to the Android app.
     * Responds with http 200 and all the surveys in json format if successful
     * Responds with http 400 "Server couldn't connect to database" if not.
     * 
     * @param req The HTTP request containing headers, body, etc.
     * @param res The HTTP response containing the http code and server response
     */
    void getPolls(const Rest::Request& req, Http::ResponseWriter res);

    /**
     * Return the online status of the 3 engines. True means that the engine is online
     * 
     * @param req The HTTP request containing headers, body, etc.
     * @param res The HTTP response containing the http code and server response
     */
    void getStatus(const Rest::Request& req, Http::ResponseWriter res);

    shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    Address addr;
    MySQL db;
    bool enginesStatus[3] = {false, false, false};
};

#endif // SERVER_HPP