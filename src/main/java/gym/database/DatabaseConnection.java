package gym.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;

public final class DatabaseConnection {

    // JDBC connection object
    // I want to have only one instance of the connection object.
    private static Connection conn = null;
    // This boolean will be used to check if the schema has been created or not.
    // It will be set to true after the schema is created.
    // This will prevent the schema from being created multiple times.
    // not really nesseccasy, because SQL will ignore the create statement if the schema already exists.
    private static boolean schema_created = false;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    // Helper method for error handling
    private static void handleError(String error_msg, SQLException e, boolean exit_on_error) {
        System.err.println(error_msg + ": " + e.getMessage());
        if (exit_on_error) {
            System.exit(99);
        }
        throw new RuntimeException(error_msg, e);
    }
    
    private static void createSchema(boolean exit_on_error) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(SQLTemplates.SQL_CREATE_SCHEMA);
            stmt.close();
        } catch (SQLException e) {
            handleError("Error creating schema", e, exit_on_error);
        }
    }

    private static void setSchema(boolean exit_on_error) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(SQLTemplates.SQL_SET_SCHEMA);
            stmt.close();
        } catch (SQLException e) {
            handleError("Error setting schema", e, exit_on_error);
        }
    }

    // Establishes a connection to the PostgreSQL database using JDBC.
    // It takes a boolean parameter to determine whether to exit the program on error.
    // If exit_on_error is true, the program will terminate if a connection cannot be established.
    // If exit_on_error is false, will throw an exception instead.
    public static Connection getConnection(boolean exit_on_error) {
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                closeConnection(exit_on_error);
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                if (!schema_created) {
                    createSchema(exit_on_error);
                    schema_created = true;
                }
                setSchema(exit_on_error); 
            }
            return conn;
        } catch (SQLException e) {
            handleError("Error connecting to database", e, exit_on_error);
            return null;        }
    }

    // Overloaded method to get a connection without the exit_on_error parameter.
    // This method will call the getConnection method with exit_on_error=true
    public static Connection getConnection() {
        return getConnection(true);
    }

    // Checks if the connection is invalid (null, closed, or not valid).
    public static boolean isConnectionInvalid() {
        try {
            return conn == null || conn.isClosed() || !conn.isValid(2);
        } catch (SQLException e) {} {
            // if exception then the connection is indeed invalid
            return true; 
        }
    }
    
    // Closes the connection safely, handling any SQL exceptions that may occur.
    // It takes a Connection object and a boolean parameter to determine whether to exit the program on error.
    public static void closeConnection(boolean exit_on_error) {
        // No need to close if the connection is already invalid
        if (isConnectionInvalid()) {
            return; // No need to close if the connection is already invalid
        }
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            handleError("Error closing database connection", e, exit_on_error);
        }
    }

    // Overloaded method to close a connection without the exit_on_error parameter.
    // This method will call the closeSafe method with exit_on_error=true.
    public static void closeConnection() {
        closeConnection(true);
    }

    // Creates a statement object for executing SQL queries.
    public static Statement createStatement(boolean exit_on_error) {
        try {
            return getConnection(exit_on_error).createStatement();
        } catch (SQLException e) {
            handleError("Error creating statement", e, exit_on_error);
            return null;        
        }
    }

    // Overloaded method to create a statement without the exit_on_error parameter.
    public static Statement createStatement() {
        return createStatement(true);
    }

    // Advances the ResultSet cursor to the next row.
    public static boolean rsNext(ResultSet rs, boolean exit_on_error) {
        try {
            return rs.next();
        } catch (SQLException e) {
            handleError("Error advancing ResultSet cursor", e, exit_on_error);
            return false; 
        }
    }
    
    // Overloaded method to advance the ResultSet cursor without the exit_on_error parameter.
    public static boolean rsNext(ResultSet rs) {
        return rsNext(rs, true);
    }
  
    // Checks if a table is empty by executing a simple SELECT query with LIMIT 1.
    public static boolean isTableEmpty(String table_name, boolean exit_on_error) {
        String query = "SELECT 1 FROM " + table_name + " LIMIT 1";
        getConnection(exit_on_error);
        Statement stmt = createStatement(exit_on_error);
        ResultSet rs = executeQuery(stmt, query, exit_on_error);
        boolean is_empty = !rsNext(rs, exit_on_error);
        closeResultSet(rs, exit_on_error);
        closeStatement(stmt, exit_on_error);
        return is_empty;
    }

    // Overloaded method to check if a table is empty without the exit_on_error parameter.
    // This method will call the isTableEmpty method with exit_on_error=true.   
    public static boolean isTableEmpty(String table_name) {
        return isTableEmpty(table_name, true);
    }

    // Retrieves a string value from the ResultSet for a given column name.
    public static int rsGetInt(ResultSet rs, String column, boolean exit_on_error) {
        try {
            return rs.getInt(column);
        } catch (SQLException e) {
            handleError("Error retrieving int from column [" + column + "]", e, exit_on_error);
            return 0;
        }
    }
    
    // Overloaded method to retrieve a string value from the ResultSet without the exit_on_error parameter.
    public static int rsGetInt(ResultSet rs, String column) {
        return rsGetInt(rs, column, true);
    }

    // Retrieves a string value from the ResultSet for a given column name.
    public static String rsGetString(ResultSet rs, String column, boolean exit_on_error) {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            handleError("Error retrieving string from column [" + column + "]", e, exit_on_error);
            return null;
        }
    }
    
    // Overloaded method to retrieve a string value from the ResultSet without the exit_on_error parameter.
    public static String rsGetString(ResultSet rs, String column) {
        return rsGetString(rs, column, true);
    }

    // Retrieves a BigDecimal value from the ResultSet for a given column name.
    public static double rsGetDouble(ResultSet rs, String column, boolean exit_on_error) {
        try {
            return rs.getDouble(column);
        } catch (SQLException e) {
            handleError("Error retrieving decimal from column [" + column + "]", e, exit_on_error);
            return 0.0;
        }
    }

    // Overloaded method to retrieve a BigDecimal value from the ResultSet without the exit_on_error parameter.    
    public static double rsGetDouble(ResultSet rs, String column) {
        return rsGetDouble(rs, column, true);
    }

    // Retrieves a Date value from the ResultSet for a given column name.
    public static Date rsGetDate(ResultSet rs, String column, boolean exit_on_error) {
        try {
            return rs.getDate(column);
        } catch (SQLException e) {
            handleError("Error retrieving date from column [" + column + "]", e, exit_on_error);
            return null;
        }
    }

    // Overloaded method to retrieve a Date value from the ResultSet without the exit_on_error parameter.
    public static Date rsGetDate(ResultSet rs, String column) {
        return rsGetDate(rs, column, true);
    }

    // Sets an int value in the PreparedStatement at the specified index.
    public static void psSetInt(PreparedStatement ps, int index, int value, boolean exit_on_error) {
        try {
            ps.setInt(index, value);
        } catch (SQLException e) {
            handleError("Error setting int at index [" + index + "]", e, exit_on_error);
        }
    }
    
    // Overloaded method to set an int value in the PreparedStatement without the exit_on_error parameter.
    public static void psSetInt(PreparedStatement ps, int index, int value) {
        psSetInt(ps, index, value, true);
    }
    
    // Sets a double value in the PreparedStatement at the specified index.
    public static void psSetDouble(PreparedStatement ps, int index, double value, boolean exit_on_error) {
        try {
            ps.setDouble(index, value);
        } catch (SQLException e) {
            handleError("Error setting double at index [" + index + "]", e, exit_on_error);
        }
    }

    // Overloaded method to set a double value in the PreparedStatement without the exit_on_error parameter.
    public static void psSetDouble(PreparedStatement ps, int index, double value) {
        psSetDouble(ps, index, value, true);
    }

    // Sets a String value in the PreparedStatement at the specified index.
    public static void psSetString(PreparedStatement ps, int index, String value, boolean exit_on_error) {
        try {
            ps.setString(index, value);
        } catch (SQLException e) {
            handleError("Error setting String at index [" + index + "]", e, exit_on_error);
        }
    }
    
    // Overloaded method to set a String value in the PreparedStatement without the exit_on_error parameter.
    public static void psSetString(PreparedStatement ps, int index, String value) {
        psSetString(ps, index, value, true);
    }
    
    // Sets a Date value in the PreparedStatement at the specified index.
    public static void psSetDate(PreparedStatement ps, int index, java.sql.Date value, boolean exit_on_error) {
        try {
            ps.setDate(index, value);
        } catch (SQLException e) {
            handleError("Error setting Date at index [" + index + "]", e, exit_on_error);
        }
    }
    
    // Overloaded method to set a Date value in the PreparedStatement without the exit_on_error parameter.    
    public static void psSetDate(PreparedStatement ps, int index, java.sql.Date value) {
        psSetDate(ps, index, value, true);
    }

    // Creates a PreparedStatement for parameterized SQL queries.
    // If exit_on_error is true, exits on failure. Otherwise, throws RuntimeException.
    public static PreparedStatement prepareStatement(String sql, boolean exit_on_error) {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            handleError("Error preparing statement: " + sql, e, exit_on_error);
            return null; 
        }
    }

    // Overloaded version with exit_on_error = true by default.
    public static PreparedStatement prepareStatement(String sql) {
        return prepareStatement(sql, true);
    }

    // Creates a PreparedStatement for parameterized SQL queries that also returns generated keys.
    public static PreparedStatement prepareStatementWithKeys(String sql, boolean exit_on_error) {
        try {
            return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            handleError("Error preparing statement (with keys): " + sql, e, exit_on_error);
            return null;
        }
    }

    // Overloaded version with exit_on_error = true by default.
    public static PreparedStatement prepareStatementWithKeys(String sql) {
        return prepareStatementWithKeys(sql, true);
    }

    // Executes a SQL command using the provided Statement object.
    public static void executeStatement(Statement stmt, String sql, boolean exit_on_error) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            handleError("SQL execution error for: " + sql, e, exit_on_error);
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
            handleError("Statement execution error", e, exit_on_error);
            return null; 
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
            handleError("Query execution error: " + sql, e, exit_on_error);
            return null;
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
            handleError("Update execution error: " + sql, e, exit_on_error);
            return -1;
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
            handleError("PreparedStatement update error", e, exit_on_error);
            return -1;
        }
    }
    
    // Overloaded method to execute an update SQL command without the exit_on_error parameter.
    public static int executeUpdate(PreparedStatement ps) {
        return executeUpdate(ps, true);
    }

    // Retrieves the auto-generated key from the last executed statement.
    // It assumes that the statement was created with the option to return generated keys.
    public static int getGeneratedKey(PreparedStatement ps, boolean exit_on_error) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                // Assuming the key is in the first column
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleError("Error retrieving generated key", e, exit_on_error);

        }
        return -1;
    }

    // Overloaded method to retrieve the auto-generated key without the exit_on_error parameter.
    public static int getGeneratedKey(PreparedStatement ps) {
        return getGeneratedKey(ps, true);
    }
    
    // Closes the Statement object safely, handling any SQL exceptions that may occur.
    // it should also work with PreparedStatement objects, since they are subclasses of Statement.
    public static void closeStatement(Statement stmt, boolean exit_on_error) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                handleError("Error closing statement", e, exit_on_error);
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
                handleError("Error closing ResultSet", e, exit_on_error);
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
