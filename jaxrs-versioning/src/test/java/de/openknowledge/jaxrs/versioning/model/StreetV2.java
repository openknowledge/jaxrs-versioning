package java.de.openknowledge.jaxrs.versioning.model;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class StreetV2 {

    private String adressLine1;

    private String adressLine2;

    public StreetV2(String adressLine1, String adressLine2) {
        this.adressLine1 = adressLine1;
        this.adressLine2 = adressLine2;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public String getAdressLine2() {
        return adressLine2;
    }

}
