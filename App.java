/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: Main is the entry point for the Week 1 demo. It displays the
 *          banner and welcome message, creates sample employees
 *          (inheritance + composition), and displays their information.
 */
public class App {

    public static void main(String[] args) {
        Consoleui ui = new Consoleui();

        ui.showBanner("PROJECT WEEK 1", "Inheritance, Composition, and User Interactions",
                "valentin esparza");
        ui.showWelcome();

        // Instantiate classes with realistic data.
        // Each employee is built with an Address (COMPOSITION) and is a
        // child of Employee (INHERITANCE).
        Employee[] employees = {
            new HourlyEmployee(1001, "Maria Santos",
                    new Address("412 Granby St", "Norfolk", "VA", "23510"), 24.50, 45),
            new SalariedEmployee(1002, "James Whitaker",
                    new Address("88 Atlantic Ave", "Virginia Beach", "VA", "23451"), 78000),
            new CommissionEmployee(1003, "Priya Patel",
                    new Address("19 Colley Ave", "Norfolk", "VA", "23517"), 2500, 40000, 0.05)
        };

        ui.showHeader("Employee Summary");
        ui.showEmployeeTable(employees);

        ui.showHeader("Employee Details");
        for (Employee e : employees) {
            System.out.println(e);
            System.out.println();
        }
    }
}