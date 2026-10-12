/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: ConsoleUI is a reusable helper class that handles all console
 *          output for the application (banner, welcome message, section
 *          headers, and formatted employee table). It is designed to be
 *          reused in later weeks and in the final project.
 */
public class Consoleui {

    private static final int WIDTH = 78;

    /** Prints the project week indicator, assignment title, and author. */
    public void showBanner(String week, String title, String author) {
        printLine('=');
        System.out.println(center(week));
        System.out.println(center(title));
        System.out.println(center("By: " + author));
        printLine('=');
    }

    /** Prints a welcome message with basic instructions. */
    public void showWelcome() {
        System.out.println();
        System.out.println("Welcome to the Employee Management System!");
        System.out.println("This week's demo shows three employee types (Hourly,");
        System.out.println("Salaried, and Commission) stored in sample data.");
        System.out.println("The full menu (add, remove, update, display, quit)");
        System.out.println("will be built out in later weeks.");
        System.out.println();
    }

    /** Prints a section header. */
    public void showHeader(String text) {
        System.out.println();
        System.out.println("--- " + text + " ---");
    }

    /** Prints a formatted table of employees. */
    public void showEmployeeTable(Employee[] employees) {
        System.out.printf("%-4s %-18s %-11s %-12s %-22s%n",
                "ID", "Name", "Type", "Pay", "Location");
        printLine('-');
        for (Employee e : employees) {
            System.out.printf("%-4d %-18s %-11s %-12s %-22s%n",
                    e.getId(), e.getName(), e.getType(),
                    String.format("$%,.2f", e.calculatePay()),
                    e.getAddress().getCityState());
        }
        printLine('-');
    }

    private void printLine(char c) {
        System.out.println(String.valueOf(c).repeat(WIDTH));
    }

    private String center(String text) {
        int pad = Math.max(0, (WIDTH - text.length()) / 2);
        return " ".repeat(pad) + text;
    }
}