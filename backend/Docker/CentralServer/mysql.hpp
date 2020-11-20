#ifndef MYSQL_HPP
#define MYSQL_HPP

#include <mysql_connection.h>
#include <mysql_driver.h>
#include "json.hpp"

#define DB "CentralServer."
#define SURVEYS_T "Surveys"
#define ACCOUNTS_T "Accounts"

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
    
    string surveys_t = DB SURVEYS_T;
    string accounts_t = DB ACCOUNTS_T;
};

#endif // MYSQL_HPP