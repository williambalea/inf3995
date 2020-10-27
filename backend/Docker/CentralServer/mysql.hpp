#ifndef MYSQL_HPP
#define MYSQL_HPP

#include <mysql_connection.h>
#include <mysql_driver.h>
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>
#include "json.hpp"

#define HOST "tcp://34.70.117.28:3306"
#define USER "root"
#define PASS "jerome"

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


MySQL::MySQL() {
    driver = get_mysql_driver_instance();
    setup();
}

void MySQL::sendPoll(std::string email, std::string firstName, std::string lastName, int age, bool interest) {
    connect();
    
    PreparedStatement* stmt = con->prepareStatement(
        "INSERT INTO Polls (email, firstName, lastName, age, interest) VALUES (?, ?, ?, ?, ?)"
    );

    stmt->setString(1, email);
    stmt->setString(2, firstName);
    stmt->setString(3, lastName);
    stmt->setInt(4, age);
    stmt->setBoolean(5, interest);
    stmt->execute();

    delete stmt;
    disconnect();
}

json MySQL::getPolls(bool &err) {
    err = !connect();
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

bool MySQL::connect() {
    con = driver->connect(HOST, USER, PASS);
    Statement* stmt = con->createStatement();
    stmt->execute("USE Server");
    delete stmt;
    return con->isValid();
}

void MySQL::disconnect() {
    delete con;
}

void MySQL::setup() {
    con = driver->connect(HOST, USER, PASS);
    Statement* stmt = con->createStatement();

    stmt->execute("DROP DATABASE IF EXISTS Server");
    stmt->execute("CREATE DATABASE Server");
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

#endif // MYSQL_HPP