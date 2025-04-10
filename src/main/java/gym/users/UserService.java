package gym.users;

import gym.utilities.Utils;
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

}
