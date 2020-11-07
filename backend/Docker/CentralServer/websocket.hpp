#ifndef WEBSOCKET
#define WEBSOCKET

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <websocketpp/common/thread.hpp>
#include <websocketpp/common/memory.hpp>
#include <map>
#include "WSMeta.hpp"

using namespace std;
typedef websocketpp::client<websocketpp::config::asio_client> client;

class Websocket {
public:
    Websocket();
    ~Websocket();

    int connect(string const &uri);
    void close(int id, websocketpp::close::status::value code, string reason);
    void send(int id, string message);
    shared_ptr<WSMeta> get_metadata(int id) const;
private:
    typedef map<int,shared_ptr<WSMeta>> connList;

    client m_endpoint;
    shared_ptr<thread> m_thread;
    int m_nextId;
    connList m_connections;
};

#endif
