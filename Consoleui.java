/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: ConsoleUI is a reusable helper class that handles all console
 *          output for the application (banner, welcome message, section
 *          headers, formatted employee table, and payroll report). It is
 *          designed to be reused in later weeks and in the final project.
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
        System.out.println("This week's demo runs a sample payroll for three employee");
        System.out.println("types (Hourly, Salaried, and Commission). It shows how one");
        System.out.println("interface lets the program treat every employee the same way.");
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

    /** Prints a payroll report for any list of Payable objects. */
    public void showPayroll(java.util.List<? extends Payable> items, double total) {
        System.out.printf("%-18s %-46s %-12s%n", "Name", "How Pay Was Figured", "Pay");
        printLine('-');
        for (Payable p : items) {
            System.out.printf("%-18s %-46s %-12s%n", p.getName(),
                    p.getPayDescription(), String.format("$%,.2f", p.calculatePay()));
        }
        printLine('-');
        System.out.printf("%-65s %-12s%n", "TOTAL PAYROLL", String.format("$%,.2f", total));
    }

    private void printLine(char c) {
        System.out.println(String.valueOf(c).repeat(WIDTH));
    }

    private String center(String text) {
        int pad = Math.max(0, (WIDTH - text.length()) / 2);
        return " ".repeat(pad) + text;
    }
}
