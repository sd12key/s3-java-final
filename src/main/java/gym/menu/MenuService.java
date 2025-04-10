package gym.menu;

import java.sql.Connection;
import java.util.Scanner;

import gym.users.UserService;
import gym.users.User;
import gym.utilities.Utils;

public class MenuService {

    public static void showMain(Connection conn, Scanner scanner) {
        while (true) {
            Utils.print_title_message("MAIN MENU", 50, '*', 70);
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();
                    // Call the login method from UserService
                    // Assuming UserService.login returns a User object or null if login fails
                   User logged_user = UserService.login(username, password, conn);
                    if (logged_user != null) {
                        logged_user.showUserMenu();
                    }
                    break;
                case "2":
                    // UserService.register(scanner, conn);
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
