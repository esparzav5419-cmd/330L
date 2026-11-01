/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: CommissionEmployee is a child of the abstract Employee class
 *          for employees paid a monthly base plus a percentage of sales.
 *          It implements the abstract methods from Employee and has three
 *          CONSTRUCTORS.
 */
public class CommissionEmployee extends Employee { // INHERITANCE

    // ACCESS SPECIFIER: private. Only this class uses these values.
    private static final double DEFAULT_COMMISSION_RATE = 0.05;
    private double baseMonthlyPay; // not final: the update feature changes these
    private double monthlySales;
    private double commissionRate;

    // CONSTRUCTOR 1: full constructor with an explicit ID.
    public CommissionEmployee(int id, String name, Address address,
                              double baseMonthlyPay, double monthlySales,
                              double commissionRate) {
        super(id, name, address);
        this.baseMonthlyPay = baseMonthlyPay;
        this.monthlySales = monthlySales;
        this.commissionRate = commissionRate;
    }

    // CONSTRUCTOR 2: automatic ID.
    public CommissionEmployee(String name, Address address,
                              double baseMonthlyPay, double monthlySales,
                              double commissionRate) {
        super(name, address);
        this.baseMonthlyPay = baseMonthlyPay;
        this.monthlySales = monthlySales;
        this.commissionRate = commissionRate;
    }

    // CONSTRUCTOR 3: new hire with no sales yet and the standard rate.
    public CommissionEmployee(String name, Address address, double baseMonthlyPay) {
        this(name, address, baseMonthlyPay, 0, DEFAULT_COMMISSION_RATE);
    }

    // PUBLIC getters and setters (Week 4): used by the database and menu.
    public double getBaseMonthlyPay() { return baseMonthlyPay; }
    public double getMonthlySales() { return monthlySales; }
    public double getCommissionRate() { return commissionRate; }
    public void setBaseMonthlyPay(double baseMonthlyPay) { this.baseMonthlyPay = baseMonthlyPay; }
    public void setMonthlySales(double monthlySales) { this.monthlySales = monthlySales; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

    // ABSTRACTION: implements the abstract methods required by Employee.
    @Override
    public String getType() { return "Commission"; }

    @Override
    public double calculatePay() {
        return baseMonthlyPay + monthlySales * commissionRate;
    }

    @Override
    public String getPayDescription() {
        return formatMoney(baseMonthlyPay) + String.format(" base + %.1f%% of ", commissionRate * 100)
                + formatMoney(monthlySales) + " sales";
    }
}
