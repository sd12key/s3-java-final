package gym.users.interfaces;

import gym.users.User;
import gym.users.childclasses.Member;
import gym.users.childclasses.Trainer;
import gym.users.childclasses.Admin;

public interface RoleBasedAccess {

    // static method to create a user based on role
    // or this could be a method in a User class
    // my preference is to keep it here (related to RoleBasedAccess)
    static User createUser(int id, String username, String password_hash, String email, String full_name, String address, String phone_number, String role, boolean exit_on_error) {
        switch (role) {
            case User.ROLE_ADMIN:
                return new Admin(id, username, password_hash, email, full_name, address, phone_number);
            case User.ROLE_TRAINER:
                return new Trainer(id, username, password_hash, email, full_name, address, phone_number);
            case User.ROLE_MEMBER:
                return new Member(id, username, password_hash, email, full_name, address, phone_number);
            default:
                if (exit_on_error) {
                    System.err.println("Unknown role: " + role);
                    System.exit(99);
                }
                return null;
        }
    }

    // Overload method with default exit_on_error = true
    static User createUser(int id, String username, String password_hash, String email, String full_name, String address, String phone_number, String role) {
        return createUser(id, username, password_hash, email, full_name, address, phone_number, role, true);
    }

    // overload method with id set to 0 (for new users)
    static User createUser(String username, String password_hash, String email, String full_name, String address, String phone_number, String role, boolean exit_on_error) {
        return createUser(0, username, password_hash, email, full_name, address, phone_number, role, exit_on_error);
    }

    // Overload method with id set to 0 (for new users) and default exit_on_error = true
    static User createUser(String username, String password_hash, String email, String full_name, String address, String phone_number, String role) {
        return createUser(username, password_hash, email, full_name, address, phone_number, role, true);    
    }

    // Generec static method to cast a User to a specific role type
    // This method checks if the user is of the specified role type and returns it if true, otherwise returns null.
    // usage example: Trainer trainer = User.castToRole(user, Trainer.class);
    // checks if role is "trainer" and if user is an instance of Trainer class
    static <T extends User> T castToRole(User user, Class<T> roleClass) {
        if (user != null 
            && user.getRole().equalsIgnoreCase(roleClass.getSimpleName())
            && roleClass.isInstance(user)) { 
            return roleClass.cast(user);
        }
        return null;
    }

    // Methods to be implemented by subclasses
    void showUserMenu();
    void handleMenuChoice(String choice);
    String[] getMenuItems(); 
}
