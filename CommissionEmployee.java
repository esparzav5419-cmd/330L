/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: CommissionEmployee is a DERIVED (child) class of Employee for
 *          employees paid a base salary plus a percentage of sales.
 */
public class CommissionEmployee extends Employee { // INHERITANCE

    private final double baseMonthlyPay;
    private final double monthlySales;
    private final double commissionRate;

    public CommissionEmployee(int id, String name, Address address,
                              double baseMonthlyPay, double monthlySales,
                              double commissionRate) {
        super(id, name, address);
        this.baseMonthlyPay = baseMonthlyPay;
        this.monthlySales = monthlySales;
        this.commissionRate = commissionRate;
    }

    @Override
    public String getType() { return "Commission"; }

    @Override
    public String getPayDescription() {
        return String.format("$%,.2f base + %.1f%% of $%,.2f sales",
                baseMonthlyPay, commissionRate * 100, monthlySales);
    }

    @Override
    public double calculatePay() {
        return baseMonthlyPay + monthlySales * commissionRate;
    }
}
