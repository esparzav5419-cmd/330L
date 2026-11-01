/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: App is the entry point for the Week 4 demo. It displays the
 *          banner and welcome message, opens the SQLite database (filling
 *          it with sample employees the first time), and starts the menu.
 *          The program keeps running until the user chooses Quit.
 */
import java.io.File;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class App {

    private static final String DATABASE_FILE = "employees.db";

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.showBanner("PROJECT WEEK 4", "Database Interactions", "valentin esparza");
        ui.showWelcome();

        boolean firstRun = !new File(DATABASE_FILE).exists();
        try (EmployeeDatabase database = new EmployeeDatabase(DATABASE_FILE)) {
            if (firstRun) {
                addSampleEmployees(database);
                ui.showMessage("A new database was created with 5 sample employees.");
            } else {
                // Loading the employees also keeps automatic IDs from repeating
                // an ID that is already saved (see the Employee constructors).
                ui.showMessage("Loaded " + database.getAllEmployees().size()
                        + " employees from the database.");
            }
            new EmployeeMenu(ui, new InputReader(), database).run();
        } catch (SQLException e) {
            ui.showMessage("Could not open the database: " + e.getMessage());
        } catch (NoSuchElementException e) {
            ui.showMessage("\nInput ended. Goodbye!");
        }
    }

    /** Fills a brand-new database with realistic sample employees (CREATE). */
    private static void addSampleEmployees(EmployeeDatabase database) throws SQLException {
        // CONSTRUCTORS: different constructors are used, as in Week 3.
        database.addEmployee(new HourlyEmployee(1001, "Maria Santos",
                new Address("412 Granby St", "Norfolk", "VA", "23510"), 24.50, 45));
        database.addEmployee(new SalariedEmployee("James Whitaker",
                new Address("88 Atlantic Ave", "Virginia Beach", "VA", "23451"), 78000));
        database.addEmployee(new CommissionEmployee("Priya Patel",
                new Address("19 Colley Ave", "Norfolk", "VA", "23517"), 2500, 40000, 0.05));
        database.addEmployee(new HourlyEmployee("Daniel Brooks",
                new Address("Chesapeake", "VA"), 18.75));
        database.addEmployee(new CommissionEmployee("Aisha Rahman",
                new Address(), 2000));
    }
}
