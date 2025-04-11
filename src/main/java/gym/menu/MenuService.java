package gym.menu;

import java.util.List;
import java.util.Scanner;

import gym.users.UserService;
import gym.memberships.MembershipService;
import gym.workoutclasses.WorkoutClassService;
import gym.users.User;
import gym.users.childclasses.Trainer;

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
                print_choice_name(user, choice);
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":
                // view all users
                print_choice_name(user, choice);
                printReport(UserService.getAllUsersReport(), true);
                break;
            case "3":
                // view user account info
                print_choice_name(user, choice);
                showUserInformation();
                break;
            case "4":
                // delete user account
                print_choice_name(user, choice);
                deleteUser(user.getId());
                break;
            case "5":
                // view all memberships
                print_choice_name(user, choice);
                printReport(MembershipService.getAllMembershipsReport(), true);
                break;
            case "6":
                // list all classes
                print_choice_name(user, choice);
                printReport(WorkoutClassService.getAllWorkoutClassesReport(), true);
                break;
            case "7":
                // view all memberships
                print_choice_name(user, choice);
                printReport(MembershipService.getRevenueReport(), true);
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
                print_choice_name(user, choice);
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":
                // view all classes            
                print_choice_name(user, choice);
                printReport(WorkoutClassService.getAllWorkoutClassesReport(), true);
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
                print_invalid_choice();
        }
    }

    // implementations of menu handlers for Trainer
    public static void handleTrainerMenu(User user, String choice) {
        switch (choice) {
            case "1":
                // view account info
                print_choice_name(user, choice);
                printReport(UserService.getUserDetailsReport(user), true);
                break;
            case "2":
                // view all classes
                print_choice_name(user, choice);
                printReport(WorkoutClassService.getAllWorkoutClassesReport(), true);
                // code                    
                break;
            case "3":
                // view my classes
                print_choice_name(user, choice);
                printReport(WorkoutClassService.getTrainerWorkoutClassesReport((Trainer) user), true);
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
                print_invalid_choice(); 
        }
    }

    public static void showUserInformation() {
        int user_id = enterId("Enter User ID to View Account Info");
        if (user_id == 0) { return; }
        System.out.println();
        User fetch_user = UserService.getUser(user_id);
        if (fetch_user == null) { user_id = 0; }
        printReport(UserService.getUserDetailsReport(user_id), false);
        if (fetch_user != null && fetch_user.canHaveMembership()) {
            System.out.println();
            printReport(MembershipService.getMembershipsByUserReport(fetch_user), false);
        }
        if (fetch_user != null && fetch_user.canTeachClass()) {
            System.out.println();
            printReport(WorkoutClassService.getTrainerWorkoutClassesReport((Trainer) fetch_user), false);
        }
        wait_for_enter();
    }

    public static void deleteUser(int admin_id) {
        int user_id = enterId("Enter Account User ID to Delete");
        if (user_id == 0) { return; }
        if (user_id == admin_id) {
            System.out.println(Utils.add_offset_to_string("<!> You cannot delete your own account.", MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }
        System.out.println();
        User fetch_user = UserService.getUser(user_id);
        if (fetch_user == null) {
            System.out.println(Utils.add_offset_to_string(">>> User not found.", MenuConst.OFFSET_MENU_TITLE));
        } else {
            System.out.println(Utils.add_offset_to_string(">>> User found: " + fetch_user.getFullName(), MenuConst.OFFSET_MENU_TITLE));
            System.out.println(Utils.add_offset_to_string("          Role: " + Utils.firstCharToUpperCase(fetch_user.getRole()), MenuConst.OFFSET_MENU_TITLE));
            System.out.println(Utils.add_offset_to_string("      Username: " + fetch_user.getUsername(), MenuConst.OFFSET_MENU_TITLE));
            System.out.println(Utils.add_offset_to_string("         Email: " + fetch_user.getFullName(), MenuConst.OFFSET_MENU_TITLE));
            int total_count = 0;
            if (fetch_user.canHaveMembership()) {
                int [] membership_counts = MembershipService.calculateMembershipCounts(MembershipService.getMembershipsByUser(fetch_user));
                int active_count = membership_counts[0];
                int expired_count = membership_counts[1];
                total_count = active_count + expired_count;
                if (total_count > 0) {
                    System.out.println(Utils.add_offset_to_string("   Memberships: " + total_count + " (active: " + active_count + ", expired: " + expired_count + ")", MenuConst.OFFSET_MENU_TITLE));
                } else {
                    System.out.println(Utils.add_offset_to_string("   Memberships: None", MenuConst.OFFSET_MENU_TITLE));
                }
            }
            int class_count = 0;
            if (fetch_user.canTeachClass()) {
                class_count = WorkoutClassService.getWorkoutClassesByTrainer((Trainer) fetch_user).size();
                if (class_count > 0) {
                    System.out.println(Utils.add_offset_to_string("       Classes: " + class_count, MenuConst.OFFSET_MENU_TITLE));
                } else {
                    System.out.println(Utils.add_offset_to_string("       Classes: None", MenuConst.OFFSET_MENU_TITLE));
                }
            }
            System.out.println();
            // confirm action                    
            boolean delete_confirmed = confirm_action("==> Are you sure you want to delete this user? (y/n): ");
            boolean delete_confirmed2 = false;
            // confirm one more if user has memberships or classes
            if (delete_confirmed && (total_count > 0 || class_count > 0)) {
                System.out.println(Utils.add_offset_to_string("<!> This user has memberships and/or classes. All information will be lost on deletion!", MenuConst.OFFSET_MENU_TITLE));
                delete_confirmed2 = confirm_action("==> Are you sure you want to delete this user? (y/n): ");
            }
            // now we had two confirmations, so we can delete the user
            if (delete_confirmed && delete_confirmed2) {
                
                // deletion
                boolean user_deleted_ok = UserService.deleteUser(user_id);

                if (user_deleted_ok) {
                    System.out.println(Utils.add_offset_to_string("<-> User deleted successfully.", MenuConst.OFFSET_MENU_TITLE));
                } else {
                    System.out.println(Utils.add_offset_to_string("<!> Failed to delete user.", MenuConst.OFFSET_MENU_TITLE));
                }

            // user deletion was cancelled
            } else {
                    System.out.println(Utils.add_offset_to_string("--> User deletion cancelled.", MenuConst.OFFSET_MENU_TITLE));
            }
        }
        wait_for_enter();                    
    }

    public static void print_choice_name(User user, String choice) {
        print_info_message(user.getMenuItems()[Utils.validate_id(choice)-1], '>');
    }

    public static void wait_for_enter() {
        System.out.print("\n" + Utils.add_offset_to_string("--> Press Enter to continue... ", 
        MenuConst.OFFSET_MENU_TITLE));
        scanner.nextLine();
    }

    public static void printReport(List<String> lines, boolean wait_for_enter, String header) {
        if (header != null) {
            print_info_message(header, '-');
        }
        
        lines.forEach(line -> 
            System.out.println(Utils.add_offset_to_string(line, MenuConst.OFFSET_MENU_TITLE))
        );
        
        if (wait_for_enter) { wait_for_enter(); }
    }   

    public static void printReport(List<String> lines, boolean wait_for_enter) {
        printReport(lines, wait_for_enter, null);
    }

    public static int enterId(String prompt) {
        while (true) {
            System.out.print(Utils.add_offset_to_string(prompt + " (0 to exit): ", MenuConst.OFFSET_MENU_TITLE));
            String input = scanner.nextLine();
            int id = Utils.validate_id(input);
            if (id != -1) {
                return id;
            }
            System.out.println(Utils.add_offset_to_string("--> Invalid input.", MenuConst.OFFSET_MENU_TITLE));
        }
    }

    public static boolean confirm_action(String prompt) {
        System.out.print(Utils.add_offset_to_string(prompt, MenuConst.OFFSET_MENU_TITLE));
        String confirm = scanner.nextLine().trim();
        return "y".equalsIgnoreCase(confirm);
    }


}
