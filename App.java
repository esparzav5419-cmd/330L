/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: App is the entry point for the Week 2 demo. It displays the
 *          banner and welcome message, creates sample employees, and runs
 *          a payroll that demonstrates the Payable INTERFACE and
 *          POLYMORPHISM.
 */
import java.util.ArrayList;
import java.util.List;

public class App {

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static void main(String[] args) {
        Consoleui ui = new Consoleui();
        Payrollprocessor payroll = new Payrollprocessor();

        ui.showBanner("PROJECT WEEK 2", "Software Design, Interfaces & Polymorphism",
                "valentin esparza");
        ui.showWelcome();

        // Employees are built with an Address (COMPOSITION) and are children
        // of Employee (INHERITANCE). The array type is the base class.
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

        // POLYMORPHISM + INTERFACE: every employee is stored as a Payable.
        // The list does not care which employee type each one is.
        List<Payable> payables = new ArrayList<>();
        for (Employee e : employees) {
            payables.add(e);
        }

        // POLYMORPHISM: the same call, calculatePay(), runs different code
        // depending on the actual object (Hourly, Salaried, or Commission).
        ui.showHeader("Payroll Report (polymorphism through the Payable interface)");
        ui.showPayroll(payables, payroll.totalPay(payables));
    }
}
