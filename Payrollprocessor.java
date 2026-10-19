/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: PayrollProcessor totals pay for a group of Payable objects.
 *          It only knows about the Payable interface, so it works with
 *          every employee type (and any future Payable class).
 */
import java.util.List;

public class Payrollprocessor {

    /**
     * Adds up the pay for every item in the list.
     * POLYMORPHISM: calculatePay() is called on a Payable reference, and
     * Java runs the version belonging to the actual object (Hourly,
     * Salaried, or Commission) at runtime.
     */
    public double totalPay(List<? extends Payable> items) {
        double total = 0;
        for (Payable item : items) {
            total += item.calculatePay();
        }
        return total;
    }
}