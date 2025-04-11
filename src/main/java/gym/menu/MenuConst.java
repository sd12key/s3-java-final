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
        "(5) View Memberships and Revenue",
        "(0) Logout"
    };

    public static final String MEMBER_MENU = " MEMBER ";
    public static final String[] MEMBER_MENU_ITEMS = {
        "(1) View Account Details",
        "(2) List All Workout Classes",
        "(3) Purchase Membership",
        "(4) View Membership Expenses",
        "(0) Logout"
    };

    public static final String TRAINER_MENU = " TRAINER ";
    public static final String[] TRAINER_MENU_ITEMS = {
        "(1) View Account Details",
        "(2) List Workout Classes",
        "(3) Create Workout Class",
        "(4) Modify Workout Class",
        "(5) Delete Workout Class",
        "(6) Purchase Membership", 
        "(0) Logout"
    };
}
