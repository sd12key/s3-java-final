package gym;

import java.sql.Connection;

import gym.database.DatabaseConnection;
import gym.dbinit.DatabaseInitializer;
import gym.utilities.Utils;

public class GymApp {

    private static final int LINE_LENGTH = 70;

    public static void main(String[] args)  {

        Utils.print_title_message("WELCOME TO GYM APP", LINE_LENGTH, '*');

        Connection gymdb = DatabaseInitializer.databaseInit();
        // DatabaseInitializer.dropTables(gymdb);

        

        DatabaseConnection.closeConnection(gymdb);
        
    }

}
