package gym.menu;
import java.util.Scanner;
import java.util.regex.Pattern;

import gym.utilities.Utils;

public class InputService {

    // Regex patterns for various inputs
    public static final String REGEX_USERNAME = "^[a-zA-Z][a-zA-Z0-9_-]*$";;
    public static final String REGEX_PASSWORD = 
    "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!*()_\\-])(?=\\S+$).{8,}$";
    public static final String REGEX_SIMPLE_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
    public static final String REGEX_FULL_NAME = 
    "^[\\p{L} .'-]+$";
    public static final String REGEX_SIMPLE_ADDRESS = 
    "^[\\p{L}0-9\\-\\.,#'/ ]+$";
    public static final String REGEX_EMAIL = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
    public static final String REGEX_NON_EMPTY = "^.+$";
    public static final String REGEX_PHONE_10_DIGIT = "^\\d{10}$";
    public static final String REGEX_MMYY_DATE = "^(0[1-9]|1[0-2])/\\d{2}$";
    public static final String REGEX_CREDIT_CARD = "^\\d{16}$";
   
    /**
     * Gets validated input with regex checking and cancellation support
     * @param scanner Scanner for input
     * @param regexPattern Validation pattern
     * @param prompt Display prompt
     * @param errorPrompt Error message
     * @param enterAllowed If true, empty input returns "-1"
     * @return Valid input, null if cancelled, "-1" if empty input allowed
     */
    public static String getValidatedInput(Scanner scanner,
                                         String regexPattern,
                                         String prompt,
                                         String errorPrompt,
                                         boolean enterAllowed) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            // Handle empty input
            if (input.isEmpty()) {
                if (enterAllowed) {
                    return "-1";
                }
                System.out.println(errorPrompt);
                continue;
            }
            
            // Handle cancellation
            if (input.equals("-1")) {
                return null;
            }
            
            // Validate pattern
            if (Pattern.matches(regexPattern, input)) {
                return input;
            }
            
            System.out.println(errorPrompt);
        }
    }

    
    public static String enterCreditCardInfo(Scanner scanner) {

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);

        System.out.println(sStr + "Enter credit card details (-1 at any prompt to cancel):");
        
        String cardNumber = promptCardNumber(scanner);
        if (cardNumber == null) return null;
        
        String expiration = promptExpiration(scanner);
        if (expiration == null) return null;
        
        String cvv = promptCVV(scanner);
        if (cvv == null) return null;
        
        return "Card " + cardNumber + "Exp. " + expiration + ", CVV " + cvv;
    }
    
    private static String promptCardNumber(Scanner scanner) {

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);        

        while (true) {
            System.out.print(sStr + "Enter 16-digit card number: ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("-1")) return null;
            
            String digitsOnly = input.replaceAll("[^0-9]", "");
            
            if (digitsOnly.length() != 16) {
                System.out.println(sStr + "<!> Card number must be 16 digits");
                continue;
            }
            
            return digitsOnly.replaceAll("(.{4})(?!$)", "$1-");
        }
    }
    
    private static String promptExpiration(Scanner scanner) {

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);        

        while (true) {

            System.out.print(sStr + "Enter expiration (MM/YY): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("-1")) return null;
            
            if (!Pattern.matches(REGEX_MMYY_DATE, input)) {
                System.out.println(sStr + "<!> Format must be MM/YY");
                continue;
            }
            
            return input;
        }
    }
    
    private static String promptCVV(Scanner scanner) {

        String sStr = Utils.symbol_line(' ', MenuConst.OFFSET_MENU_TITLE);        

        while (true) {
            System.out.print(sStr + "Enter CVV (3 digits): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("-1")) return null;
            
            if (!Pattern.matches("\\d{3}", input)) {
                System.out.println(sStr + "<!> CVV must be 3 digits");
                continue;
            }
            
            return input;
        }
    }
 
}