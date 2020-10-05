#ifndef MYSQL_HPP
#define MYSQL_HPP

#include <iostream>
#include <string>
#include <mysql_connection.h>
#include <mysql_driver.h>
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>

#define HOST "tcp://34.70.117.28:3306"
#define USER "root"
#define PASS "jerome"

using namespace sql;
using namespace sql::mysql;

class MySQL {
public:
    MySQL() {
        driver = get_mysql_driver_instance();
        setupServerDB();
    }

    void sendPoll(std::string courriel, std::string prenom, std::string nom, int age, bool interet) {
        connectToServerDB();
        
        PreparedStatement* stmt = con->prepareStatement(
            "INSERT INTO Polls (courriel, prenom, nom, age, interet) VALUES (?, ?, ?, ?, ?)");

        stmt->setString(1, courriel);
        stmt->setString(2, prenom);
        stmt->setString(3, nom);
        stmt->setInt(4, age);
        stmt->setBoolean(5, interet);
        stmt->execute();

        delete stmt;
        disconnectFromServerDB();
    }

private:
    void connectToServerDB() {
        con = driver->connect(HOST, USER, PASS);
        Statement* stmt = con->createStatement();
        stmt->execute("USE Server");
        delete stmt;
    }

    void disconnectFromServerDB() {
        delete con;
    }

    void setupServerDB() {
        con = driver->connect(HOST, USER, PASS);
        Statement* stmt = con->createStatement();

        stmt->execute("DROP DATABASE IF EXISTS Server");
        stmt->execute("CREATE DATABASE Server");
        stmt->execute("USE Server");
        stmt->execute("DROP TABLE IF EXISTS Polls");
        // TODO : ask android app how they manage string verification
        stmt->execute(
            "CREATE TABLE Polls ("
            "courriel VARCHAR(40), "
            "prenom VARCHAR(40), "
            "nom VARCHAR(40), "
            "age TINYINT, "
            "interet BOOL)"
        );

        delete stmt;
        delete con;
    }

    MySQL_Driver *driver;
    Connection *con;
};

#endif