package gym.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import gym.database.SQLTemplates;
import gym.database.DBConst;


public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";
    
    // Establishes a connection to the PostgreSQL database using JDBC.
    // It takes a boolean parameter to determine whether to exit the program on error.
    // If exit_on_error is true, the program will terminate if a connection cannot be established.
    // If exit_on_error is false, will throw an exception instead.
    public static Connection getConnection(Boolean exit_on_error) {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            // Wrap and rethrow as runtime exception if exit_on_error=false
            throw new RuntimeException(e);
        }
    }

    // Overloaded method to get a connection without the exit_on_error parameter.
    // This method will call the getConnection method with exit_on_error=true
    public static Connection getConnection() {
        return getConnection(true);
    }

    // This method ensures that the connection is valid and not closed.
    // If the connection is closed (or invalid), it will create a new one.
    // I will not be reconnecting to the database on every query, but rather
    // checking if the connection is still valid and re-establishing it if necessary.
    public static Connection ensureConnection(Connection conn, boolean exit_on_error) {
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                conn = getConnection(exit_on_error);
                Statement stmt = createStatement(conn, exit_on_error);
                executeStatement(stmt, SQLTemplates.SQL_SET_SCHEMA, exit_on_error);
                closeStatement(stmt, exit_on_error);
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Database communication error: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded method to ensure a connection without the exit_on_error parameter.
    // This method will call the ensureConnection method with exit_on_error=true.
    // will throw an exception if the connection cannot be re-established.
    public static Connection ensureConnection(Connection conn) {
        return ensureConnection(conn, true);
    }
    
    // Closes the connection safely, handling any SQL exceptions that may occur.
    // It takes a Connection object and a boolean parameter to determine whether to exit the program on error.
    public static void closeConnection(Connection conn, boolean exit_on_error) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                if (exit_on_error) {
                    System.exit(99);
                }
                throw new RuntimeException(e);
            }
        }
    }

    // Overloaded method to close a connection without the exit_on_error parameter.
    // This method will call the closeSafe method with exit_on_error=true.
    public static void closeConnection(Connection conn) {
        closeConnection(conn, true);
    }

    // Creates a statement object for executing SQL queries.
    // It takes a Connection object and a boolean parameter to determine whether to exit the program on error.
    // or throw an exception on error if exit_on_error is false.
    public static Statement createStatement(Connection conn, boolean exit_on_error) {
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Error creating statement: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded method to create a statement without the exit_on_error parameter.
    public static Statement createStatement(Connection conn) {
        return createStatement(conn, true);
    }

    // Creates a PreparedStatement for parameterized SQL queries.
    // If exit_on_error is true, exits on failure. Otherwise, throws RuntimeException.
    public static PreparedStatement prepareStatement(Connection conn, String sql, boolean exit_on_error) {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("Error preparing statement: " + sql);
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded version with exit_on_error = true by default.
    public static PreparedStatement prepareStatement(Connection conn, String sql) {
        return prepareStatement(conn, sql, true);
    }

    // Creates a PreparedStatement for parameterized SQL queries that also returns generated keys.
    public static PreparedStatement prepareStatementWithKeys(Connection conn, String sql, boolean exit_on_error) {
        try {
            return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            System.err.println("Error preparing statement (with keys): " + sql);
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded version with exit_on_error = true by default.
    public static PreparedStatement prepareStatementWithKeys(Connection conn, String sql) {
        return prepareStatementWithKeys(conn, sql, true);
    }

    // Executes a SQL command using the provided Statement object.
    public static void executeStatement(Statement stmt, String sql, boolean exit_on_error) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL execution error for: " + sql);
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded method to execute a SQL command without the exit_on_error parameter.
    public static void executeStatement(Statement stmt, String sql) {
        executeStatement(stmt, sql, true);
    }


    // Executes a SQL Query using the provided PreparedStatement object.
    public static ResultSet executeQuery(PreparedStatement stmt, boolean exit_on_error) {
        try {
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Statement execution error!");
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded method with exit_on_error=true
    public static ResultSet executeQuery(PreparedStatement stmt) {
        return executeQuery(stmt, true);
    }

    // overloaded method to execute a SQL Query using simple Statement object.
    public static ResultSet executeQuery(Statement stmt, String sql, boolean exit_on_error) {
        try {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Statement execution error: " + sql);
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }

    // Overloaded method with exit_on_error=true
    public static ResultSet executeQuery(Statement stmt, String sql) {
        return executeQuery(stmt, sql, true);
    }

    // Executes an update SQL command using the provided Statement object.
    public static int executeUpdate(Statement stmt, String sql, boolean exit_on_error) {
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Update execution error: " + sql);
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }
    
    // Overloaded method to execute an update SQL command without the exit_on_error parameter.
    public static int executeUpdate(Statement stmt, String sql) {
        return executeUpdate(stmt, sql, true);
    }

    // Executes an update SQL command using the provided PreparedStatement object.
    public static int executeUpdate(PreparedStatement ps, boolean exit_on_error) {
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("PreparedStatement update error!");
            System.err.println("*** Details: " + e.getMessage());
            if (exit_on_error) {
                System.exit(99);
            }
            throw new RuntimeException(e);
        }
    }
    
    // Overloaded method to execute an update SQL command without the exit_on_error parameter.
    public static int executeUpdate(PreparedStatement ps) {
        return executeUpdate(ps, true);
    }

    // Retrieves the auto-generated key from the last executed statement.
    // It assumes that the statement was created with the option to return generated keys.
    public static int getGeneratedKey(PreparedStatement ps) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                // Assuming the key is in the first column
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving generated key: " + e.getMessage());
        }
        return -1;
    }
    
    // Closes the Statement object safely, handling any SQL exceptions that may occur.
    // it should also work with PreparedStatement objects, since they are subclasses of Statement.
    public static void closeStatement(Statement stmt, boolean exit_on_error) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
                if (exit_on_error) {
                    System.exit(99);
                }
                throw new RuntimeException(e);
            }
        }
    }
    
    // Overloaded method to close a statement without the exit_on_error parameter.
    public static void closeStatement(Statement stmt) {
        closeStatement(stmt, true);
    }

    // Closes the ResultSet object safely, handling any SQL exceptions that may occur.
    public static void closeResultSet(ResultSet rs, boolean exit_on_error) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
                if (exit_on_error) {
                    System.exit(99);
                }
                throw new RuntimeException(e);
            }
        }
    }
    
    // Overloaded version
    public static void closeResultSet(ResultSet rs) {
        closeResultSet(rs, true);
    }

    // Methods to close the conn, stmt, and rs objects quietly, ignoring any errors.
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }

    // will also work on PreparedStatement
    public static void closeQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }
            
    

}
