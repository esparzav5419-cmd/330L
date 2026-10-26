/*
 * Name: valentin esparza
 * Date: 10/25/26
 * Purpose: SalariedEmployee is a child of the abstract Employee class for
 *          employees paid a fixed annual salary (paid monthly). It
 *          implements the abstract methods from Employee and has two
 *          CONSTRUCTORS.
 */
public class SalariedEmployee extends Employee { // INHERITANCE

    // ACCESS SPECIFIER: private. Only this class uses these values.
    private static final int MONTHS_PER_YEAR = 12;
    private final double annualSalary;

    // CONSTRUCTOR 1: full constructor with an explicit ID.
    public SalariedEmployee(int id, String name, Address address,
                            double annualSalary) {
        super(id, name, address);
        this.annualSalary = annualSalary;
    }

    // CONSTRUCTOR 2: automatic ID.
    public SalariedEmployee(String name, Address address, double annualSalary) {
        super(name, address);
        this.annualSalary = annualSalary;
    }

    // ABSTRACTION: implements the abstract methods required by Employee.
    @Override
    public String getType() { return "Salaried"; }

    @Override
    public double calculatePay() { return annualSalary / MONTHS_PER_YEAR; }

    @Override
    public String getPayDescription() {
        return formatMoney(annualSalary) + " per year / " + MONTHS_PER_YEAR + " months";
    }
}
