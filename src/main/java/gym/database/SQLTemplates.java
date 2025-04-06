package gym.database;

import gym.database.DBConst;

public class SQLTemplates {
    public static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + DBConst.SCHEMA;
    public static final String SQL_SET_SCHEMA = "SET search_path TO " + DBConst.SCHEMA;

    public static final String SQL_DROP_TABLES = 
        "DROP TABLE IF EXISTS " + 
        DBConst.Users.TABLE + ", " +
        DBConst.MembershipTypes.TABLE + ", " +
        DBConst.Memberships.TABLE + ", " +
        DBConst.WorkoutClasses.TABLE + ";";

    public static final String SQL_CREATE_USERS_TABLE =
        "CREATE TABLE IF NOT EXISTS " + 
        DBConst.Users.TABLE + " (" +
        DBConst.Users.ID + " SERIAL PRIMARY KEY, " +
        DBConst.Users.USERNAME + " VARCHAR(50) NOT NULL UNIQUE, " +
        DBConst.Users.PASSWORD_HASH + " VARCHAR(255) NOT NULL, " +
        DBConst.Users.EMAIL + " VARCHAR(100), " +
        DBConst.Users.PHONE_NUMBER + " VARCHAR(20), " +
        DBConst.Users.ADDRESS + " TEXT, " +
        DBConst.Users.ROLE + " VARCHAR(20) NOT NULL" +
        ");";

    public static final String SQL_CREATE_MEMBERSHIP_TYPES_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.MembershipTypes.TABLE + " (" +
        DBConst.MembershipTypes.ID + " SERIAL PRIMARY KEY, " +
        DBConst.MembershipTypes.USER_ROLE + " VARCHAR(20) NOT NULL, " +
        DBConst.MembershipTypes.TYPE + " VARCHAR(50) NOT NULL UNIQUE, " +
        DBConst.MembershipTypes.DESCRIPTION + " TEXT, " +
        DBConst.MembershipTypes.DURATION_MONTHS + " INT, " +
        DBConst.MembershipTypes.COST + " DECIMAL(8,2)" +
        ");";

    // ON DELETE CASCADE is used here to automatically delete memberships when the user is deleted
    public static final String SQL_CREATE_MEMBERSHIPS_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.Memberships.TABLE + " (" +
        DBConst.Memberships.ID + " SERIAL PRIMARY KEY, " +
        DBConst.Memberships.TYPE_ID + " INT NOT NULL REFERENCES " +
        DBConst.MembershipTypes.TABLE + "(" + DBConst.MembershipTypes.ID + "), " +
        DBConst.Memberships.MEMBER_ID + " INT NOT NULL REFERENCES " +
        DBConst.Users.TABLE + "(" + DBConst.Users.ID + ") ON DELETE CASCADE, " +
        DBConst.Memberships.PURCHASE_DATE + " DATE NOT NULL" +
        ");";

    // ON DELETE CASCADE is used here to automatically delete workout classes when the trainer is deleted
    public static final String SQL_CREATE_WORKOUT_CLASSES_TABLE =
        "CREATE TABLE IF NOT EXISTS " + DBConst.WorkoutClasses.TABLE + " (" +
        DBConst.WorkoutClasses.ID + " SERIAL PRIMARY KEY, " +
        DBConst.WorkoutClasses.TYPE + " VARCHAR(100) NOT NULL, " +
        DBConst.WorkoutClasses.DESCRIPTION + " TEXT, " +
        DBConst.WorkoutClasses.TRAINER_ID + " INT NOT NULL REFERENCES " +
        DBConst.Users.TABLE + "(" + DBConst.Users.ID + ") ON DELETE CASCADE" +
        ");";

}
