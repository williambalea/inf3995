class EnginSQL:

    DB_HOSTNAME = "34.70.117.28"
    DB_USERNAME = "root"
    DB_PASSWORD = "jerome"
    DB_NAME = "Bixi"

    def __init__(self):
        create_connection(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_NAME)

    #create connection to DB
    def create_connection(host_name, user_name, user_password):
        connection = None
        try:
            connection = mysql.connector.connect(
                host=host_name,
                user=user_name,
                passwd=user_password,
                database=MY_DATABASE
            )
            print("Connection to MySQL DB successful")
        except Error as e:
            print(f"The error '{e}' occurred")
        return connection
