package gym.dbinit;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import gym.database.DBConst;
import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.memberships.Membership;
import gym.memberships.MembershipDAO;
import gym.memberships.MembershipType;
import gym.memberships.MembershipTypeDAO;
import gym.users.User;
import gym.users.UserDAO;
import gym.users.childclasses.Trainer;
import gym.workoutclasses.WorkoutClass;
import gym.workoutclasses.WorkoutClassDAO;
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
                loadUsersFromCSV(conn, CSV_FOLDER.resolve(CSV_FILES[0]));
                loadMembershipTypesFromCSV(conn, CSV_FOLDER.resolve(CSV_FILES[1]));
                loadMembershipsFromCSV(conn, CSV_FOLDER.resolve(CSV_FILES[2]));
                loadWorkoutClassesFromCSV(conn, CSV_FOLDER.resolve(CSV_FILES[3]));
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
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(path.toFile()));
            // skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = Utils.parse_csv(line);
    
                // System.out.println(parts.length + " parts: " + line);
                // Check if the line has the expected number of parts, skip if not
                if (parts.length < 5) continue;

                String user_role = parts[0];
                String type = parts[1];
                String description = parts[2];
                int duration = Integer.parseInt(parts[3]);
                double cost = Double.parseDouble(parts[4]);
    
                MembershipType mt = new MembershipType(user_role, type, description, duration, cost);
                // System.out.println("Adding membership type: " + mt.toString());
                int added_md_id = MembershipTypeDAO.addNewReturnId(mt, conn);
                if (added_md_id == -1) {
                    System.err.println("Failed to add membership type: " + mt.toString());
                    System.exit(99);
                } else {
                    // Print the added membership type
                    mt.setId(added_md_id);
                    System.out.println("Added membership type: " + mt.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading membership types from CSV: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void loadUsersFromCSV(Connection conn, Path path) throws Exception {
    Scanner scanner = null;
    try {
        scanner = new Scanner(new FileReader(path.toFile()));
        // skip header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = Utils.parse_csv(line);

            // System.out.println(parts.length + " parts: " + line);
            if (parts.length < 7) continue;

            String username = parts[0];

            // plain text password
            String plain_password = parts[1];
            // Hash the password using BCrypt
            String password_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());


            String email = parts[2];
            String full_name = parts[3];
            String address = parts[4];
            String phone_number = parts[5];
            String role = parts[6];
            
            // user cannot be instantiated, so use the static helper method to create it
            User user = User.create(username, password_hash, email, full_name, address, phone_number, role);
            // System.out.println("Adding user: " + user.toString());
            int added_user_id = UserDAO.addNewReturnId(user, conn);
            if (added_user_id == -1) {
                System.err.println("Failed to add user: " + user.toString());
                System.exit(99);
            } else {
                // Print the added user
                System.out.println("Added user: [" + added_user_id + "] " + user.toStringNoId());
            }
        }
        } catch (Exception e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void loadMembershipsFromCSV(Connection conn, Path path) throws Exception {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(path.toFile()));
            // skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = Utils.parse_csv(line);

                System.out.println(parts.length + " parts: " + line);
                if (parts.length < 3) continue;

                // Parse CSV fields
                int type_id = Integer.parseInt(parts[0]);
                int user_id = Integer.parseInt(parts[1]);
                LocalDate purchase_date = LocalDate.parse(parts[2]);

                // create membershiptype and user objects
                MembershipType type = MembershipTypeDAO.getById(type_id, conn);
                User user = UserDAO.getById(user_id, conn);
                
                if (type == null || user == null) {
                    System.err.println("Error: Could not find referenced type or user");
                    System.exit(99);
                } else if (!user.getRole().equalsIgnoreCase(type.getUserRole())) {
                    System.err.println("Error: User role does not match membership type role");
                    System.exit(99);
                }

                // create membership object and add to database
                Membership membership = new Membership(type, user, purchase_date);
                System.out.println("Adding membership: " + membership.toString());
                if (!MembershipDAO.addNew(membership, conn)) {
                    System.err.println("Failed to add membership: " + membership.toString());
                    System.exit(99);
                } else {
                    System.out.println("Added membership: " + membership.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading memberships from CSV: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }    

    private static void loadWorkoutClassesFromCSV(Connection conn, Path path) throws Exception {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(path.toFile()));
            // skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = Utils.parse_csv(line);

                System.out.println(parts.length + " parts: " + line);
                if (parts.length < 3) continue;

                // Parse CSV fields
                String type = parts[0];
                String description = parts[1];
                int trainerId = Integer.parseInt(parts[2]);

                // Fetch trainer from database
                Trainer trainer = (Trainer) UserDAO.getById(trainerId, conn);
                
                if (trainer == null) {
                    System.err.println("Error: Could not find trainer with ID " + trainerId);
                    System.exit(99);
                } else if (!trainer.getRole().equalsIgnoreCase(User.ROLE_TRAINER)) {
                    System.err.println("Error: User with ID " + trainerId + " is not a trainer");
                    System.exit(99);
                }

                // create workout class object and add to database
                WorkoutClass workoutClass = new WorkoutClass(type, description, trainer);
                System.out.println("Adding workout class: " + workoutClass.toString());
                if(!WorkoutClassDAO.addNew(workoutClass, conn)) {
                    System.err.println("Failed to add workout class: " + workoutClass.toString());
                    System.exit(99);
                } else {
                    System.out.println("Added workout class: " + workoutClass.toString());
                }

            }
        } catch (Exception e) {
            System.err.println("Error loading workout classes from CSV: " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }    
        

}