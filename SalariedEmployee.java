/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: SalariedEmployee is a DERIVED (child) class of Employee for
 *          employees paid a fixed annual salary (paid monthly here).
 */
public class SalariedEmployee extends Employee { // INHERITANCE

    private double annualSalary;

    public SalariedEmployee(int id, String name, Address address,
                            double annualSalary) {
        super(id, name, address);
        this.annualSalary = annualSalary;
    }

    @Override
    public String getType() { return "Salaried"; }

    @Override
    public double calculatePay() { return annualSalary / 12; }
}