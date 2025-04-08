package gym.database;

public class SQLTemplates {
    public static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + DBConst.SCHEMA;
    public static final String SQL_SET_SCHEMA = "SET search_path TO " + DBConst.SCHEMA;

    // ======== Membership types table creation and data insertion ==========
    public static final String SQL_CREATE_MEMBERSHIP_TYPES_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.MembershipTypes.TABLE + " (" +
        DBConst.MembershipTypes.ID + " SERIAL PRIMARY KEY, " +
        DBConst.MembershipTypes.USER_ROLE + " VARCHAR(20) NOT NULL, " +
        DBConst.MembershipTypes.TYPE + " VARCHAR(50) NOT NULL, " +
        DBConst.MembershipTypes.DESCRIPTION + " TEXT, " +
        DBConst.MembershipTypes.DURATION_MONTHS + " INT, " +
        DBConst.MembershipTypes.COST + " DECIMAL(7,2)" +
        ")";

    public static final String SQL_INSERT_MEMBERSHIP_TYPE =
    "INSERT INTO " + DBConst.MembershipTypes.TABLE + " (" +
    DBConst.MembershipTypes.USER_ROLE + ", " +
    DBConst.MembershipTypes.TYPE + ", " +
    DBConst.MembershipTypes.DESCRIPTION + ", " +
    DBConst.MembershipTypes.DURATION_MONTHS + ", " +
    DBConst.MembershipTypes.COST +
    ") VALUES (?, ?, ?, ?, ?)";

    public static final String SQL_INSERT_MEMBERSHIP_TYPE_RETURN_ID =
    SQL_INSERT_MEMBERSHIP_TYPE + " RETURNING " + DBConst.MembershipTypes.ID;
    
    public static final String SQL_SELECT_MEMBERSHIP_TYPE_BY_ID =
    "SELECT * FROM " + DBConst.MembershipTypes.TABLE + " WHERE " + DBConst.MembershipTypes.ID + " = ?";

    public static final String SQL_SELECT_MEMBERSHIP_TYPE_BY_USER_ROLE =
    "SELECT * FROM " + DBConst.MembershipTypes.TABLE + " WHERE " + DBConst.MembershipTypes.USER_ROLE + " = ? + ORDER BY " + DBConst.MembershipTypes.ID;
    
    public static final String SQL_SELECT_ALL_MEMBERSHIP_TYPES =
    "SELECT * FROM " + DBConst.MembershipTypes.TABLE + " ORDER BY " + DBConst.MembershipTypes.ID;

    // ======== Users table creation and data insertion =========
    public static final String SQL_CREATE_USERS_TABLE =
        "CREATE TABLE IF NOT EXISTS " + 
        DBConst.Users.TABLE + " (" +
        DBConst.Users.ID + " SERIAL PRIMARY KEY, " +
        DBConst.Users.USERNAME + " VARCHAR(50) NOT NULL UNIQUE, " +
        DBConst.Users.PASSWORD_HASH + " VARCHAR(255) NOT NULL, " +
        DBConst.Users.EMAIL + " VARCHAR(100), " +
        DBConst.Users.FULL_NAME + " VARCHAR(100), " +
        DBConst.Users.ADDRESS + " TEXT, " +
        DBConst.Users.PHONE_NUMBER + " VARCHAR(20), " +
        DBConst.Users.ROLE + " VARCHAR(20) NOT NULL" +
        ")";

    public static final String SQL_SELECT_USER_BY_ID =
        "SELECT * FROM " + DBConst.Users.TABLE + " WHERE " + DBConst.Users.ID + " = ?";

    public static final String SQL_SELECT_USER_BY_USERNAME =
        "SELECT * FROM " + DBConst.Users.TABLE + " WHERE " + DBConst.Users.USERNAME + " = ?";

    public static final String SQL_SELECT_ALL_USERS =
        "SELECT * FROM " + DBConst.Users.TABLE + " ORDER BY " + DBConst.Users.ID;

    public static final String SQL_INSERT_USER =
        "INSERT INTO " + DBConst.Users.TABLE + " (" +
        DBConst.Users.USERNAME + ", " +
        DBConst.Users.PASSWORD_HASH + ", " +
        DBConst.Users.EMAIL + ", " +
        DBConst.Users.FULL_NAME + ", " +
        DBConst.Users.ADDRESS + ", " +
        DBConst.Users.PHONE_NUMBER + ", " +
        DBConst.Users.ROLE +
        ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_INSERT_USER_RETURN_ID =
        SQL_INSERT_USER + " RETURNING " + DBConst.Users.ID;

    public static final String SQL_UPDATE_USER_BY_ID =
        "UPDATE " + DBConst.Users.TABLE + " SET " +
        DBConst.Users.USERNAME + " = ?, " +
        DBConst.Users.PASSWORD_HASH + " = ?, " +
        DBConst.Users.EMAIL + " = ?, " +
        DBConst.Users.FULL_NAME + " = ?, " +
        DBConst.Users.ADDRESS + " = ?, " +
        DBConst.Users.PHONE_NUMBER + " = ?, " +
        DBConst.Users.ROLE + " = ? WHERE " + DBConst.Users.ID + " = ?";
    
    public static final String SQL_DELETE_USER_BY_ID =
        "DELETE FROM " + DBConst.Users.TABLE + " WHERE " + DBConst.Users.ID + " = ?";        
    
    // ========= Memberships table creation and data insertion ========

    // ON DELETE CASCADE is used here to automatically delete memberships when the user is deleted
    public static final String SQL_CREATE_MEMBERSHIPS_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.Memberships.TABLE + " (" +
        DBConst.Memberships.ID + " SERIAL PRIMARY KEY, " +
        DBConst.Memberships.TYPE_ID + " INT NOT NULL REFERENCES " +
        DBConst.MembershipTypes.TABLE + "(" + DBConst.MembershipTypes.ID + "), " +
        DBConst.Memberships.MEMBER_ID + " INT NOT NULL REFERENCES " +
        DBConst.Users.TABLE + "(" + DBConst.Users.ID + ") ON DELETE CASCADE, " +
        DBConst.Memberships.PURCHASE_DATE + " DATE NOT NULL" +
        ")";

    // ========= Workout classes table creation and data insertion ========

    // ON DELETE CASCADE is used here to automatically delete workout classes when the trainer is deleted
    public static final String SQL_CREATE_WORKOUT_CLASSES_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.WorkoutClasses.TABLE + " (" +
        DBConst.WorkoutClasses.ID + " SERIAL PRIMARY KEY, " +
        DBConst.WorkoutClasses.TYPE + " VARCHAR(100) NOT NULL, " +
        DBConst.WorkoutClasses.DESCRIPTION + " TEXT, " +
        DBConst.WorkoutClasses.TRAINER_ID + " INT NOT NULL REFERENCES " +
        DBConst.Users.TABLE + "(" + DBConst.Users.ID + ") ON DELETE CASCADE" +
        ")";

    public static final String SQL_DROP_TABLES = 
        "DROP TABLE IF EXISTS " + 
        DBConst.Users.TABLE + ", " +
        DBConst.MembershipTypes.TABLE + ", " +
        DBConst.Memberships.TABLE + ", " +
        DBConst.WorkoutClasses.TABLE;

    

}
