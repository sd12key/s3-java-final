package gym;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gym.database.DatabaseConnection;
import gym.dbinit.DatabaseInitializer;
import gym.menu.MenuService;

public class GymApp {

    private static final String APP_USAGE = 
        "\nGymApp [argument1] [argument2]\n" +
        "       Valid arguments:\n" +
        "       --drop           Drop all tables\n" +
        "       --drop --init    Drop tables and initialize from CSV\n" +
        "                        (--init can only be used with --drop)\n";
    private static final String DROP_ARG="--drop";
    private static final String INIT_ARG="--init";

    public static void main(String[] args)  {
        System.out.println("*** Welcome to the Gym Management System ***\n");

        // Check if the argument is present in the command line arguments and process them
        DatabaseInitializer.databaseInit(processArg(args, DROP_ARG), processArg(args, INIT_ARG));

        Scanner scanner = new Scanner(System.in);
        MenuService.init(scanner);
        MenuService.showMain();
        scanner.close();
        DatabaseConnection.closeConnection();
    }

    private static boolean processArg(String[] args, String arg_name) {
        boolean target_found = false;
        boolean has_drop = false;
        boolean has_init = false;
        List<String> seen_args = new ArrayList<String>();
    
        // Check if the argument is present in the command line arguments       
        for (String arg : args) {
            if (arg == null) {
                continue;
            }

            if (seen_args.contains(arg)) {
                System.err.println("\n--> Error: duplicate argument [" + arg + "] detected! \n"+ APP_USAGE);
                System.exit(99);
            }
            seen_args.add(arg);

            if (arg.equals(arg_name)) {
                target_found = true;
            }
            if (arg.equals(DROP_ARG)) {
                has_drop = true;
            } else if (arg.equals(INIT_ARG)) {
                has_init = true;
            } else {
                System.err.println("\n--> Error: Invalid argument [" + arg + "] detected! \n" + APP_USAGE);
                System.exit(99);
            }
        }
    
        // Validate --init requires --drop
        if (has_init && !has_drop) {
            System.err.println(APP_USAGE);
            System.exit(99);
        }
    
        return target_found;
    }
}
