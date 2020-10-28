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

    /**
     * Gets the mysql drivers and calls setup()
     */
    MySQL();

    /**
     * Sends a completed survey to the database
     * 
     * @param email     Respondent's email
     * @param firstName Respondent's first name
     * @param lastName  Respondent's last name
     * @param age       Respondent's age
     * @param interest  Respondent's interest in being contacted
     */
    void sendPoll(std::string email, std::string firstName, std::string lastName, int age, bool interest);

    /**
     * Get all the accumulated surveys from the database
     * 
     * @param err Store the connection status of the database
     * @return A json containing all of the surveys
     */
    json getPolls(bool &err);

private:

    /**
     * Connects to the database. Each request to the database needs a connection first.
     * 
     * @return The status of the connection. True if successful.
     */
    bool connect();

    /**
     * Disconnects from the database. Each request to the database needs a 
     * disconnection when completed
     */
    void disconnect();

    /**
     * Drops the Server database containing the survey if it exists and recreates it
     * from scratch. In the server database is created the Polls table containing the
     * surveys
     */
    void setup();

    MySQL_Driver *driver;
    Connection *con;
};

#endif // MYSQL_HPP