package gym.utilities;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // This method takes a string and a length, and returns the string centered within the specified length.
    // If the string is longer than the specified length, it truncates the string to fit.
    public static String center_string(String str, int length) {
        if (str.length() > length) {
            str = str.substring(0, length);
        }
        int padding = (length - str.length()) / 2;
        String paddedString = " ".repeat(padding) + str + " ".repeat(length - str.length() - padding);
        return paddedString;
    }

    public static String symbol_line(char symbol, int length) {
        return Character.toString(symbol).repeat(length);
    }

    public static void print_title_message(String message, int line_length, char symbol) {
        System.out.println("\n" + symbol_line(symbol, line_length));
        System.out.println(symbol + center_string(message, line_length - 2) + symbol);
        System.out.println(symbol_line(symbol, line_length) + "\n");
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
}
