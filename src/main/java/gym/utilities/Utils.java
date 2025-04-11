package gym.utilities;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {
    // This method takes a string and a length, and returns the string centered within the specified length.
    // If the string is longer than the specified length, it truncates the string to fit.
    public static String center_string(String str, int length) {
        if (str.length() > length) {
            str = str.substring(0, length);
        }
        int padding = (length - str.length()) / 2;
        String padded_string = " ".repeat(padding) + str + " ".repeat(length - str.length() - padding);
        return padded_string;
    }

    public static String symbol_line(char symbol, int length) {
        return Character.toString(symbol).repeat(length);
    }

    public static String add_offset_to_string(String str, int offset) {
        return symbol_line(' ', offset) + str;
    }

    public static String print_role(String role, char symbol, int length) {
        int left_offset = 5;
        String padded_string = symbol_line(symbol, left_offset) +
            role + symbol_line(symbol, length - left_offset - role.length());
        return padded_string;
    }

    public static void print_title_message(String message1, String message2, int message_length, char symbol, int offset, String role) {
        System.out.println();
        System.out.println(add_offset_to_string(print_role(role, symbol, message_length), offset));
        System.out.println(add_offset_to_string(symbol + center_string(message1, message_length - 2) + symbol, offset));
        if (!message1.equals(message2)) {
            System.out.println(add_offset_to_string(symbol + center_string(message2, message_length - 2) + symbol, offset));
        }
        System.out.println(add_offset_to_string(symbol_line(symbol, message_length), offset));
        System.out.println();
    }

    public static void print_title_message(String message, int message_length, char symbol, int offset, String role) {
        print_title_message(message, message, message_length, symbol, offset, role);
    }

    public static String FirstCharToUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    // This method parses a CSV line and returns an array of strings.
    public static String[] parse_csv(String line) {
        List<String> csv_line_parts = new ArrayList<>();
        boolean in_quotes = false;
        String current_field = ""; 
        
        for (int i = 0; i < line.length(); i++) { 
            char c = line.charAt(i);
            
            if (c == '"') {
                in_quotes = !in_quotes;
            } 
            else if (c == ',' && !in_quotes) {
                csv_line_parts.add(current_field.trim());
                current_field = ""; 
            } 
            else {
                current_field += c; 
            }
        }

        csv_line_parts.add(current_field.trim());
        return csv_line_parts.toArray(new String[0]);
    }

    public static String hash_password(String plain_password) {
        return BCrypt.hashpw(plain_password, BCrypt.gensalt());
    }
    
    public static boolean check_password(String plain_password, String hashed_password) {
        return BCrypt.checkpw(plain_password, hashed_password);
    }

    public static boolean password_hashes_equal(String hashed_password_1, String hashed_password_2) {
        return hashed_password_1.equals(hashed_password_2);
    }

    public static boolean passwords_equal(String password_1, String password_2) {
        return password_1.equals(password_2);
    }

    public static int validate_id(String input) {
        if (input == null || input.trim().isEmpty()) {
            return -1;
        }
        try {
            int value = Integer.parseInt(input.trim());
            return value >= 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
