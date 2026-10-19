/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: SalariedEmployee is a DERIVED (child) class of Employee for
 *          employees paid a fixed annual salary (paid monthly here).
 */
public class SalariedEmployee extends Employee { // INHERITANCE

    private final double annualSalary;

    public SalariedEmployee(int id, String name, Address address,
                            double annualSalary) {
        super(id, name, address);
        this.annualSalary = annualSalary;
    }

    @Override
    public String getType() { return "Salaried"; }

    @Override
    public String getPayDescription() {
        return String.format("$%,.2f per year / 12 months", annualSalary);
    }

    @Override
    public double calculatePay() { return annualSalary / 12; }
}
