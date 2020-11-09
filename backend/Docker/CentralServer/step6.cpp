
// g++ step6.cpp -lboost_system -D_WEBSOCKETPP_CPP11_STL_ -lpthread

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <stdlib.h>
#include <iostream>
#include <map>
#include <string>

#define ENGINE1_ADDR "ws://localhost:3000"
#define ENGINE2_ADDR "ws://localhost:3000"
#define ENGINE3_ADDR "ws://localhost:3000"

using namespace std;

typedef websocketpp::client<websocketpp::config::asio_client> client;

class Meta {
public:
    typedef shared_ptr<Meta> ptr;

    Meta(int id, websocketpp::connection_hdl hdl, string uri, bool &engineStatus)
      : m_id(id)
      , m_hdl(hdl)
      , m_status("Connecting")
      , m_uri(uri)
      , m_server("N/A")
      , m_engineStatus(&engineStatus)
    {}

    void on_open(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Open";

        client::connection_ptr con = c->get_con_from_hdl(hdl);
        m_server = con->get_response_header("Server");

        *m_engineStatus = true;

        cout 
        << "Connected to engine " << m_id + 1 
        << " on " << m_server 
        << " with id " << m_id
        << endl;

    }

    void on_fail(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Failed";

        client::connection_ptr con = c->get_con_from_hdl(hdl);
        m_server = con->get_response_header("Server");
        m_error_reason = con->get_ec().message();
    }
    
    void on_close(client * c, websocketpp::connection_hdl hdl) {
        m_status = "Closed";
        client::connection_ptr con = c->get_con_from_hdl(hdl);
        auto closeCode = con->get_remote_close_code();
        string closeName = websocketpp::close::status::get_string(closeCode);
        string closeReason = con->get_remote_close_reason();

        *m_engineStatus = false;

        cout 
        << "Disconnected from engine " << m_id + 1 
        << " on " << m_server 
        << " with id " << m_id
        << endl;
    }

    websocketpp::connection_hdl get_hdl() const {
        return m_hdl;
    }
    
    int get_id() const {
        return m_id;
    }
    
    string get_status() const {
        return m_status;
    }

private:
    int m_id;
    websocketpp::connection_hdl m_hdl;
    string m_status;
    string m_uri;
    string m_server;
    string m_error_reason;
    bool *m_engineStatus;

};

class Websocket {
public:
    Websocket (bool *enginesStatus)
    : m_next_id(0)
    , m_enginesStatus(enginesStatus) {
        
        m_endpoint.clear_access_channels(websocketpp::log::alevel::all);
        m_endpoint.clear_error_channels(websocketpp::log::elevel::all);

        m_endpoint.init_asio();
        m_endpoint.start_perpetual();

        m_thread = make_shared<thread>(&client::run, &m_endpoint);

        connectToEngines();
    }

    ~Websocket() {
        m_endpoint.stop_perpetual();
        
        for (const auto& it : m_connection_list) {
            if (it.second->get_status() != "Open") continue;
            
            cout << "Closing connection " << it.second->get_id() << endl;
            
            error_code ec;
            m_endpoint.close(it.second->get_hdl(), websocketpp::close::status::going_away, "", ec);
            if (ec) {
                cout << "Error closing connection " << it.second->get_id() << ": "  
                          << ec.message() << endl;
            }
        }
        
        m_thread->join();
    }

    int connect(string const & uri) {
        error_code ec;

        client::connection_ptr con = m_endpoint.get_connection(uri, ec);
        if (ec) {
            cout << "Connect initialization error: " << ec.message() << endl;
            return -1;
        }

        int new_id = m_next_id++;
        Meta::ptr metadata_ptr = make_shared<Meta>(new_id, con->get_handle(), uri, m_enginesStatus[new_id]);
        m_connection_list[new_id] = metadata_ptr;

        con->set_open_handler(bind(
            &Meta::on_open,
            metadata_ptr,
            &m_endpoint,
            placeholders::_1
        ));
        con->set_fail_handler(bind(
            &Meta::on_fail,
            metadata_ptr,
            &m_endpoint,
            placeholders::_1
        ));
        con->set_close_handler(bind(
            &Meta::on_close,
            metadata_ptr,
            &m_endpoint,
            placeholders::_1
        ));

        m_endpoint.connect(con);

        return new_id;
    }

    Meta::ptr get_metadata(int id) const {
        con_list::const_iterator metadata_it = m_connection_list.find(id);
        if (metadata_it == m_connection_list.end()) {
            return Meta::ptr();
        } else {
            return metadata_it->second;
        }
    }

    void connectToEngines() {
        int idEngine1 = connect(ENGINE1_ADDR);
        int idEngine2 = connect(ENGINE2_ADDR);
        int idEngine3 = connect(ENGINE3_ADDR);
    }

private:
    typedef map<int,Meta::ptr> con_list;

    client m_endpoint;
    shared_ptr<thread> m_thread;

    con_list m_connection_list;
    int m_next_id;
    bool *m_enginesStatus;
};

int main() {
    return 0;
}
