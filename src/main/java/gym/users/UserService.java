package gym.users;

import java.sql.Connection;

public class UserService {
    
    public static User login(String username, String password,  Connection conn) {
        System.out.print("Login Logic here");
        return null; // Placeholder return value
    }

    public static void register(User user, Connection conn) {
        System.out.print("Registration Logic here");
    }

}
