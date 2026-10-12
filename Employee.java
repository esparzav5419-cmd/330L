/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: Employee is the BASE CLASS for all employee types. It holds the
 *          data common to every employee. It also demonstrates COMPOSITION
 *          by containing an Address object.
 */
public class Employee {

    private int id;
    private String name;
    private Address address; // COMPOSITION: Employee "has an" Address

    public Employee(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Address getAddress() { return address; }

    /** Overridden by child classes. */
    public String getType() { return "Employee"; }

    /** Overridden by child classes with their own pay rules. */
    public double calculatePay() { return 0.0; }

    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nType: " + getType()
                + "\nAddress: " + address
                + String.format("%nPay: $%,.2f", calculatePay());
    }
}