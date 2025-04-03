package gym.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static final String SQL_ERROR_MESSAGE_CONN = "Error establishing a connection to the database.";

    public static final String SQL_ERROR_MESSAGE_CLOSE = "Error closing the database connection.";


    // This method establishes a connection to the PostgreSQL database using JDBC.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // This method ensures that the connection is valid and not closed.
    // If the connection is closed (or invalid), it will create a new one.
    // I will not be recconnecting to the database on every query, but rather
    // checking if the connection is still valid and re-establishing it if necessary.
    public static Connection ensureConnection(Connection conn) throws SQLException {
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            conn = getConnection();
        }
        return conn;
    }
}
