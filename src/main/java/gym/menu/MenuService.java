package gym.menu;

import java.util.List;
import java.util.Scanner;

import gym.users.UserService;
import gym.users.User;

import gym.utilities.Utils;

public final class MenuService {

    private static Scanner scanner = null;

    private MenuService() {
        // private vonstructor for MenuService (no instantiation)
    }

    public static void init(Scanner sc) {
        scanner = sc;
    }
       
    public static void printMenu(String menu_role, String menu_title1, String menu_title2, String[] menu_items) {
        Utils.print_title_message(menu_title1, menu_title2, MenuConst.WIDTH_MENU_TITLE, '*', MenuConst.OFFSET_MENU_TITLE, menu_role);
        for (int i = 0; i < menu_items.length; i++) {
            System.out.println(Utils.add_offset_to_string(menu_items[i], MenuConst.OFFSET_MENU_ITEMS));
        }
    }

    public static void printMenu(String menu_role, String menu_title, String[] menu_items) {
        printMenu(menu_role, menu_title, menu_title, menu_items);
    }

    private static void print_info_message(String message, char symbol) {
        System.out.println("\n" + Utils.add_offset_to_string(Utils.print_role(" " + message + " ",symbol,MenuConst.WIDTH_MENU_TITLE), MenuConst.OFFSET_MENU_TITLE) + "\n");
    }

    private static void print_invalid_choice() {
        print_info_message("Invalid choice, try again", '=');
    }

    private static void print_logged_out() {
        print_info_message("LOGGED OUT!", '*');
    }

    private static void print_goodbye() {
        print_info_message("GOODBYE!", '*');
    }

    public static void showMain() {
        while (true) {
            printMenu(MenuConst.MAIN_MENU, MenuConst.APP_WELCOME_MSG, MenuConst.MAIN_MENU_ITEMS);
            System.out.print("\n"+Utils.add_offset_to_string("Enter your choice: ", MenuConst.OFFSET_MENU_ITEMS));
            String choice = scanner.nextLine().trim();

            String username = null;
            String password = null;
            User logged_user = null;
            
            switch (choice) {
                case "1":
                    System.out.println("=== Login ===");
                    System.out.print("Enter username: ");
                    username = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    password = scanner.nextLine().trim();
                    // Call the login method from UserService
                    // Assuming UserService.login returns a User object or null if login fails
                    logged_user = UserService.login(username, password);
                    if (logged_user != null) {
                        System.out.println("Login successful! Welcome, " + logged_user.getFullName() + "!");
                        
                        // *** ROLE-BASED ACCESS ***
                        // loged_user.showUserMenu();
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
                    print_goodbye();
                    return;
                default:
                    print_invalid_choice();
            }
        }
    }

    // Method to show custome menu for each user type
    // *** FULLY ROLE-BASED ACCESS ***
    public static void showUser(User user) {
        while (true) {
            printMenu(
                " " + user.getRole().toUpperCase() + " ",
                user.getFullName(), 
                user.getUsername() + "(" + user.getEmail() + ")", 
                user.getMenuItems()
            );
            
            System.out.print("\n"+Utils.add_offset_to_string("Enter your choice: ", MenuConst.OFFSET_MENU_ITEMS));

            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                print_logged_out();
                return;
            }
            user.handleMenuChoice(choice);
        }
    }

    // implementations of menu handlers for Admin
    public static void handleAdminMenu(User user, String choice) {
        switch (choice) {
            case "1":
                // view account info
                print_info_message(user.getMenuItems()[0], '>');
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":

                System.out.println("=== Choice 2 ===");
                // code                    
                break;
            case "3":
                System.out.println("=== Choice 3 ===");
                // code                    
                break;
            case "4":
                System.out.println("=== Choice 4 ===");
                // code                    
                break;
            case "5":
                System.out.println("=== Choice 5 ===");                
                // code                    
                break;
            default:
                print_invalid_choice();
        }
    }

    // implementations of menu handlers for Member
    public static void handleMemberMenu(User user, String choice) {
        switch (choice) {
            case "1":
                // view account info
                print_info_message(user.getMenuItems()[0], '>');
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":
                System.out.println("=== Choice 2 ===");
                // code                    
                break;
            case "3":
                System.out.println("=== Choice 3 ===");
                // code                    
                break;
            case "4":
                System.out.println("=== Choice 4 ===");
                // code                    
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // implementations of menu handlers for Trainer
    public static void handleTrainerMenu(User user, String choice) {
        switch (choice) {
            case "1":
                // view account info
                print_info_message(user.getMenuItems()[0], '>');
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":
                System.out.println("=== Choice 2 ===");
                // code                    
                break;
            case "3":
                System.out.println("=== Choice 3 ===");
                // code                    
                break;
            case "4":
                System.out.println("=== Choice 4 ===");
                // code                    
                break;
            case "5":
                System.out.println("=== Choice 5 ===");                
                // code                    
            case "6":
                System.out.println("=== Choice 6 ===");                
                // code                    
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void printReport(List<String> lines, boolean wait_for_enter, String header) {
        if (header != null) {
            print_info_message(header, '-');
        }
        
        lines.forEach(line -> 
            System.out.println(Utils.add_offset_to_string(line, MenuConst.OFFSET_MENU_TITLE))
        );
        
        if (wait_for_enter) {
            System.out.print("\n" + Utils.add_offset_to_string("--> Press Enter to continue... ", 
                MenuConst.OFFSET_MENU_TITLE));
            scanner.nextLine();
        }
    }   

    public static void printReport(List<String> lines, boolean wait_for_enter) {
        printReport(lines, wait_for_enter, null);
    }



}
