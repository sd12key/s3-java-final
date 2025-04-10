package gym.users.childclasses;

import gym.users.User;

public class Admin extends User {

    // Constructor for Admin class
    public Admin(int id, String username, String password_hash, String email, String full_name,  String address, String phone_number) {
        super(id, username, password_hash, email, full_name, address, phone_number, User.ROLE_ADMIN);
    }

    // Constructor for new Admin (id is preset to 0)
    public Admin(String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(username, password_hash, email, full_name, address, phone_number, User.ROLE_ADMIN);
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showUserMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Create User");
        System.out.println("2. Delete User");
        System.out.println("3. View All Memberships");
        System.out.println("4. Total Annual Revenue");
        System.out.println("0. Logout");
    }
    
}
