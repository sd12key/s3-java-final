package gym.users.childclasses;

import gym.users.User;
import gym.menu.MenuConst;
import gym.menu.MenuService;

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
        MenuService.showUser(this);
    }

    @Override
    public String[] getMenuItems() {
        return MenuConst.ADMIN_MENU_ITEMS;
    }

    @Override
    public void handleMenuChoice(String choice) {
        MenuService.handleAdminMenu(this, choice);
    }
    
}
