#ifndef WS_META
#define WS_META

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <websocketpp/common/thread.hpp>
#include <websocketpp/common/memory.hpp>

using namespace std;
typedef websocketpp::client<websocketpp::config::asio_client> client;

class WSMeta {
public:
    WSMeta(int id, websocketpp::connection_hdl hdl, std::string uri);
    void open(client * c, websocketpp::connection_hdl hdl);
    void fail(client * c, websocketpp::connection_hdl hdl);
    void close(client * c, websocketpp::connection_hdl hdl);

    websocketpp::connection_hdl get_hdl();
    int get_id() const;
    std::string get_status() const;


private:
    int m_id;
    websocketpp::connection_hdl m_hdl;
    string m_status;
    string m_uri;
    string m_server;
    string m_errorReason;
};

#endif