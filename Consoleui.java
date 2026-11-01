/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: ConsoleUI is a reusable helper class that handles all console
 *          output for the application (banner, welcome message, section
 *          headers, menus, formatted employee table, employee details, and
 *          payroll report). It is
 *          designed to be reused in later weeks and in the final project.
 */
public class ConsoleUI {

    // ACCESS SPECIFIER: private. The display width is an internal detail.
    private final int width;

    // CONSTRUCTOR 1 (default): standard 78-character console width.
    public ConsoleUI() {
        this(78);
    }

    // CONSTRUCTOR 2: lets the caller choose the display width.
    public ConsoleUI(int width) {
        this.width = width;
    }

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
        System.out.println("Use the menu to add, display, update, and remove employees.");
        System.out.println("Type the number of a choice and press Enter. Your changes are");
        System.out.println("saved in an SQLite database (employees.db), so they are still");
        System.out.println("there the next time you run the program.");
        System.out.println();
    }

    /** Prints one line of text. */
    public void showMessage(String text) {
        System.out.println(text);
    }

    /** Prints a section header. */
    public void showHeader(String text) {
        System.out.println();
        System.out.println("--- " + text + " ---");
    }

    /** Prints a numbered menu with a title. */
    public void showMenu(String title, String[] options) {
        System.out.println();
        System.out.println(center(title));
        System.out.println("  " + "-".repeat(45));
        for (String option : options) {
            System.out.println("   " + option);
        }
        System.out.println("  " + "-".repeat(45));
    }

    /** Prints every detail of one employee. */
    public void showEmployeeDetail(Employee e) {
        System.out.println(e);
        System.out.println("Pay Basis: " + e.getPayDescription());
    }

    /** Prints a formatted table of employees. */
    public void showEmployeeTable(java.util.List<Employee> employees) {
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
        System.out.println(employees.size() + " employee(s) displayed.");
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
        System.out.println(String.valueOf(c).repeat(width));
    }

    private String center(String text) {
        int pad = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(pad) + text;
    }
}
