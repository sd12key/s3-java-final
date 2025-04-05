package gym.dbinit;

import java.nio.file.Path;
import java.nio.file.Paths;

import gym.database.DatabaseConnection;

public class DatabaseInitializer {
    private static final String INI_FILE = "gymapp.ini";
    private static final String PIN_FILE = "gymapp.pin";

    private static final Path CSV_FOLDER = Paths.get("ini_data");
    private static final String[] CSV_FILES = {
        "users.csv",
        "membership_types.csv",
        "memberships.csv",
        "workout_classes.csv"
    };

}