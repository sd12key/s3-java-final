package gym.menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import gym.users.UserService;
import gym.memberships.Membership;
import gym.memberships.MembershipService;
import gym.memberships.MembershipType;
import gym.memberships.MembershipTypeService;
import gym.workoutclasses.WorkoutClass;
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
            User logged_user = null;
            String role = null;
            
            switch (choice) {
                case "1":
                    print_info_message(MenuConst.MAIN_MENU_ITEMS[0], '>');
                    logged_user = loginUser();
                    if (logged_user != null) {
                        print_info_message("Welcome, " + logged_user.getFullName() + "!", '*');
                        // *** ROLE-BASED ACCESS ***
                        // loged_user.showUserMenu();
                        logged_user.showUserMenu();
                    }
                    break;
                case "2":
                    // Register a new user (Member)
                    print_info_message(MenuConst.MAIN_MENU_ITEMS[1], '>');
                    role = User.ROLE_MEMBER;
                    registerUser(role);
                    break;
                case "3":
                    // Register a new user (Trainer)
                    print_info_message(MenuConst.MAIN_MENU_ITEMS[2], '>');
                    role = User.ROLE_TRAINER;
                    registerUser(role);                    
                    break;
                case "4":
                    // Register a new user (Admin)                
                    print_info_message(MenuConst.MAIN_MENU_ITEMS[3], '>');
                    role = User.ROLE_ADMIN;
                    registerUser(role);
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
                user.getUsername() + " (" + user.getEmail() + ")", 
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

    public static User loginUser() {
        // User login
        System.out.println(Utils.add_offset_to_string(">>> User Login (entering -1 to aborts process)\n", MenuConst.OFFSET_MENU_TITLE));

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);
        String error_msg = "<!> Invalid Credentials, please try again.";
        String cancel_msg = "<!> Login Procedure cancelled.";

        String username = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_NON_EMPTY,
            sStr + "    Enter Username: ",
            sStr + "<!> Invalid Username, try again, or enter -1 to abort.",
            false
        );

        // username = "member_li";
        // username = "trainer_bob";
        if (username == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return null;
        }

        String password = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_NON_EMPTY,
            sStr + "    Enter Password: ",
            sStr + "<!> Invalid Password, try again, or enter -1 to abort.",
            false
        );

        // password = "pass123";
        // password = "train123";
        if (password == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return null;
        }

        User logged_user = UserService.login(username, password);
        if (logged_user == null) {
            System.out.println();
            System.out.println(Utils.add_offset_to_string(error_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return null;
        }    
        return logged_user;

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
                // purchase membership
                print_choice_name(user, choice);
                purchaseMembership(user);
                break;
            case "4":
                print_choice_name(user, choice);
                printReport(MembershipService.getMembershipsAndExpencesReport(user), true);
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
                break;
            case "3":
                // view my classes
                print_choice_name(user, choice);
                printReport(WorkoutClassService.getTrainerWorkoutClassesReport((Trainer) user), true);
                break;
            case "4":
                // create class
                print_choice_name(user, choice);
                createWorkoutClass((Trainer) user);
                break;
            case "5":
                print_choice_name(user, choice);
                updateWorkoutClass((Trainer) user);
                break;
            case "6":
                print_choice_name(user, choice);
                deleteWorkoutClass((Trainer) user);
                break;
            case "7":
                print_choice_name(user, choice);
                purchaseMembership(user);
                break;
            case "8":
                print_choice_name(user, choice);
                printReport(MembershipService.getMembershipsAndExpencesReport(user), true);
                break;
            default:
                print_invalid_choice(); 
        }
    }

    // register
    public static void registerUser(String role) {
        System.out.println(Utils.add_offset_to_string(">>> User Registration (" + Utils.firstCharToUpperCase(role) + ")", MenuConst.OFFSET_MENU_TITLE));
        System.out.println(Utils.add_offset_to_string("    (entering -1 to aborts process)", MenuConst.OFFSET_MENU_TITLE));

        // Register a new user (Member, Trainer, or Admin)
        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);
        String error_msg = sStr + "<!> Invalid input. Please try again. -1 to cancel.";
        String cancel_msg = "<!> User Registration cancelled.";
        boolean unique_username = false;
        String username = null;

        do {
            username = MenuService.getValidatedInput(
                scanner,
                MenuConst.REGEX_USERNAME,
                sStr + "    Enter Username: ",
                sStr + "<!> Invalid Username; only letters, numbers and '_-' are allowed.",
                false
            );

            if (username == null) {
                System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
                wait_for_enter();
                return;
            }

            unique_username = UserService.isUsernameAvailable(username);
            if (!unique_username) {
                System.out.println(Utils.add_offset_to_string("<!> Username not available. Please choose another one.", MenuConst.OFFSET_MENU_TITLE));
            }
        } while (!unique_username);

        String password = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_PASSWORD,
            sStr + "    Enter Password: ",
            sStr + "<!> At least 8 characters, 1 upper, 1 lowercase, 1 digit.",
            false
        );

        if (password == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        String password_hash = Utils.hash_password(password);       

        String full_name = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_FULL_NAME,
            sStr + "    Enter Full Name: ",
            error_msg,
            false
        );

        if (full_name == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        String address = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_ADDRESS,
            sStr + "    Enter Full Address: ",
            error_msg,
            false
        );

        if (address == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        String phone = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_PHONE_10_DIGIT,
            sStr + "    Enter Phone: ",
            sStr + "<!> Phone number must be 10-digits.",
            false
        );

        if (phone == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }
        
        String email = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_EMAIL,
            sStr + "    Enter Email: ",
            sStr + "<!> Invalid email format, please try again.",
            false
        );

        if (email == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        email = email.toLowerCase();
        
        // Call the register method from UserService
        if (!UserService.register(username, password_hash, email, full_name, address, phone, role)) {
            System.out.println(Utils.add_offset_to_string("<!> User Registration failed, please try again.", MenuConst.OFFSET_MENU_TITLE));
        } else {
            System.out.println(Utils.add_offset_to_string("<+> User Registration was successfull, you can now login.", MenuConst.OFFSET_MENU_TITLE));
        }

        wait_for_enter();
    };


    public static void showUserInformation() {
        int user_id = enterId("Enter User ID (UID) to View Account Info");
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

    public static void deleteWorkoutClass(Trainer user) {
        int class_id = enterId("Enter Workout Class ID (CID) to Delete");
        if (class_id == 0) { return; }
        System.out.println();
        WorkoutClass fetch_class = WorkoutClassService.getWorkoutClassById(class_id);
        if (fetch_class == null) {
            System.out.println(Utils.add_offset_to_string(">>> Workout Class not found.", MenuConst.OFFSET_MENU_TITLE));
        } else if (fetch_class.getTrainer().getId() != user.getId()) {
            System.out.println(Utils.add_offset_to_string("<!> You are not the Trainer for this Class.", MenuConst.OFFSET_MENU_TITLE));
        } else {
            // now trying to delete
            System.out.println(Utils.add_offset_to_string(">>> Workout Class found:", MenuConst.OFFSET_MENU_TITLE));
            System.out.println(Utils.add_offset_to_string("    " + fetch_class.toString() + "\n", MenuConst.OFFSET_MENU_TITLE));
            boolean delete_confirmed = confirm_action("==> Are you sure you want to delete this Class? (y/n): ");
            if (delete_confirmed) {
                Boolean class_deleted_ok = WorkoutClassService.deleteWorkoutClass(fetch_class);
                if (class_deleted_ok) {
                    System.out.println(Utils.add_offset_to_string("<-> Workout Class deleted successfully.", MenuConst.OFFSET_MENU_TITLE));
                } else {
                    System.out.println(Utils.add_offset_to_string("<!> Failed to delete Workout Class.", MenuConst.OFFSET_MENU_TITLE));
                }
            } else {
                System.out.println(Utils.add_offset_to_string("--> Workout Class deletion cancelled.", MenuConst.OFFSET_MENU_TITLE));
            }
        }
        wait_for_enter();
        return;
    }

    public static void updateWorkoutClass(Trainer user) {
        int class_id = enterId("Enter Workout Class ID (CID) to Modify");
        if (class_id == 0) { return; }
        System.out.println();
        WorkoutClass fetch_class = WorkoutClassService.getWorkoutClassById(class_id);
        if (fetch_class == null) {
            System.out.println(Utils.add_offset_to_string(">>> Workout Class not found.", MenuConst.OFFSET_MENU_TITLE));
        } else if (fetch_class.getTrainer().getId() != user.getId()) {
            System.out.println(Utils.add_offset_to_string("<!> You are not the Trainer for this Class.", MenuConst.OFFSET_MENU_TITLE));
        } else {
            // now trying to modify
            System.out.println(Utils.add_offset_to_string(">>> Workout Class found:", MenuConst.OFFSET_MENU_TITLE));
            System.out.println(Utils.add_offset_to_string("    " + fetch_class.toString() + "\n", MenuConst.OFFSET_MENU_TITLE));

            // get new values
            boolean unchanged = true;
            String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);
            String error_msg = sStr + "<!> Invalid input. Please try again. -1 to cancel.";
            String cancel_msg = "<!> Workout Class update cancelled.";
            
            System.out.println(Utils.add_offset_to_string("--> Workout Class Type: " + fetch_class.getType(), MenuConst.OFFSET_MENU_TITLE));
            String type = MenuService.getValidatedInput(
                scanner,
                MenuConst.REGEX_NON_EMPTY,
                sStr + "    New Class Type (Enter = Unchanged): ",
                error_msg,
                true
            );

            if (type == null) {
                System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
                wait_for_enter();
                return;
            } else if (type.equals("-1")) {
                type = fetch_class.getType();
                System.out.println(Utils.add_offset_to_string("<=> Unchanged: " + type, MenuConst.OFFSET_MENU_TITLE));
            } else {
                unchanged = false;
            }

            System.out.println(Utils.add_offset_to_string("--> Workout Class Description: " + fetch_class.getDescription(), MenuConst.OFFSET_MENU_TITLE));
            String desc = MenuService.getValidatedInput(
                scanner,
                MenuConst.REGEX_NON_EMPTY,
                sStr + "    New Description (Enter = Unchanged): ",
                error_msg,
                true
            );

            if (desc == null) {
                System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
                wait_for_enter();
                return;
            } else if (desc.equals("-1")) {
                desc = fetch_class.getDescription();
                System.out.println(Utils.add_offset_to_string("<=> Unchanged: " + desc, MenuConst.OFFSET_MENU_TITLE));
            } else {
                unchanged = false;
            }

            if (unchanged) {
                System.out.println(Utils.add_offset_to_string("<!> No changes made to Workout Class.", MenuConst.OFFSET_MENU_TITLE));
                wait_for_enter();
                return;
            }

            fetch_class.setType(type);
            fetch_class.setDescription(desc);

            System.out.println();
            System.out.println(Utils.add_offset_to_string(">>> Class - " + fetch_class.toString(), MenuConst.OFFSET_MENU_TITLE));

            boolean update_confirmed = confirm_action("==> Are you sure you want to modify this Class? (y/n): ");
            if (update_confirmed) {
                Boolean class_updated_ok = WorkoutClassService.updateWorkoutClass(fetch_class);
                if (class_updated_ok) {
                    System.out.println(Utils.add_offset_to_string("<-> Workout Class updated successfully.", MenuConst.OFFSET_MENU_TITLE));
                } else {
                    System.out.println(Utils.add_offset_to_string("<!> Failed to update Workout Class.", MenuConst.OFFSET_MENU_TITLE));
                }
            } else {
                System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            }
        }
        wait_for_enter();
        return;
    }

    public static void createWorkoutClass(Trainer user) {
        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);
        String error_msg = sStr + "<!> Invalid input. Please try again. -1 to cancel.";
        String cancel_msg = "<!> Workout Class creation cancelled.";
        
        String type = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_NON_EMPTY,
            sStr + "Enter Workout Class Type: ",
            error_msg,
            false
        );

        if (type == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        String desc = MenuService.getValidatedInput(
            scanner,
            MenuConst.REGEX_NON_EMPTY,
            sStr + "Enter Class Description: ",
            error_msg,
            false
        );

        if (desc == null) {
            System.out.println(Utils.add_offset_to_string(cancel_msg, MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        WorkoutClass new_class = new WorkoutClass(type, desc, user);
        System.out.println();
        System.out.println(Utils.add_offset_to_string(">>> Class - " + new_class.toStringNoId(), MenuConst.OFFSET_MENU_TITLE));
        boolean create_confirmed = confirm_action("==> Please, confirm the creation of the new Class ? (y/n): ");
        if (create_confirmed) {
            boolean class_created_ok = WorkoutClassService.addNewWorkoutClass(new_class);
            if (class_created_ok) {
                System.out.println(Utils.add_offset_to_string("<-> Workout Class created successfully.", MenuConst.OFFSET_MENU_TITLE));
            } else {
                System.out.println(Utils.add_offset_to_string("<!> Failed to create Workout Class.", MenuConst.OFFSET_MENU_TITLE));
            }
            } else {
            System.out.println(Utils.add_offset_to_string("--> Workout Class creation cancelled.", MenuConst.OFFSET_MENU_TITLE));
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

    public static void purchaseMembership(User user) {

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);

        if (!user.canHaveMembership()) {
            System.out.println(Utils.add_offset_to_string("<!> You cannot purchase a membership.", MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }
        System.out.println(Utils.add_offset_to_string(">>> Memberships available for purchase:", MenuConst.OFFSET_MENU_TITLE));
        List <MembershipType> available_memberships = MembershipTypeService.getMembershipTypesByRole(user.getRole());
        if (available_memberships == null || available_memberships.isEmpty()) {
            System.out.println(Utils.add_offset_to_string("<!> No memberships available for purchase.", MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }
        int i = 0;
        int max_types = available_memberships.size();
        for (MembershipType membership_type : available_memberships) {
            i++;
            String price_str = "$" + Utils.double_to_str(membership_type.getCost());
            System.out.println(Utils.add_offset_to_string("    (" + (i) + ") " 
                + price_str + " / " + membership_type.getType() + " (" + membership_type.getDescription()+")", MenuConst.OFFSET_MENU_TITLE));
        }
        System.out.println(Utils.add_offset_to_string("    (0) Cancel", MenuConst.OFFSET_MENU_TITLE));
        System.out.println();
        int membership_id = enterId("Select Membership to Purchase", max_types);
        if (membership_id == 0) { return; }

        // Fetch the selected membership type
        MembershipType selected_membership = available_memberships.get(membership_id - 1);  // Remember list is 0-indexed
        
        // Display selected membership info
        System.out.println(sStr + ">>> Membership selected:");
        System.out.println(sStr + "    " + selected_membership.toStringNoId());  
        
        // Check if user already has an active membership
        List<Membership> active_memberships = MembershipService.getActiveMembershipsByUser(user);
        
        Membership last_active_membership = null;

        for (Membership membership : active_memberships) {
            if (last_active_membership == null || membership.getExpirationDate().isAfter(last_active_membership.getExpirationDate())) {
                last_active_membership = membership;
            }
        }
        
        if (last_active_membership != null) {
            System.out.println();
            System.out.println(sStr + "==> You already have an active membership ending " + last_active_membership.getExpirationDate());
            System.out.println(sStr + "<i> Your new membership will start on the next day: " + last_active_membership.getExpirationDate().plusDays(1));
            System.out.println();
        }        

        // Ask for credit card info
        String card_info = enterCreditCardInfo();
        // returns null if user cancels
        if (card_info == null) {
            System.out.println(Utils.add_offset_to_string( "<!> Membership purchase canceled.", MenuConst.OFFSET_MENU_TITLE));
            wait_for_enter();
            return;
        }

        System.out.println(Utils.add_offset_to_string("<i> " + card_info + "\n", MenuConst.OFFSET_MENU_TITLE));

        // Final confirmation for purchase
        boolean confirm_purchase = confirm_action(sStr + "==> Are you sure you want to purchase this membership? (y/n): ");
        if (!confirm_purchase) {
            System.out.println(Utils.add_offset_to_string("--> Membership purchase canceled.", MenuConst.OFFSET_MENU_TITLE));
            return;
        }

        LocalDate purchase_date;

        if (last_active_membership == null) {
            // No active membership, so set purchase date to today
            purchase_date = LocalDate.now();
        } else {
            // There is an active membership, so set purchase date to the day after the last active membership's expiration date
            purchase_date = last_active_membership.getExpirationDate().plusDays(1);
        }

        Membership new_membership = new Membership(selected_membership, user, purchase_date);

        // adding to database
        boolean success = MembershipService.addNewMembership(new_membership); 
        
        if (success) {
            System.out.println(Utils.add_offset_to_string("<-> Membership purchased successfully!", MenuConst.OFFSET_MENU_TITLE));
        } else {
            System.out.println(Utils.add_offset_to_string("<!> Failed to purchase membership.", MenuConst.OFFSET_MENU_TITLE));
        }

        wait_for_enter();
    }

    public static String enterCreditCardInfo() {
        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);
        String error_msg = sStr + "<!> Invalid input. Please try again. -1 to cancel.";

        String card_number = getValidatedInput(
            scanner,
            MenuConst.REGEX_CREDIT_CARD_NUMBER,
            sStr + "==> Enter Credit Card Number (16-digits): ",
            error_msg,
            false
        );
        if (card_number == null) { return null; }

        String card_expiration = getValidatedInput(
            scanner,
            MenuConst.REGEX_CREDIT_CARD_EXP,
            sStr + "==> Enter Expiration (MM/YY): ",
            error_msg,
            false
        );
        if (card_expiration == null) { return null; }

        String card_cvv = getValidatedInput(
            scanner,
            MenuConst.REGEX_CREDIT_CARD_CVV,
            sStr + "==> Enter Card CVV Code (3-digits): ",
            error_msg,
            false
        );
        if (card_cvv == null) { return null; }

        return "Card " + "**** **** **** " + card_number.substring(card_number.length() - 4) + ", Exp. " + card_expiration + ", CVV ***";
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

    public static int enterId(String prompt, int max_id) {
        while (true) {
            System.out.print(Utils.add_offset_to_string(prompt + " (0 to exit): ", MenuConst.OFFSET_MENU_TITLE));
            String input = scanner.nextLine();
            int id = Utils.validate_id(input);
            if (id == 0) { return 0; }
            if (id > 0 && id <= max_id) {
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
    
    public static String getValidatedInput(Scanner scanner,
                                         String regex_pattern,
                                         String prompt,
                                         String error_prompt,
                                         boolean enter_allowed) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            // Handle empty input
            if (input.isEmpty()) {
                if (enter_allowed) {
                    return "-1";
                }
                System.out.println(error_prompt);
                continue;
            }
            
            // Handle cancellation
            if (input.equals("-1")) {
                return null;
            }
            
            // Validate pattern
            if (Pattern.matches(regex_pattern, input)) {
                return input;
            }

            System.out.println(error_prompt);
        }
    }


}
