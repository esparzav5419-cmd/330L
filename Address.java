/*
 * Name: valentin esparza
 * Date: 10/11/26
 * Purpose: Address stores an employee's mailing address. It is used to
 *          demonstrate COMPOSITION: every Employee "has an" Address.
 */
public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getCityState() {
        return city + ", " + state;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zip;
    }
}