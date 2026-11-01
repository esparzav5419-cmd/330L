/*
 * Name: valentin esparza
 * Date: 10/31/26
 * Purpose: Employee is an ABSTRACT BASE CLASS for all employee types. It
 *          holds the data every employee shares, implements the Payable
 *          interface, and requires each child class to supply its own
 *          type and pay rules. It also demonstrates COMPOSITION (it holds
 *          an Address), overloaded CONSTRUCTORS, and ACCESS SPECIFIERS.
 *          Week 4: name and address can now be changed through setters so
 *          the update feature can save changes to the database.
 */
public abstract class Employee implements Payable {

    // ACCESS SPECIFIERS: all data is private. Outside code reads it
    // through public getters, so it cannot be changed by accident.
    private static int nextId = 1001; // next automatic ID
    private final int id;
    private String name;       // not final: the update feature changes it
    private Address address;   // COMPOSITION: Employee "has an" Address

    // CONSTRUCTOR 1: full constructor with an explicit ID.
    // ACCESS SPECIFIER: protected. Only child classes call it, because
    // an abstract class can never be created directly.
    protected Employee(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
        if (id >= nextId) {
            nextId = id + 1; // keep automatic IDs from repeating an explicit ID
        }
    }

    // CONSTRUCTOR 2: automatic ID. Reuses constructor 1 with this(...).
    protected Employee(String name, Address address) {
        this(nextId, name, address);
    }

    // PUBLIC: needed by the display code and other classes.
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    public Address getAddress() { return address; }

    // PUBLIC setters (Week 4): the menu's update feature needs to change
    // these values. The ID has no setter because it never changes.
    public void setName(String name) { this.name = name; }

    public void setAddress(Address address) { this.address = address; }

    // ABSTRACTION: abstract methods have no body here. Every child class
    // MUST implement them, so each employee type defines its own rules
    // while the rest of the program treats them all the same way.
    public abstract String getType();

    @Override
    public abstract double calculatePay();

    @Override
    public abstract String getPayDescription();

    // ACCESS SPECIFIER: protected. Only child classes need this helper to
    // format money in their pay descriptions, so it is not public.
    protected String formatMoney(double amount) {
        return String.format("$%,.2f", amount);
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nType: " + getType()
                + "\nAddress: " + address + "\nPay: " + formatMoney(calculatePay());
    }
}
