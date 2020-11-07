#include "websocket.hpp"

using namespace std;
typedef websocketpp::client<websocketpp::config::asio_client> client;

Websocket::Websocket() : m_nextId(0) {
    m_endpoint.clear_access_channels(websocketpp::log::alevel::all);
    m_endpoint.clear_error_channels(websocketpp::log::elevel::all);

    m_endpoint.init_asio();
    m_endpoint.start_perpetual();

    m_thread = make_shared<std::thread>(&client::run, &m_endpoint);
}

Websocket::~Websocket() {
    m_endpoint.stop_perpetual();

    for (const auto& connection : m_connections) {
        if (connection.second->get_status() != "Open") continue;
        
        cout << "Closing socket " << connection.second->get_id() << endl;

        error_code e;
        m_endpoint.close(connection.second->get_hdl(), websocketpp::close::status::going_away, "", e);
        if (e) {
            cout << "Error closing socket" << connection.second->get_id()
                 << " : " << e.message() << endl;
        } 
    }

    m_thread->join();
}

int Websocket::connect(string const &uri) {

}

void Websocket::close(int id, websocketpp::close::status::value code, string reason) {

}

void Websocket::send(int id, string message) {

}

shared_ptr<WSMeta> Websocket::get_metadata(int id) const {

}
