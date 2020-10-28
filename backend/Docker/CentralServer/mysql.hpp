#ifndef MYSQL_HPP
#define MYSQL_HPP

#include <mysql_connection.h>
#include <mysql_driver.h>
#include "json.hpp"

using namespace sql;
using namespace sql::mysql;
using json = nlohmann::json;

class MySQL {
public:
    MySQL();
    void sendPoll(std::string email, std::string firstName, std::string lastName, int age, bool interest);
    json getPolls(bool &err);

private:
    bool connect();
    void disconnect();
    void setup();

    MySQL_Driver *driver;
    Connection *con;
};

#endif // MYSQL_HPP