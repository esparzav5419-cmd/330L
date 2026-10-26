/*
 * Name: valentin esparza
 * Date: 10/25/26
 * Purpose: Address stores an employee's mailing address. It demonstrates
 *          COMPOSITION (every Employee "has an" Address), several kinds of
 *          CONSTRUCTORS (default, parameterized, and copy), and ACCESS
 *          SPECIFIERS (private data, one private helper method).
 */
public class Address {

    // ACCESS SPECIFIER: private. Nothing outside this class needs to
    // change these values directly, so they are not exposed.
    private final String street;
    private final String city;
    private final String state;
    private final String zip;

    // CONSTRUCTOR 1 (default / no-argument): used when no address is on file.
    public Address() {
        this("Address not on file", "", "", "");
    }

    // CONSTRUCTOR 2 (partial): used when only the city and state are known.
    public Address(String city, String state) {
        this("Street not on file", city, state, "");
    }

    // CONSTRUCTOR 3 (full parameterized): every other constructor ends up here.
    public Address(String street, String city, String state, String zip) {
        this.street = clean(street);
        this.city = clean(city);
        this.state = clean(state);
        this.zip = clean(zip);
    }

    // CONSTRUCTOR 4 (copy): builds a new Address with the same values.
    public Address(Address other) {
        this(other.street, other.city, other.state, other.zip);
    }

    // ACCESS SPECIFIER: private helper. Only the constructors use it, so it
    // is hidden from the rest of the program.
    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    /** Public because the display code needs a short location. */
    public String getCityState() {
        if (city.isEmpty()) {
            return "Not on file";
        }
        return state.isEmpty() ? city : city + ", " + state;
    }

    @Override
    public String toString() {
        if (city.isEmpty()) {
            return street;
        }
        return (street + ", " + city + ", " + state + " " + zip).trim();
    }
}
