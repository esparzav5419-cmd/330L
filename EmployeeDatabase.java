/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: EmployeeDatabase is the only class that talks to the SQLite
 *          database. It creates the tables and provides the four CRUD
 *          operations: Create (addEmployee), Read (getAllEmployees,
 *          getEmployeesByType, getEmployeeById), Update (updateEmployee),
 *          and Delete (deleteEmployee). Employee objects go in and come
 *          out, so the rest of the program never sees any SQL.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase implements AutoCloseable {

    // ACCESS SPECIFIER: private. The connection is an internal detail.
    // COMPOSITION: EmployeeDatabase "has a" database Connection.
    private final Connection connection;

    // Every read starts from this query, which joins employees to their address.
    private static final String SELECT_EMPLOYEES =
            "SELECT e.employee_id, e.employee_type, e.name, e.hourly_rate, "
            + "e.hours_worked, e.annual_salary, e.base_monthly_pay, "
            + "e.monthly_sales, e.commission_rate, "
            + "a.street, a.city, a.state, a.zip "
            + "FROM employees e JOIN addresses a ON e.address_id = a.address_id ";

    // CONSTRUCTOR: opens (or creates) the database file and the tables.
    public EmployeeDatabase(String fileName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        createTables();
    }

    // ACCESS SPECIFIER: private. Only the constructor calls it.
    private void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS addresses ("
                    + "address_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "street TEXT NOT NULL, city TEXT NOT NULL, "
                    + "state TEXT NOT NULL, zip TEXT NOT NULL)");
            statement.execute("CREATE TABLE IF NOT EXISTS employees ("
                    + "employee_id INTEGER PRIMARY KEY, "
                    + "employee_type TEXT NOT NULL "
                    + "CHECK (employee_type IN ('Hourly', 'Salaried', 'Commission')), "
                    + "name TEXT NOT NULL, "
                    + "address_id INTEGER NOT NULL REFERENCES addresses(address_id), "
                    + "hourly_rate REAL, hours_worked REAL, annual_salary REAL, "
                    + "base_monthly_pay REAL, monthly_sales REAL, commission_rate REAL)");
        }
    }

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------
    /** Saves a new employee and their address. Both rows save or neither does. */
    public void addEmployee(Employee employee) throws SQLException {
        connection.setAutoCommit(false);
        try {
            int addressId;
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO addresses (street, city, state, zip) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                bindAddress(ps, employee.getAddress());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    addressId = keys.getInt(1);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO employees (employee_id, employee_type, name, address_id, "
                    + "hourly_rate, hours_worked, annual_salary, base_monthly_pay, "
                    + "monthly_sales, commission_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, employee.getId());
                ps.setString(2, employee.getType());
                ps.setString(3, employee.getName());
                ps.setInt(4, addressId);
                bindPayColumns(ps, employee, 5);
                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // ---------------------------------------------------------------
    // READ
    // ---------------------------------------------------------------
    /** Returns every employee, ordered by ID. */
    public List<Employee> getAllEmployees() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                SELECT_EMPLOYEES + "ORDER BY e.employee_id")) {
            return readEmployees(ps);
        }
    }

    /** Returns only the employees of one type ("Hourly", "Salaried", or "Commission"). */
    public List<Employee> getEmployeesByType(String type) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                SELECT_EMPLOYEES + "WHERE e.employee_type = ? ORDER BY e.employee_id")) {
            ps.setString(1, type);
            return readEmployees(ps);
        }
    }

    /** Returns one employee, or null if no employee has that ID. */
    public Employee getEmployeeById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                SELECT_EMPLOYEES + "WHERE e.employee_id = ?")) {
            ps.setInt(1, id);
            List<Employee> found = readEmployees(ps);
            return found.isEmpty() ? null : found.get(0);
        }
    }

    // ---------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------
    /** Saves the current values of an existing employee. Returns false if the ID is not found. */
    public boolean updateEmployee(Employee employee) throws SQLException {
        connection.setAutoCommit(false);
        try {
            int rows;
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE employees SET name = ?, hourly_rate = ?, hours_worked = ?, "
                    + "annual_salary = ?, base_monthly_pay = ?, monthly_sales = ?, "
                    + "commission_rate = ? WHERE employee_id = ?")) {
                ps.setString(1, employee.getName());
                bindPayColumns(ps, employee, 2);
                ps.setInt(8, employee.getId());
                rows = ps.executeUpdate();
            }
            if (rows > 0) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "UPDATE addresses SET street = ?, city = ?, state = ?, zip = ? "
                        + "WHERE address_id = (SELECT address_id FROM employees "
                        + "WHERE employee_id = ?)")) {
                    bindAddress(ps, employee.getAddress());
                    ps.setInt(5, employee.getId());
                    ps.executeUpdate();
                }
            }
            connection.commit();
            return rows > 0;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------
    /** Removes an employee and their address. Returns false if the ID is not found. */
    public boolean deleteEmployee(int id) throws SQLException {
        connection.setAutoCommit(false);
        try {
            int addressId = -1;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT address_id FROM employees WHERE employee_id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        addressId = rs.getInt(1);
                    }
                }
            }
            if (addressId < 0) {
                connection.rollback();
                return false;
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM employees WHERE employee_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM addresses WHERE address_id = ?")) {
                ps.setInt(1, addressId);
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    // ---------------------------------------------------------------
    // Private helpers (ACCESS SPECIFIER: private, used only in this class)
    // ---------------------------------------------------------------

    /** Runs a query and turns each row into the right kind of Employee. */
    private List<Employee> readEmployees(PreparedStatement ps) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Address address = new Address(rs.getString("street"), rs.getString("city"),
                        rs.getString("state"), rs.getString("zip"));
                int id = rs.getInt("employee_id");
                String name = rs.getString("name");
                // CONSTRUCTORS + POLYMORPHISM: each row becomes an object of the
                // right child class, but the rest of the program just sees Employee.
                switch (rs.getString("employee_type")) {
                    case "Hourly" -> employees.add(new HourlyEmployee(id, name, address,
                                rs.getDouble("hourly_rate"), rs.getDouble("hours_worked")));
                    case "Salaried" -> employees.add(new SalariedEmployee(id, name, address,
                                rs.getDouble("annual_salary")));
                    default -> employees.add(new CommissionEmployee(id, name, address,
                                rs.getDouble("base_monthly_pay"), rs.getDouble("monthly_sales"),
                                rs.getDouble("commission_rate")));
                }
            }
        }
        return employees;
    }

    private void bindAddress(PreparedStatement ps, Address address) throws SQLException {
        ps.setString(1, address.getStreet());
        ps.setString(2, address.getCity());
        ps.setString(3, address.getState());
        ps.setString(4, address.getZip());
    }

    /**
     * Sets the six pay columns, starting at the given parameter number.
     * Columns that do not apply to this employee type are stored as NULL.
     */
    private void bindPayColumns(PreparedStatement ps, Employee employee, int start)
            throws SQLException {
        Double[] values = new Double[6]; // hourly rate, hours, salary, base, sales, rate
        switch (employee) {
            case HourlyEmployee h -> {
                values[0] = h.getHourlyRate();
                values[1] = h.getHoursWorked();
            }
            case SalariedEmployee salariedEmployee -> values[2] = salariedEmployee.getAnnualSalary();
            case CommissionEmployee c -> {
                values[3] = c.getBaseMonthlyPay();
                values[4] = c.getMonthlySales();
                values[5] = c.getCommissionRate();
            }
            default -> {
            }
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                ps.setNull(start + i, Types.REAL);
            } else {
                ps.setDouble(start + i, values[i]);
            }
        }
    }
}