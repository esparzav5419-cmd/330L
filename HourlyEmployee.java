/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: HourlyEmployee is a DERIVED (child) class of Employee for
 *          employees paid by the hour, with time-and-a-half over 40 hours.
 */
public class HourlyEmployee extends Employee { // INHERITANCE: extends Employee

    private double hourlyRate;
    private double hoursWorked;

    public HourlyEmployee(int id, String name, Address address,
                          double hourlyRate, double hoursWorked) {
        super(id, name, address); // calls the base class constructor
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public String getType() { return "Hourly"; }

    @Override
    public double calculatePay() {
        double overtime = Math.max(0, hoursWorked - 40);
        double regular = hoursWorked - overtime;
        return regular * hourlyRate + overtime * hourlyRate * 1.5;
    }
}