#include "mysql.hpp"
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>

#define HOST "tcp://34.70.117.28:3306"
#define USER "root"
#define PASS "jerome"

using namespace sql;
using namespace sql::mysql;
using json = nlohmann::json;

MySQL::MySQL() {
    driver = get_mysql_driver_instance();
    setup();
}

void MySQL::sendSurvey(json survey) {
    connect();
    
    PreparedStatement* stmt = con->prepareStatement(
        "INSERT INTO Polls (email, firstName, lastName, age, interest) VALUES (?, ?, ?, ?, ?)"
    );

    stmt->setString(1, survey["email"].get<string>());
    stmt->setString(2, survey["firstName"].get<string>());
    stmt->setString(3, survey["lastName"].get<string>());
    stmt->setInt(4, survey["age"].get<int>());
    stmt->setBoolean(5, survey["interest"].get<bool>());
    stmt->execute();

    delete stmt;
    disconnect();
}

json MySQL::getSurvey() {
    connect();
    Statement *stmt = con->createStatement();
    ResultSet *res  = stmt->executeQuery("SELECT * FROM Polls");
    json allData;
    while (res->next()) {
        json data;
        data["email"]     = res->getString("email");
        data["firstName"] = res->getString("firstName");
        data["lastName"]  = res->getString("lastName");
        data["age"]       = res->getInt("age");
        data["interest"]  = res->getBoolean("interest");
        allData.push_back(data);
    }

    delete res;
    delete stmt;
    disconnect();
    return allData;
}

json MySQL::getUser(string user) {
    connect();
    Statement *stmt = con->createStatement();
    ResultSet *res = stmt->executeQuery("SELECT * FROM Accounts WHERE user='" + user + "'");
    json data;
    while (res->next()) {
        data["user"] = res->getString("user");
        data["salt"] = res->getString("salt");
        data["pw"]   = res->getString("pw");
    }

    delete res;
    delete stmt;
    disconnect();
    return data;
}

void MySQL::updatePass(string user, string salt, string newPass) {
    connect();
    PreparedStatement* stmt = con->prepareStatement("UPDATE Accounts SET salt=?, pw=? WHERE user=?");

    stmt->setString(1, salt);
    stmt->setString(2, newPass);
    stmt->setString(3, user);
    stmt->execute();


    delete stmt;
    disconnect();
}

void MySQL::connect() {
    con = driver->connect(HOST, USER, PASS);
    Statement* stmt = con->createStatement();
    stmt->execute("USE Server");
    delete stmt;
}

void MySQL::disconnect() {
    delete con;
}

void MySQL::setup() {
    con = driver->connect(HOST, USER, PASS);
    Statement* stmt = con->createStatement();

    stmt->execute("USE Server");
    stmt->execute("DROP TABLE IF EXISTS Polls");
    stmt->execute(
        "CREATE TABLE Polls ("
        "email VARCHAR(40), "
        "firstName VARCHAR(40), "
        "lastName VARCHAR(40), "
        "age TINYINT, "
        "interest BOOL)"
    );

    delete stmt;
    delete con;
}