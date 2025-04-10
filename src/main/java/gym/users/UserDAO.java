package gym.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.users.interfaces.RoleBasedAccess;
import gym.database.DBConst;

public abstract class UserDAO {

    public static boolean addNew(User user, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_INSERT_USER, exit_on_error);
    
            DatabaseConnection.psSetString(ps, 1, user.getUsername(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, user.getPasswordHash(), exit_on_error);
            DatabaseConnection.psSetString(ps, 3, user.getEmail(), exit_on_error);
            DatabaseConnection.psSetString(ps, 4, user.getFullName(), exit_on_error);
            DatabaseConnection.psSetString(ps, 5, user.getAddress(), exit_on_error);
            DatabaseConnection.psSetString(ps, 6, user.getPhoneNumber(), exit_on_error);
            DatabaseConnection.psSetString(ps, 7, user.getRole(), exit_on_error);
            int rows_updated = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_updated > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    public static boolean addNew(User user) {
        return addNew(user, true);
    }

    public static int addNewReturnId(User user, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_INSERT_USER_RETURN_ID, exit_on_error);
    
            DatabaseConnection.psSetString(ps, 1, user.getUsername(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, user.getPasswordHash(), exit_on_error);
            DatabaseConnection.psSetString(ps, 3, user.getEmail(), exit_on_error);
            DatabaseConnection.psSetString(ps, 4, user.getFullName(), exit_on_error);
            DatabaseConnection.psSetString(ps, 5, user.getAddress(), exit_on_error);
            DatabaseConnection.psSetString(ps, 6, user.getPhoneNumber(), exit_on_error);
            DatabaseConnection.psSetString(ps, 7, user.getRole(), exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                return DatabaseConnection.rsGetInt(rs, DBConst.Users.ID, exit_on_error);
            } else {
                if (exit_on_error) {
                    System.err.println("User Insert failed: no ID returned.");
                    System.exit(99);
                }
                return -1;
            }
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static int addNewReturnId(User user) {
        return addNewReturnId(user, true);
    }

    // get user by id from the database
    public static User getById(int user_id, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_SELECT_USER_BY_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, user_id, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            User user = null;
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                user = buildFromResultSet(rs, exit_on_error);
            }

            return user;

        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overload method to exit on any error
    public static User getById(int user_id) {
        return getById(user_id, true);
    }

    // get user by username from the database
    public static User getByUsername(String username, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_SELECT_USER_BY_USERNAME, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, username, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            User user = null;
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                user = buildFromResultSet(rs, exit_on_error);
            }

            return user;

        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overload method to exit on any error
    public static User getByUsername(String username) {
        return getByUsername(username, true);
    }

    // get all users from the database
    public static ArrayList<User> getAllUsers(boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_SELECT_ALL_USERS, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                User user = buildFromResultSet(rs, exit_on_error);
                if (user != null) {
                    users.add(user);
                }
            }

            return users;

        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overload method to exit on any error
    public static ArrayList<User> getAllUsers() {
        return getAllUsers(true);
    }

    // update user by id in the database
    public static boolean updateById(User user, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
    
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_UPDATE_USER_BY_ID, exit_on_error);
    
            DatabaseConnection.psSetString(ps, 1, user.getUsername(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, user.getPasswordHash(), exit_on_error);
            DatabaseConnection.psSetString(ps, 3, user.getEmail(), exit_on_error);
            DatabaseConnection.psSetString(ps, 4, user.getFullName(), exit_on_error);
            DatabaseConnection.psSetString(ps, 5, user.getAddress(), exit_on_error);
            DatabaseConnection.psSetString(ps, 6, user.getPhoneNumber(), exit_on_error);
            DatabaseConnection.psSetString(ps, 7, user.getRole(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 8, user.getId(), exit_on_error);
    
            int rows_updated = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_updated > 0;
    
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static boolean updateById(User user) {
        return updateById(user, true);
    }

    // delete user by id from the database
    public static boolean deleteById(int user_id, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
    
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_DELETE_USER_BY_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, user_id, exit_on_error);
    
            int rows_deleted = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_deleted > 0;
    
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static boolean deleteById(int user_id) {
        return deleteById(user_id, true);
    }

    // build a User object from the ResultSet
    // this method is private to prevent direct access from outside the class
    private static User buildFromResultSet(ResultSet rs, boolean exit_on_error) {
        int id = DatabaseConnection.rsGetInt(rs, DBConst.Users.ID, exit_on_error);
        String username = DatabaseConnection.rsGetString(rs, DBConst.Users.USERNAME, exit_on_error);
        String password_hash = DatabaseConnection.rsGetString(rs, DBConst.Users.PASSWORD_HASH, exit_on_error);
        String email = DatabaseConnection.rsGetString(rs, DBConst.Users.EMAIL, exit_on_error);
        String full_name = DatabaseConnection.rsGetString(rs, DBConst.Users.FULL_NAME, exit_on_error);
        String address = DatabaseConnection.rsGetString(rs, DBConst.Users.ADDRESS, exit_on_error);
        String phone_number = DatabaseConnection.rsGetString(rs, DBConst.Users.PHONE_NUMBER, exit_on_error);
        String role = DatabaseConnection.rsGetString(rs, DBConst.Users.ROLE, exit_on_error);
        return RoleBasedAccess.createUser(id, username, password_hash, email, full_name, address, phone_number, role, exit_on_error);
    }

    // overload method to exit on any error
    public static User buildFromResultSet(ResultSet rs) {
        return buildFromResultSet(rs, true);
    }

}
