/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: CommissionEmployee is a DERIVED (child) class of Employee for
 *          employees paid a base salary plus a percentage of sales.
 */
public class CommissionEmployee extends Employee { // INHERITANCE

    private double baseMonthlyPay;
    private double monthlySales;
    private double commissionRate;

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
    public double calculatePay() {
        return baseMonthlyPay + monthlySales * commissionRate;
    }
}