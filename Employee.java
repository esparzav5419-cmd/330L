/*
 * Name: valentin esparza
 * Date: 10/18/2026
 * Purpose: Employee is the BASE CLASS for all employee types. It holds the
 *          data common to every employee, implements the Payable interface,
 *          and demonstrates COMPOSITION by containing an Address object.
 */
public class Employee implements Payable { // implements the INTERFACE

    private final int id;
    private final String name;
    private final Address address; // COMPOSITION: Employee "has an" Address

    public Employee(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public Address getAddress() { return address; }

    /** Overridden by child classes. */
    public String getType() { return "Employee"; }

    /** Overridden by child classes with their own pay rules (POLYMORPHISM). */
    @Override
    public double calculatePay() { return 0.0; }

    /** Overridden by child classes to explain how pay was figured. */
    @Override
    public String getPayDescription() { return "Not specified"; }

    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nType: " + getType()
                + "\nAddress: " + address
                + String.format("%nPay: $%,.2f", calculatePay());
    }
}
