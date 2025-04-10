package gym.users.childclasses;

import gym.users.User;

public class Trainer extends User{
    // Constructor for Trainer class
    public Trainer(int id, String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(id, username, password_hash, email, full_name,  address, phone_number, User.ROLE_TRAINER);
    }

    // Constructor for new Trainer (id is preset to 0)
    public Trainer(String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(username, password_hash, email, full_name, address, phone_number, User.ROLE_TRAINER);
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showUserMenu() {
        System.out.println("=== Trainer Menu ===");
        System.out.println("1. View Workout Classes");
        System.out.println("2. Create Workout Class");
        System.out.println("3. Modify Workout Class");
        System.out.println("4. Delete Workout Class");
        System.out.println("5. Purchase Membership");
        System.out.println("0. Logout");
    }
}
