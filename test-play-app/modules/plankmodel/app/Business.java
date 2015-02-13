package plank.models;

import java.util.Currency;

/**
 * Created by aabramov on 1/20/15.
 */


public class Business {

    public String name;
    public String ein;
    public String addressLine1;
    public String addressLine2;
    public String state;
    public String zipcode;
    public double revenue;
    public String website;


    public static Business getDummy() {

        // integer between 0 - 100
        int rand = (int) Math.random()*100;


        Business bus = new Business();
        bus.name = "name "+rand;
        bus.ein = "12-1234567";
        bus.addressLine1 = rand+" Some St.";
        bus.addressLine2 = "apt. "+rand;
        bus.state = "RI";
        bus.zipcode = "02906";
        bus.revenue = Math.random()*100;
        bus.website = "www.somesite"+rand+".com";

        return bus;

    }

}
