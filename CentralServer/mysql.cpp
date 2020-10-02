#include <iostream>
#include <mysql_connection.h>
#include <mysql_driver.h>
#include <cppconn/statement.h>

using namespace sql;
using namespace sql::mysql;

int main() {
    MySQL_Driver *driver;
    Connection *con;
    Statement *stmt;

    driver = get_mysql_driver_instance();
    con = driver->connect("tcp://127.0.0.1:3306", "root", "1234");
    stmt = con->createStatement();
    stmt->execute("USE testdb");
    stmt->execute("DROP TABLE IF EXISTS testTable");
    stmt->execute("CREATE TABLE testTable(id INT, label CHAR(1))");
    stmt->execute("INSERT INTO testTable(id, label) VALUES (1, 'b')");

    delete stmt;
    delete con; // -lmysqlcppconn
}