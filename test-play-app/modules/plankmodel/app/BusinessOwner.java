package plank.models;

/**
 * Created by aabramov on 1/20/15.
 */


public class BusinessOwner {

    public String firstName;
    public String lastName;
    public String email;
    public String ssn;
    public String addressLine1;
    public String addressLine2;
    public String state;
    public String zipcode;


    public static BusinessOwner getDummy() {

        // integer between 0 - 100
        int rand = (int) Math.random()*100;


        BusinessOwner bus = new BusinessOwner();
        bus.firstName = "firstName "+rand;
        bus.lastName = "lastName "+rand;
        bus.email = "email"+rand+"@gmail.com";

        bus.ssn = "012-34-6789";
        bus.addressLine1 = rand+" Some St.";
        bus.addressLine2 = "apt. "+rand;
        bus.state = "RI";
        bus.zipcode = "02906";

        return bus;

    }
}
