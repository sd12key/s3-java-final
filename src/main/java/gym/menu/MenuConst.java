package gym.menu;

public final class MenuConst {
    public static final int WIDTH_MENU_TITLE = 60;
    public static final int OFFSET_MENU_TITLE = 5;
    public static final int OFFSET_MENU_ITEMS = 20;

    public static final String APP_WELCOME_MSG = "WELCOME TO GYM APP";
    public static final String MAIN_MENU = " MAIN MENU ";
    public static final String[] MAIN_MENU_ITEMS = {
        "(1) Login",
        "(2) Register Member", 
        "(3) Register Trainer",
        "(4) Register Admin",
        "(0) Exit"
    };

    public static final String ADMIN_MENU = " ADMIN ";
    public static final String[] ADMIN_MENU_ITEMS = {
        "(1) View Account Details",
        "(2) List All Registered Users",
        "(3) View User Account Details",
        "(4) Delete User Account",
        "(5) View All Memberships",
        "(6) List Workout Classes",
        "(7) View Revenue Report",
        "(0) Logout"
    };

    public static final String MEMBER_MENU = " MEMBER ";
    public static final String[] MEMBER_MENU_ITEMS = {
        "(1) View Account Details",
        "(2) List Workout Classes",
        "(3) Purchase Membership",
        "(4) View Membership/Expenses",
        "(0) Logout"
    };

    public static final String TRAINER_MENU = " TRAINER ";
    public static final String[] TRAINER_MENU_ITEMS = {
        "(1) View Account Details",
        "(2) List All Workout Classes",
        "(3) View My Workout Classes",    
        "(4) Create Workout Class",
        "(5) Modify Workout Class",
        "(6) Delete Workout Class",
        "(7) Purchase Membership", 
        "(8) View Membership/Expenses",        
        "(0) Logout"
    };

    // Regex patterns for various inputs
    public static final String REGEX_USERNAME = "^[a-zA-Z][a-zA-Z0-9_-]*$";;
    public static final String REGEX_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
    public static final String REGEX_FULL_NAME = "^[\\p{L} .'-]+$";
    public static final String REGEX_ADDRESS = "^[\\p{L}0-9\\-\\.,#'/ ]+$";
    public static final String REGEX_EMAIL = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
    public static final String REGEX_NON_EMPTY = "^.+$";
    public static final String REGEX_PHONE_10_DIGIT = "^\\d{10}$";
    public static final String REGEX_CREDIT_CARD_NUMBER = "^\\d{16}$";
    public static final String REGEX_CREDIT_CARD_EXP = "^(0[1-9]|1[0-2])/\\d{2}$";
    public static final String REGEX_CREDIT_CARD_CVV = "^\\d{3}$";
    
}
