package gym.users;
import gym.users.interfaces.RoleBasedAccess;
import gym.utilities.Utils;

// This is the base class for all users in the gym management system.
// It's an abstract class, meaning it cannot be instantiated directly.
// It serves as a template for other user types (Admin, Trainer, Member).
// It implements the RoleMenu interface, which defines methods for managing user roles and menus.
public abstract class User implements RoleBasedAccess {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_TRAINER = "trainer";
    public static final String ROLE_MEMBER = "member";

    private final int id;
    private String username;
    private String password_hash;
    private String email;
    private String full_name;
    private String address;
    private String phone_number;
    private String role;

    // Constructor
    public User(int id, String username, String password_hash, String email, String full_name,  String address, String phone_number, String role) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.full_name = full_name;
        this.address = address;
        this.phone_number = phone_number;
        this.role = role;
    }

    // Constructor for new user (id is preset to 0)
    // This is used when creating a new user and the id is not known yet (it will be generated by the database)
    public User(String username, String password_hash, String email, String full_name, String address, String phone_number, String role) {
        this(0, username, password_hash, email, full_name, phone_number, address, role);
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return this.password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
   
    public abstract boolean canHaveMembership();

    public abstract boolean canTeachClass();

    @Override
    public String toString() {
        return "[UID:" + this.id + "/" + Utils.firstCharToUpperCase(this.role) + "]: " + 
        this.username + " (" + this.full_name + ", " + this.email + ")";
    }

    public String toStringNoId() {
        return Utils.firstCharToUpperCase(this.role) + ": " + this.username + " (" + this.full_name + ", " + this.email + ")";
    }

    // abstract methods from the interface, to be implemented by subclasses
    @Override
    public abstract void showUserMenu();
    @Override 
    public abstract void handleMenuChoice(String choice);
    @Override 
    public abstract String[] getMenuItems();

}
