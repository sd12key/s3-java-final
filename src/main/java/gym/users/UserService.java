package gym.users;

import gym.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import gym.users.interfaces.RoleBasedAccess;

public class UserService {

    public static boolean isUsernameAvailable(String username) {
        // Check if the username is available in the database
        return UserDAO.getByUsername(username) == null;
    }

    // Login method to authenticate a user
    public static User login(String username, String password) {
        User user = UserDAO.getByUsername(username);
        if (user != null && Utils.check_password(password, user.getPasswordHash())) {
            return user;
            } 
        return null;
    }

    // Register a new user in the database
    public static boolean register(User user) {
        return UserDAO.addNew(user); 
    }

    // Overload register new user with all fields
    public static boolean register(String username, String password_hash, String email, String full_name, String address, String phone_number, String role) {
        User user = RoleBasedAccess.createUser(username, password_hash, email, full_name, address, phone_number, role);
        return register(user);
    }

    // get user by object
    public static User getUser(User user) {
        return UserDAO.getById(user.getId());
    }

    // get user by id
    public static User getUser(int id) {
        return UserDAO.getById(id);
    }

    public static List<String> getUserDetailsReport(User user) {
        List<String> details = new ArrayList<>();
        User fetched_user = UserDAO.getById(user.getId());
        if (fetched_user == null) {
            details.add(">>> User not found.");
            return details;
        }
        details.add("User ID: " + fetched_user.getId());
        details.add("Role: " + Utils.FirstCharToUpperCase(fetched_user.getRole()));
        details.add("Username: " + fetched_user.getUsername());
        details.add("Email: " + fetched_user.getEmail());
        details.add("Name: " + fetched_user.getFullName());
        details.add("Address: " + fetched_user.getAddress());
        details.add("Phone: " + fetched_user.getPhoneNumber());
        return details;
    }

    public static List<String> getUserDetailsReport(int id) {
        List<String> details = new ArrayList<>();
        User user = UserDAO.getById(id);
        if (user == null) {
            details.add(">>> User not found.");
            return details;
        }
        details.add("User ID: " + user.getId());
        details.add("Role: " + Utils.FirstCharToUpperCase(user.getRole()));
        details.add("Username: " + user.getUsername());
        details.add("Email: " + user.getEmail());
        details.add("Name: " + user.getFullName());
        details.add("Address: " + user.getAddress());
        details.add("Phone: " + user.getPhoneNumber());
        return details;
    }

    public static List<User> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    public static List<String> getAllUsersReport() {
        List<String> report = new ArrayList<>();
        List<User> users = UserDAO.getAllUsers();
        if (users == null || users.isEmpty()) {
            report.add("No users found.");
            return report;
        }
        for (User user : users) {
            report.add(user.toString());
        }
        return report;
    }

}
