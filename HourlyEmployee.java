/*
 * Name: valentin esparza
 * Date: 10/25/26
 * Purpose: HourlyEmployee is a child of the abstract Employee class for
 *          employees paid by the hour, with time-and-a-half over 40
 *          hours. It implements the abstract methods from Employee and
 *          has three CONSTRUCTORS.
 */
public class HourlyEmployee extends Employee { // INHERITANCE

    // ACCESS SPECIFIER: private constants and data. Only this class uses them.
    private static final double OVERTIME_THRESHOLD = 40;
    private static final double OVERTIME_MULTIPLIER = 1.5;
    private final double hourlyRate;
    private final double hoursWorked;

    // CONSTRUCTOR 1: full constructor with an explicit ID.
    public HourlyEmployee(int id, String name, Address address,
                          double hourlyRate, double hoursWorked) {
        super(id, name, address); // calls the base class constructor
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    // CONSTRUCTOR 2: automatic ID.
    public HourlyEmployee(String name, Address address,
                          double hourlyRate, double hoursWorked) {
        super(name, address);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    // CONSTRUCTOR 3: automatic ID and a standard 40-hour period.
    public HourlyEmployee(String name, Address address, double hourlyRate) {
        this(name, address, hourlyRate, OVERTIME_THRESHOLD);
    }

    // ACCESS SPECIFIER: private helper. Only this class needs it, so it
    // is not exposed.
    private double getOvertimeHours() {
        return Math.max(0, hoursWorked - OVERTIME_THRESHOLD);
    }

    // ABSTRACTION: implements the abstract methods required by Employee.
    @Override
    public String getType() { return "Hourly"; }

    @Override
    public double calculatePay() {
        double overtime = getOvertimeHours();
        double regular = hoursWorked - overtime;
        return regular * hourlyRate + overtime * hourlyRate * OVERTIME_MULTIPLIER;
    }

    @Override
    public String getPayDescription() {
        return String.format("%.1f hrs @ %s/hr (%.1f overtime at %.1fx)",
                hoursWorked, formatMoney(hourlyRate), getOvertimeHours(),
                OVERTIME_MULTIPLIER);
    }
}
