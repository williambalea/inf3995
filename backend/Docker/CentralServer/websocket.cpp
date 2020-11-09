#include "websocket.hpp"

Websocket::Websocket (bool *enginesStatus) : m_enginesStatus(enginesStatus) {
    
    m_endpoint.clear_access_channels(websocketpp::log::alevel::all);
    m_endpoint.clear_error_channels(websocketpp::log::elevel::all);

    m_endpoint.init_asio();
    m_endpoint.start_perpetual();

    m_thread = make_shared<thread>(&client::run, &m_endpoint);

    connectToEngines();
}

Websocket::~Websocket() {
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

int Websocket::connect(string const & uri, int id) {
    error_code ec;

    client::connection_ptr con = m_endpoint.get_connection(uri, ec);
    if (ec) {
        cout << "Connect initialization error: " << ec.message() << endl;
        return -1;
    }
    // TODO: what to do if the user chose an id that is already used

    Meta::ptr metadata_ptr = make_shared<Meta>(id, con->get_handle(), uri, m_enginesStatus[id]);
    m_connection_list[id] = metadata_ptr;

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

    return id;
}

Meta::ptr Websocket::get_metadata(int id) const {
    con_list::const_iterator metadata_it = m_connection_list.find(id);
    if (metadata_it == m_connection_list.end()) {
        return Meta::ptr();
    } else {
        return metadata_it->second;
    }
}

void Websocket::connectToEngines() {
    connect(ENGINE1_ADDR, 0);
    connect(ENGINE2_ADDR, 1);
    connect(ENGINE3_ADDR, 2);
}
