#include "meta.hpp"
#include "customUtilities.hpp"

Meta::Meta(int id, websocketpp::connection_hdl hdl, string uri, bool &engineStatus)
    : m_id(id)
    , m_hdl(hdl)
    , m_status("Connecting")
    , m_uri(uri)
    , m_server("N/A")
    , m_engineStatus(&engineStatus)
{}

void Meta::on_open(client * c, websocketpp::connection_hdl hdl) {
    m_status = "Open";

    client::connection_ptr con = c->get_con_from_hdl(hdl);
    m_server = con->get_response_header("Server");

    *m_engineStatus = true;

    cout 
    << Custom::getTime()
    << "Connected to engine" << m_id + 1 
    << " on " << m_uri 
    << " with id " << m_id
    << endl;

}

void Meta::on_fail(client * c, websocketpp::connection_hdl hdl) {
    m_status = "Failed";

    client::connection_ptr con = c->get_con_from_hdl(hdl);
    m_server = con->get_response_header("Server");
    m_error_reason = con->get_ec().message();
}

void Meta::on_close(client * c, websocketpp::connection_hdl hdl) {
    m_status = "Closed";
    client::connection_ptr con = c->get_con_from_hdl(hdl);
    auto closeCode = con->get_remote_close_code();
    string closeName = websocketpp::close::status::get_string(closeCode);
    string closeReason = con->get_remote_close_reason();

    *m_engineStatus = false;

    cout 
    << Custom::getTime()
    << "Disconnected from engine " << m_id + 1 
    << " on " << m_uri 
    << " with id " << m_id
    << endl;
}

websocketpp::connection_hdl Meta::get_hdl() const {
    return m_hdl;
}

int Meta::get_id() const {
    return m_id;
}

string Meta::get_status() const {
    return m_status;
}
