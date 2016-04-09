package de.openknowledge.jaxrs.versioning.model;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class AddressV2 {

    private String adressLine1;

    private String adressLine2;

    private LocationV2 location;

    public AddressV2(String adressLine1, String adressLine2, LocationV2 location) {
        this.adressLine1 = adressLine1;
        this.adressLine2 = adressLine2;
        this.location = location;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public String getAdressLine2() {
        return adressLine2;
    }

}
