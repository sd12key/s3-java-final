package gym.users.childclasses;

import gym.users.User;

public class Trainer extends User{
    // Constructor for Trainer class
    public Trainer(int id, String username, String password_hash, String email, String phone_number, String address) {
        super(id, username, password_hash, email, phone_number, address, User.ROLE_TRAINER);
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showMenu() {
        System.out.println("=== Trainer Menu ===");
        System.out.println("1. View Workout Classes");
        System.out.println("2. Create Workout Class");
        System.out.println("3. Modify Workout Class");
        System.out.println("4. Delete Workout Class");
        System.out.println("5. Purchase Membership");
        System.out.println("0. Logout");
    }
}
