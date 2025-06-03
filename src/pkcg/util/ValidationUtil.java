package pkcg.util;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for input validation and user interaction.
 * 
 * Provides methods for validating user inputs like emails, passwords, and numeric values,
 * as well as methods for reading validated input from the user with appropriate prompts
 * and error messages.
 */
public class ValidationUtil {
    // Regular expression for email validation (RFC 5322 compliant)
    private static final String EMAIL_REGEX = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    // Pattern object for email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    // Regex pattern for time in format HH:MM (24-hour format)
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    
    /**
     * Validates email format according to RFC 5322 standard.
     * 
     * @param email The email address to validate
     * @return true if email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    
    /**
     * Validates password complexity according to security requirements:
     * - At least 4 letters
     * - At least 3 numbers
     * - At least 1 special character
     * - Maximum 20 characters total
     * 
     * @param password The password to validate
     * @return true if password meets all requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        // Check if password is null or exceeds maximum length
        if (password == null || password.length() > 20) {
            return false;
        }
        
        // Count letters, numbers and special characters
        int letterCount = 0;
        int digitCount = 0;
        int specialCount = 0;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                specialCount++;
            }
        }
        
        // Check if password meets all requirements
        return (letterCount >= 4 && digitCount >= 3 && specialCount >= 1);
    }
    
    /**
     * Reads an integer from user input with validation for range bounds.
     * Provides option to return to previous menu.
     * 
     * @param scanner Scanner to read input
     * @param prompt Message to display for input
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @param allowBack Whether to allow going back (by entering 0)
     * @return Valid integer input, or -1 if user chose to go back
     */
    public static int readValidInt(Scanner scanner, String prompt, int min, int max, boolean allowBack) {
        while (true) {
            System.out.print(prompt + (allowBack ? " (0 to go back): " : ": "));
            
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                
                // Check if user wants to go back
                if (allowBack && "0".equals(input)) {
                    return -1;
                }
                
                try {
                    int value = Integer.parseInt(input);
                    if (value >= min && value <= max) {
                        return value;
                    } else {
                        System.out.println("Please enter a number between " + min + " and " + max);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } else {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
    
    /**
     * Reads a double from user input with validation for minimum value.
     * Provides option to return to previous menu.
     * 
     * @param scanner Scanner to read input
     * @param prompt Message to display for input
     * @param min Minimum allowed value (inclusive)
     * @param allowBack Whether to allow going back (by entering 0)
     * @return Valid double input, or -1 if user chose to go back
     */
    public static double readValidDouble(Scanner scanner, String prompt, double min, boolean allowBack) {
        while (true) {
            System.out.print(prompt + (allowBack ? " (0 to go back): " : ": "));
            
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                
                // Check if user wants to go back
                if (allowBack && "0".equals(input)) {
                    return -1;
                }
                
                try {
                    double value = Double.parseDouble(input);
                    if (value >= min) {
                        return value;
                    } else {
                        System.out.println("Please enter a number greater than or equal to " + min);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } else {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
    
    /**
     * Reads a string from user input with validation for minimum length.
     * Provides option to return to previous menu.
     * 
     * @param scanner Scanner to read input
     * @param prompt Message to display for input
     * @param minLength Minimum allowed length
     * @param allowEmpty Whether to allow empty input
     * @param allowBack Whether to allow going back (by entering "back")
     * @return Valid string input, or null if user chose to go back
     */
    public static String readValidString(Scanner scanner, String prompt, int minLength, boolean allowEmpty, boolean allowBack) {
        while (true) {
            System.out.print(prompt + (allowBack ? " (enter 'back' to return): " : ": "));
            
            String input = scanner.nextLine().trim();
            
            // Check if user wants to go back
            if (allowBack && "back".equalsIgnoreCase(input)) {
                return null;
            }
            
            // Check if input meets criteria
            if ((allowEmpty && input.isEmpty()) || input.length() >= minLength) {
                return input;
            } else {
                System.out.println("Input must be at least " + minLength + " characters long.");
            }
        }
    }
    
    /**
     * Reads a time in HH:MM format with validation.
     * Provides option to return to previous menu.
     * 
     * @param scanner Scanner to read input
     * @param prompt Message to display for input
     * @param allowBack Whether to allow going back
     * @return Valid time string in HH:MM format, or null if user chose to go back
     */
    public static String readValidTime(Scanner scanner, String prompt, boolean allowBack) {
        while (true) {
            System.out.print(prompt + (allowBack ? " (HH:MM, enter 'back' to return): " : " (HH:MM): "));
            
            String input = scanner.nextLine().trim();
            
            // Check if user wants to go back
            if (allowBack && "back".equalsIgnoreCase(input)) {
                return null;
            }
            
            // Check if input matches the time pattern
            if (TIME_PATTERN.matcher(input).matches()) {
                try {
                    // Further validate by parsing with LocalTime
                    LocalTime.parse(input.length() == 5 ? input : "0" + input);
                    return input;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please use HH:MM (24-hour format).");
                }
            } else {
                System.out.println("Invalid time format. Please use HH:MM (24-hour format).");
            }
        }
    }
    
    /**
     * Reads a yes/no response from the user.
     * 
     * @param scanner Scanner to read input
     * @param prompt Message to display for input
     * @return true for yes (Y), false for no (N)
     */
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("Please enter Y for Yes or N for No.");
            }
        }
    }
}