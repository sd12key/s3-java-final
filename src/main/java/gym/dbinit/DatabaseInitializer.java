package gym.dbinit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.database.DBConst;

public class DatabaseInitializer {
    
    // Constants for the CSV folder and files
    private static final Path CSV_FOLDER = Paths.get("ini_data");
    private static final String[] CSV_FILES = {
        "users.csv",
        "membership_types.csv",
        "memberships.csv",
        "workout_classes.csv"
    };

    public static Connection databaseInit(boolean drop_tables, boolean initialize_from_csv) {
        // Establish a connection to the database
        Connection conn = DatabaseConnection.getConnection();
        System.out.println("--> Connected to the database successfully!");
        System.err.println("    (" + conn.toString() + ")\n");

        if (drop_tables) { dropTables(conn); }
        // Create the tables in the database (if they don't exist)
        createTables(conn);
        // Set the schema for the connection
        setSchema(conn);

        if (initialize_from_csv) {
            // If --drop and --init are both specified, initialize from CSV files
            System.out.println("--> Initializing database from CSV files...\n");
            System.out.println("--> Imported all CSV files successfully!\n");
            } else {
            // initializing just membership_types table with default values
            
            } 
               
        return conn;
    }

    public static void setSchema(Connection conn) {
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_SCHEMA);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_SET_SCHEMA);
        DatabaseConnection.closeStatement(stmt);
    }

    public static void createTables(Connection conn) {
        conn = DatabaseConnection.ensureConnection(conn);
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_USERS_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_MEMBERSHIP_TYPES_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_MEMBERSHIPS_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_WORKOUT_CLASSES_TABLE);
        DatabaseConnection.closeStatement(stmt);
        // System.out.println("--> Created all tables successfully!\n");
    }

    public static void dropTables(Connection conn) {
        conn = DatabaseConnection.ensureConnection(conn);
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_DROP_TABLES);
        DatabaseConnection.closeStatement(stmt);
        System.out.println("--> Dropped all tables successfully!\n");
    }


        

}