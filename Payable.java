/*
 * Name: valentin esparza
 * Date: 10/25/26
 * Purpose: Payable is an INTERFACE that defines what it means to be paid
 *          through payroll. Any class that implements it promises to
 *          provide a name, a pay amount, and a description of how the
 *          pay was figured. It lets payroll code work with any payable
 *          object without knowing its exact class.
 */
public interface Payable { // INTERFACE: defined here, implemented by Employee

    /** @return the name of the person or entity being paid */
    String getName();

    /** @return the pay amount for the current pay period */
    double calculatePay();

    /** @return a short text explanation of how the pay was figured */
    String getPayDescription();
}
