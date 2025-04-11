package gym.users.childclasses;

import gym.users.User;
import gym.menu.MenuConst;
import gym.menu.MenuService;

public class Member extends User{
    // Constructor for Trainer class
    public Member(int id, String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(id, username, password_hash, email, full_name, address, phone_number, User.ROLE_MEMBER);
    }

    // Constructor for new Member (id is preset to 0)
    public Member(String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(username, password_hash, email, full_name, address, phone_number, User.ROLE_MEMBER);
    }

    public boolean canHaveMembership() {
        return true; 
    }

    public boolean canTeachClass() {
        return false; 
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showUserMenu() {
        MenuService.showUser(this);
    }

    @Override
    public String[] getMenuItems() {
        return MenuConst.MEMBER_MENU_ITEMS;
    }

    @Override
    public void handleMenuChoice(String choice) {
        MenuService.handleMemberMenu(this, choice);
    }

}


