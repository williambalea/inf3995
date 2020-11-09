#ifndef META_HPP
#define META_HPP

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <stdlib.h>
#include <iostream>
#include <map>
#include <string>

using namespace std;

typedef websocketpp::client<websocketpp::config::asio_client> client;

class Meta {
public:
    typedef shared_ptr<Meta> ptr;

    Meta(int id, websocketpp::connection_hdl hdl, string uri, bool &engineStatus);

    void on_open(client * c, websocketpp::connection_hdl hdl);

    void on_fail(client * c, websocketpp::connection_hdl hdl);
    
    void on_close(client * c, websocketpp::connection_hdl hdl);

    websocketpp::connection_hdl get_hdl() const;
    
    int get_id() const;
    
    string get_status() const;

private:
    int m_id;
    websocketpp::connection_hdl m_hdl;
    string m_status;
    string m_uri;
    string m_server;
    string m_error_reason;
    bool *m_engineStatus;

};

#endif