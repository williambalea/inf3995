#include "WSMeta.hpp"

using namespace std;
typedef websocketpp::client<websocketpp::config::asio_client> client;

WSMeta::WSMeta(int id, websocketpp::connection_hdl hdl, string uri)
    : m_id(id)
    , m_hdl(hdl)
    , m_status("Connecting")
    , m_uri(uri)
    , m_server("N/A")
    {}

    void WSMeta::open(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Open";
        client::connection_ptr con = c->get_con_from_hdl(hdl);
        m_server = con->get_response_header("Server");
    }

    void WSMeta::fail(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Failed";
        client::connection_ptr con = c->get_con_from_hdl(hdl);
        m_server = con->get_response_header("Server");
        m_errorReason = con->get_ec().message();
    }

    void WSMeta::close(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Closed";
        client::connection_ptr con = c->get_con_from_hdl(hdl);
        auto closeCode = con->get_remote_close_code();
        string closeName = websocketpp::close::status::get_string(closeCode);
        string closeReason = con->get_remote_close_reason();

        // TODO: closing handle for android app request
    }

    websocketpp::connection_hdl WSMeta::get_hdl() {

    }

    int WSMeta::get_id() const {

    }

    string WSMeta::get_status() const {

    }
