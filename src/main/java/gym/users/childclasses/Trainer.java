package gym.users.childclasses;

import gym.users.User;
import gym.menu.MenuConst;
import gym.menu.MenuService;

public class Trainer extends User{
    // Constructor for Trainer class
    public Trainer(int id, String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(id, username, password_hash, email, full_name,  address, phone_number, User.ROLE_TRAINER);
    }

    // Constructor for new Trainer (id is preset to 0)
    public Trainer(String username, String password_hash, String email, String full_name, String address, String phone_number) {
        super(username, password_hash, email, full_name, address, phone_number, User.ROLE_TRAINER);
    }

    public boolean canHaveMembership() {
        return true; 
    }

    public boolean canTeachClass() {
        return true; 
    }

    // Implementing the abstract method from RoleBasedAccess interface
    @Override
    public void showUserMenu() {
        MenuService.showUser(this);
    }

    @Override
    public String[] getMenuItems() {
        return MenuConst.TRAINER_MENU_ITEMS;
    }

    @Override
    public void handleMenuChoice(String choice) {
        MenuService.handleTrainerMenu(this, choice);
    }
}
