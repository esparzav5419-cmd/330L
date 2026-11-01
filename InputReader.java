/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: InputReader gets values from the user and checks them. Every
 *          method keeps asking until the user gives a valid answer, so
 *          bad input never crashes the program. It is a reusable helper
 *          class like ConsoleUI.
 */
import java.util.Scanner;

public class InputReader {

    // ACCESS SPECIFIER: private. Only this class reads from the keyboard.
    private final Scanner scanner = new Scanner(System.in);

    /** Asks for text and repeats until something is entered. */
    public String readText(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Please enter a value.");
        }
    }

    /** Asks for text that must match a pattern (such as a 5-digit ZIP). */
    public String readMatching(String prompt, String regex, String errorMessage) {
        while (true) {
            String value = readLine(prompt);
            if (value.matches(regex)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    /** Asks for a whole number between min and max (inclusive). */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String value = readLine(prompt);
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Invalid choice. Please enter a number from "
                    + min + " to " + max + ".");
        }
    }

    /** Asks for a number that is at least min (for pay, hours, and so on). */
    public double readDouble(String prompt, double min) {
        while (true) {
            String value = readLine(prompt);
            try {
                double number = Double.parseDouble(value);
                if (number >= min) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a number that is at least " + min + ".");
        }
    }

    /** Asks a yes/no question. */
    public boolean readYesNo(String prompt) {
        while (true) {
            String value = readLine(prompt).toLowerCase();
            if (value.equals("y") || value.equals("yes")) {
                return true;
            }
            if (value.equals("n") || value.equals("no")) {
                return false;
            }
            System.out.println("Please enter Y or N.");
        }
    }

    // ACCESS SPECIFIER: private. The other methods use this one to prompt
    // and read a line, so it does not need to be visible outside the class.
    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // throws NoSuchElementException if input ends
    }
}