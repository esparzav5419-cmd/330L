/*
 * Name: valentin esparza
 * Date: 10/31/2026
 * Purpose: EmployeeMenu runs the main menu and handles each choice. It
 *          connects the user (InputReader and ConsoleUI) to the database
 *          (EmployeeDatabase) and keeps running until the user quits.
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMenu {

    // ACCESS SPECIFIER: private. COMPOSITION: the menu "has" these helpers.
    private final ConsoleUI ui;
    private final InputReader input;
    private final EmployeeDatabase database;
    private final PayrollProcessor payroll = new PayrollProcessor();

    private static final String[] TYPES = { "Hourly", "Salaried", "Commission" };

    // CONSTRUCTOR: the menu is given the helpers it works with.
    public EmployeeMenu(ConsoleUI ui, InputReader input, EmployeeDatabase database) {
        this.ui = ui;
        this.input = input;
        this.database = database;
    }

    /** Shows the menu and handles choices until the user quits. */
    public void run() {
        String[] options = {
            "1. Add an employee", "2. Display all employees",
            "3. Display employees by type", "4. Find an employee by ID",
            "5. Update an employee", "6. Remove an employee",
            "7. Payroll report", "8. Quit"
        };
        boolean running = true;
        while (running) { // keeps going until the user chooses Quit
            ui.showMenu("MAIN MENU", options);
            int choice = input.readInt("Enter your choice (1-8): ", 1, 8);
            try {
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> displayAll();
                    case 3 -> displayByType();
                    case 4 -> findById();
                    case 5 -> updateEmployee();
                    case 6 -> removeEmployee();
                    case 7 -> showPayroll();
                    default -> running = !input.readYesNo("Are you sure you want to quit? (Y/N): ");
                }
            } catch (SQLException e) {
                ui.showMessage("Database error: " + e.getMessage());
            }
        }
        ui.showMessage("\nThank you for using the Employee Management System. Goodbye!");
    }

    // ------------------------- CREATE -------------------------
    private void addEmployee() throws SQLException {
        ui.showHeader("Add an Employee");
        int type = input.readInt("Type (1 Hourly, 2 Salaried, 3 Commission, 0 Cancel): ", 0, 3);
        if (type == 0) {
            return;
        }
        String name = input.readText("Name: ");
        Address address = new Address(
                input.readText("Street: "),
                input.readText("City: "),
                input.readMatching("State (2 letters): ", "[A-Za-z]{2}",
                        "Please enter a 2-letter state code.").toUpperCase(),
                input.readMatching("ZIP (5 digits): ", "\\d{5}",
                        "Please enter a 5-digit ZIP code."));

        // CONSTRUCTORS: the automatic-ID constructors are used, so the new
        // employee gets the next free ID.
        Employee employee;
        switch (type) {
            case 1 ->                 {
                    double rate = input.readDouble("Hourly rate: ", 0);
                    double hours = input.readDouble("Hours worked this period: ", 0);
                    employee = new HourlyEmployee(name, address, rate, hours);
                }
            case 2 -> employee = new SalariedEmployee(name, address,
                        input.readDouble("Annual salary: ", 0));
            default ->                 {
                    double base = input.readDouble("Base monthly pay: ", 0);
                    double sales = input.readDouble("Monthly sales: ", 0);
                    double rate = input.readDouble("Commission rate in percent (for example 5): ", 0) / 100;
                    employee = new CommissionEmployee(name, address, base, sales, rate);
                }
        }

        ui.showMessage("");
        ui.showEmployeeDetail(employee);
        if (input.readYesNo("Save this employee? (Y/N): ")) {
            database.addEmployee(employee);
            ui.showMessage("Employee " + employee.getId() + " (" + employee.getName() + ") was added.");
        } else {
            ui.showMessage("Employee was not saved.");
        }
    }

    // -------------------------- READ --------------------------
    private void displayAll() throws SQLException {
        ui.showHeader("All Employees");
        showList(database.getAllEmployees());
    }

    private void displayByType() throws SQLException {
        int type = input.readInt("Display which type? (1 Hourly, 2 Salaried, 3 Commission, 0 Cancel): ", 0, 3);
        if (type == 0) {
            return;
        }
        ui.showHeader(TYPES[type - 1] + " Employees");
        showList(database.getEmployeesByType(TYPES[type - 1]));
    }

    private void findById() throws SQLException {
        Employee employee = askForEmployee("Enter the ID of the employee to find (0 to cancel): ");
        if (employee != null) {
            ui.showHeader("Employee Details");
            ui.showEmployeeDetail(employee);
        }
    }

    private void showPayroll() throws SQLException {
        List<Employee> employees = database.getAllEmployees();
        if (employees.isEmpty()) {
            ui.showMessage("There are no employees to pay.");
            return;
        }
        // POLYMORPHISM: the list is treated as Payable objects.
        List<Payable> payables = new ArrayList<>(employees);
        ui.showHeader("Payroll Report");
        ui.showPayroll(payables, payroll.totalPay(payables));
    }

    // ------------------------- UPDATE -------------------------
    private void updateEmployee() throws SQLException {
        Employee employee = askForEmployee("Enter the ID of the employee to update (0 to cancel): ");
        if (employee == null) {
            return;
        }
        boolean done = false;
        while (!done) {
            ui.showHeader("Updating: " + employee.getName() + " (" + employee.getType() + ")");
            String[] fields = fieldsFor(employee);
            for (int i = 0; i < fields.length; i++) {
                ui.showMessage("   " + (i + 1) + ". " + fields[i]);
            }
            ui.showMessage("   0. Done");
            int field = input.readInt("Field to change: ", 0, fields.length);
            if (field == 0) {
                done = true;
            } else {
                applyChange(employee, fields[field - 1]);
                if (database.updateEmployee(employee)) {
                    ui.showMessage("Saved. New pay: " + String.format("$%,.2f", employee.calculatePay()));
                }
            }
        }
    }

    /** The fields the user can change: the shared ones, then the ones for this type. */
    private String[] fieldsFor(Employee employee) {
        String[] common = { "Name", "Street", "City", "State", "ZIP" };
        String[] extra;
        if (employee instanceof HourlyEmployee) {
            extra = new String[] { "Hourly rate", "Hours worked" };
        } else if (employee instanceof SalariedEmployee) {
            extra = new String[] { "Annual salary" };
        } else {
            extra = new String[] { "Base monthly pay", "Monthly sales", "Commission rate (percent)" };
        }
        String[] all = new String[common.length + extra.length];
        System.arraycopy(common, 0, all, 0, common.length);
        System.arraycopy(extra, 0, all, common.length, extra.length);
        return all;
    }

    private void applyChange(Employee employee, String field) {
        Address a = employee.getAddress();
        switch (field) {
            case "Name" -> employee.setName(input.readText("New name: "));
            case "Street" -> employee.setAddress(new Address(input.readText("New street: "),
                        a.getCity(), a.getState(), a.getZip()));
            case "City" -> employee.setAddress(new Address(a.getStreet(), input.readText("New city: "),
                        a.getState(), a.getZip()));
            case "State" -> employee.setAddress(new Address(a.getStreet(), a.getCity(),
                        input.readMatching("New state (2 letters): ", "[A-Za-z]{2}",
                                "Please enter a 2-letter state code.").toUpperCase(), a.getZip()));
            case "ZIP" -> employee.setAddress(new Address(a.getStreet(), a.getCity(), a.getState(),
                        input.readMatching("New ZIP (5 digits): ", "\\d{5}",
                                "Please enter a 5-digit ZIP code.")));
            case "Hourly rate" -> ((HourlyEmployee) employee).setHourlyRate(input.readDouble("New hourly rate: ", 0));
            case "Hours worked" -> ((HourlyEmployee) employee).setHoursWorked(input.readDouble("New hours worked: ", 0));
            case "Annual salary" -> ((SalariedEmployee) employee).setAnnualSalary(input.readDouble("New annual salary: ", 0));
            case "Base monthly pay" -> ((CommissionEmployee) employee).setBaseMonthlyPay(input.readDouble("New base monthly pay: ", 0));
            case "Monthly sales" -> ((CommissionEmployee) employee).setMonthlySales(input.readDouble("New monthly sales: ", 0));
            default -> ((CommissionEmployee) employee).setCommissionRate(
                        input.readDouble("New commission rate in percent: ", 0) / 100);
        }
    }

    // ------------------------- DELETE -------------------------
    private void removeEmployee() throws SQLException {
        Employee employee = askForEmployee("Enter the ID of the employee to remove (0 to cancel): ");
        if (employee == null) {
            return;
        }
        ui.showMessage("");
        ui.showEmployeeDetail(employee);
        if (input.readYesNo("Are you sure you want to remove this employee? (Y/N): ")) {
            if (database.deleteEmployee(employee.getId())) {
                ui.showMessage("Employee " + employee.getId() + " (" + employee.getName() + ") was removed.");
            }
        } else {
            ui.showMessage("Nothing was removed.");
        }
    }

    // ------------------------ helpers ------------------------
    // ACCESS SPECIFIER: private. These are only used inside the menu.

    /** Asks for an ID and returns that employee, or null if cancelled or not found. */
    private Employee askForEmployee(String prompt) throws SQLException {
        int id = input.readInt(prompt, 0, Integer.MAX_VALUE);
        if (id == 0) {
            return null;
        }
        Employee employee = database.getEmployeeById(id);
        if (employee == null) {
            ui.showMessage("No employee with ID " + id + " was found.");
        }
        return employee;
    }

    private void showList(List<Employee> employees) {
        if (employees.isEmpty()) {
            ui.showMessage("No employees found.");
        } else {
            ui.showEmployeeTable(employees);
        }
    }
}