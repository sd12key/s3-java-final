package gym.dbinit;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import gym.database.DBConst;
import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.memberships.MembershipType;
import gym.memberships.MembershipTypeDAO;

import gym.utilities.Utils;

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

        // Set the schema for the connection
        setSchema(conn);

        // Drop the tables if --drop is specified
        if (drop_tables) { dropTables(conn); }

        // Create the tables in the database (if they don't exist)
        createTables(conn);

        if (initialize_from_csv) {
            // If --drop and --init are both specified, initialize from CSV files
            System.out.println("--> Initializing database from CSV files...\n");
            try {
                loadMembershipTypesFromCSV(conn, CSV_FOLDER.resolve(CSV_FILES[1]));
            } catch (Exception e) {
                System.err.println("Error loading membership types from CSV: " + e.getMessage());
                System.exit(99);
            }
            System.out.println("--> Imported all CSV files successfully!\n");

            } else {
            // initializing just membership_types table with default values
            for (Object[] row : DBConst.MEMBERSHIP_TYPES_DATA) {
                String user_role = (String) row[0];
                String type = (String) row[1];
                String description = (String) row[2];
                int duration = (int) row[3];
                double cost = (double) row[4];

                MembershipType mt = new MembershipType(user_role, type, description, duration, cost);
                System.out.println("Adding membership type: " + mt.toString());
                MembershipTypeDAO.addNew(mt, conn);
            }
            System.out.println("--> Initialized membership_types table with default values!\n");
        } 

        List<MembershipType> types = MembershipTypeDAO.getAll(conn);
        System.out.println("=== All Membership Types in the Database ===");
        for (MembershipType mt : types) {
            System.out.println(mt);
        }

               
        return conn;
    }

    public static void setSchema(Connection conn) {
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_SCHEMA);
        System.out.println("--> Created schema " + DBConst.SCHEMA + " successfully!\n");
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_SET_SCHEMA);
        System.out.println("--> Set schema " + DBConst.SCHEMA + " successfully!\n");
        DatabaseConnection.closeStatement(stmt);
    }

    private static void createTables(Connection conn) {
        conn = DatabaseConnection.ensureConnection(conn);
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_USERS_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_MEMBERSHIP_TYPES_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_MEMBERSHIPS_TABLE);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_CREATE_WORKOUT_CLASSES_TABLE);
        DatabaseConnection.closeStatement(stmt);
        System.out.println("--> Created all tables successfully!\n");
    }

    private static void dropTables(Connection conn) {
        conn = DatabaseConnection.ensureConnection(conn);
        Statement stmt = DatabaseConnection.createStatement(conn);
        DatabaseConnection.executeStatement(stmt, SQLTemplates.SQL_DROP_TABLES);
        DatabaseConnection.closeStatement(stmt);
        System.out.println("--> Dropped all tables successfully!\n");
    }

    private static void loadMembershipTypesFromCSV(Connection conn, Path path) throws Exception {

        Scanner scanner = new Scanner(new FileReader(path.toFile()));
    
        // skip header
        if (scanner.hasNextLine()) {
        scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = Utils.parse_csv(line);

            System.out.println(parts.length + " parts: " + line);
            if (parts.length < 5) continue;

            String user_role = parts[0];
            String type = parts[1];
            String description = parts[2];
            int duration = Integer.parseInt(parts[3]);
            double cost = Double.parseDouble(parts[4]);

            MembershipType mt = new MembershipType(user_role, type, description, duration, cost);
            System.out.println("Adding membership type: " + mt.toString());
            MembershipTypeDAO.addNew(mt, conn);
        }

        scanner.close();
    }


        

}