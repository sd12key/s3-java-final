package gym.users.childclasses;

import gym.users.User;

public class Member extends User{
    // Constructor for Trainer class
    public Member(int id, String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(id, username, password_hash, email, full_name, address, phone_number, User.ROLE_MEMBER);
    }

    // Constructor for new Member (id is preset to 0)
    public Member(String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(username, password_hash, email, full_name, address, phone_number, User.ROLE_MEMBER);
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showUserMenu() {
        System.out.println("=== Member Menu ===");
        System.out.println("1. View Workout Classes");
        System.out.println("2. Purchase Membership");
        System.out.println("3. View Membership Expences");
        System.out.println("0. Logout");
    }
}
