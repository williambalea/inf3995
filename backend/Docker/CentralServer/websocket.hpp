#ifndef WEBSOCKET_HPP
#define WEBSOCKET_HPP

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <stdlib.h>
#include <iostream>
#include <map>
#include <string>
#include "meta.hpp"

#define ENGINE1_ADDR "ws://localhost:3000"
#define ENGINE2_ADDR "ws://localhost:3000"
#define ENGINE3_ADDR "ws://localhost:3000"

using namespace std;

typedef websocketpp::client<websocketpp::config::asio_client> client;

class Websocket {
public:
    Websocket (bool *enginesStatus);

    ~Websocket();

    int connect(string const & uri, int id);

    Meta::ptr get_metadata(int id) const;

    void connectToEngines();

private:
    typedef map<int,Meta::ptr> con_list;

    client m_endpoint;
    shared_ptr<thread> m_thread;

    con_list m_connection_list;
    bool *m_enginesStatus;
};

#endif