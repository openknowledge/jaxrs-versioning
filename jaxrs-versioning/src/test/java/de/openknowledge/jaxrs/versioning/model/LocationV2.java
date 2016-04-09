package java.de.openknowledge.jaxrs.versioning.model;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class LocationV2 {

    private String zipCode;

    private String cityName;

    public LocationV2(String zipCode, String cityName) {
        this.zipCode = zipCode;
        this.cityName = cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCityName() {
        return cityName;
    }
}
