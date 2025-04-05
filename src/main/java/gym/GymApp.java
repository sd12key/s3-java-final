package gym;

import java.sql.Connection;
import java.sql.SQLException;

import gym.database.DatabaseConnection;
import gym.utilities.Utils;

public class GymApp {

    private static final int LINE_LENGTH = 70;

    public static void main(String[] args)  {

        Utils.print_title_message("WELCOME TO GYM APP", LINE_LENGTH, '*');

        // Initialize database connection variable
        Connection conn = null;

        // Establish a connection to the database
        conn = DatabaseConnection.getConnection();
        System.out.println("--> Connected to the database successfully!");
        System.err.println("    (" + conn.toString() + ")\n");

        conn = DatabaseConnection.ensureConnection(conn);
        System.out.println("--> Re-connected to the database successfully!");
        System.err.println("    (" + conn.toString() + ")\n");

        DatabaseConnection.closeConnection(conn);
        
    }

}
