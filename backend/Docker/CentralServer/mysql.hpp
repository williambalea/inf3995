#ifndef MYSQL_HPP
#define MYSQL_HPP

#include <mysql_connection.h>
#include <mysql_driver.h>
#include "json.hpp"

using namespace std;
using namespace sql;
using namespace sql::mysql;
using json = nlohmann::json;

class MySQL {
public:
    MySQL();
    void sendSurvey(json survey);
    json getSurvey();
    json getUser(string user);
    void updatePass(string user, string salt, string newPass);

private:

    MySQL_Driver *driver;
    Connection *con;
};

#endif // MYSQL_HPP