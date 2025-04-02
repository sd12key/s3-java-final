package gym.users.childclasses;

import gym.users.User;

public class Admin extends User {

    // Constructor for Admin class
    public Admin(int id, String username, String password_hash, String email, String phone_number, String address) {
        super(id, username, password_hash, email, phone_number, address, User.ROLE_ADMIN);
    }

    // Implementing the abstract method from RoleMenu interface
    @Override
    public void showMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Create User");
        System.out.println("2. Delete User");
        System.out.println("3. View All Memberships");
        System.out.println("4. Total Annual Revenue");
        System.out.println("0. Logout");
    }
    
}
