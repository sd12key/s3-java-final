package gym.menu;

import java.util.Scanner;

import gym.users.UserService;
import gym.users.User;
import gym.utilities.Utils;

public class MenuService {

    public static void showMain(Scanner scanner) {
        while (true) {
            Utils.print_title_message("MAIN MENU", 50, '*', 70);
            System.out.println(Utils.symbol_line(' ', 20) + "(1) Login");
            System.out.println(Utils.symbol_line(' ', 20) + "(2) Register Member");
            System.out.println(Utils.symbol_line(' ', 20) + "(3) Register Trainer");
            System.out.println(Utils.symbol_line(' ', 20) + "(4) Register Admin");
            System.out.println(Utils.symbol_line(' ', 20) + "(0) Exit");

            System.out.print("\nEnter your choice: ");

            String username = null;
            String password = null;
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println("=== Login ===");
                    System.out.print("Enter username: ");
                    username = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    password = scanner.nextLine().trim();
                    // Call the login method from UserService
                    // Assuming UserService.login returns a User object or null if login fails
                    User logged_user = UserService.login(username, password);
                    if (logged_user != null) {
                        System.out.println("Login successful! Welcome, " + logged_user.getFullName() + "!");
                        logged_user.showUserMenu();
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;
                case "2","3","4":
                    // Register a new user (Member, Trainer, or Admin)
                    String role = null;
                    if (choice.equals("2")) {
                        role = User.ROLE_MEMBER;
                    } else if (choice.equals("3")) {
                        role = User.ROLE_TRAINER;
                    } else if (choice.equals("4")) {
                        role = User.ROLE_ADMIN;
                    }
                    System.out.println("=== Register ===");
                    System.out.print("Enter username: ");
                    username = scanner.nextLine().trim();
                    if (!UserService.isUsernameAvailable(username)) {
                        System.out.println("Username not available. Please choose another one.");
                        break;
                    }
                    System.out.print("Enter password: ");
                    password = scanner.nextLine().trim();
                    System.out.print("Repeat password: ");
                    String password_repeat = scanner.nextLine().trim();
                    if (!Utils.passwords_equal(password, password_repeat)) {
                        System.out.println("Passwords do not match. Please try again.");
                        break;
                    }
                    String password_hash = Utils.hash_password(password);
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("Enter full name: ");
                    String full_name = scanner.nextLine().trim();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine().trim();
                    System.out.print("Enter phone number: ");
                    String phone_number = scanner.nextLine().trim();

                    // Call the register method from UserService
                    if (!UserService.register(username, password_hash, email, full_name, address, phone_number, role)) {
                        System.out.println("Registration failed. Please try again.");
                    } else {
                        System.out.println("Registration successful! You can now log in.");
                    }
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
