/*
 * Copyright (C) open knowledge GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.openknowledge.jaxrs.versioning.conversion;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.LocationV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class CompatibilityMapperTest {

  private VersionTypeFactory factory = new VersionTypeFactory();

  private CompatibilityMapper mapper = new CompatibilityMapper(factory);

  @Test
  public void mapV10() {
    AddressV1 address = createV10();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  @Test
  public void mapV11() {
    AddressV1 address = createV11();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  @Test
  public void mapV12() {
    AddressV1 address = createV12();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  @Test
  public void mapV13() {
    AddressV1 address = createV13();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  @Test
  public void mapV14() {
    AddressV1 address = createV14();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  @Test
  public void mapV15() {
    AddressV1 address = createV15();
    mapper.map(address);
    assertThat(address, is(v10()));
    assertThat(address, is(v11()));
    assertThat(address, is(v12()));
    assertThat(address, is(v13()));
    assertThat(address, is(v14()));
    assertThat(address, is(v15()));
  }

  public AddressV1 createV10() {
    return new AddressV1() {{
      street = new StreetV1() {{
        name = "Samplestreet";
        number = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 createV11() {
    return new AddressV1() {{
      street = new StreetV1() {{
        streetName = "Samplestreet";
        streetNumber = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 createV12() {
    return new AddressV1() {{
      street = new StreetV1() {{
        streetName = "Samplestreet";
        houseNumber = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 createV13() {
    return new AddressV1() {{
      street = new StreetV1() {{
        addressLine1 = "Samplestreet 1";
        addressLine2 = "";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 createV14() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = "";
      zipCode = "12345";
      cityName = "Samplecity";
    }};
  }

  public AddressV1 createV15() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = "";
      location = new LocationV1() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  private Matcher<AddressV1> v10() {
    return allOf(
        streetHasName("Samplestreet"),
        streetHasNumber("1"),
        hasCity("12345 Samplecity"));
  }

  private Matcher<AddressV1> v11() {
    return allOf(
        streetHasStreetName("Samplestreet"),
        streetHasStreetNumber("1"),
        hasCity("12345 Samplecity"));
  }

  private Matcher<AddressV1> v12() {
    return allOf(
        streetHasStreetName("Samplestreet"),
        streetHasHouseNumber("1"),
        hasCity("12345 Samplecity"));
  }

  private Matcher<AddressV1> v13() {
    return allOf(
        streetHasAddressLine1("Samplestreet 1"),
        streetHasAddressLine2(""),
        hasCity("12345 Samplecity"));
  }

  private Matcher<AddressV1> v14() {
    return allOf(
        hasAddressLine1("Samplestreet 1"),
        hasAddressLine2(""),
        hasZipCode("12345"),
        hasCityName("Samplecity"));
  }

  private Matcher<AddressV1> v15() {
    return allOf(
        hasAddressLine1("Samplestreet 1"),
        hasAddressLine2(""),
        locationHasZipCode("12345"),
        locationHasCityName("Samplecity"));
  }

  private Matcher<AddressV1> hasAddressLine1(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "addressLine1", "addressLine1") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getAddressLine1();
       }
    };
  }

  private Matcher<AddressV1> hasAddressLine2(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "addressLine2", "addressLine2") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getAddressLine2();
       }
    };
  }

  private Matcher<AddressV1> hasCity(String city) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(city)), "city", "city") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getCity();
       }
    };
  }

  private Matcher<AddressV1> hasZipCode(String zipCode) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(zipCode)), "zipCode", "zipCode") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getZipCode();
       }
    };
  }

  private Matcher<AddressV1> hasCityName(String city) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(city)), "cityName", "cityName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getCityName();
       }
    };
  }

  private Matcher<AddressV1> streetHasName(String name) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(name)), "street.name", "street.name") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getName();
       }
    };
  }

  private Matcher<AddressV1> streetHasNumber(String number) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(number)), "street.number", "street.number") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getNumber();
       }
    };
  }

  private Matcher<AddressV1> streetHasStreetName(String name) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(name)), "street.streetName", "street.streetName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getStreetName();
       }
    };
  }

  private Matcher<AddressV1> streetHasStreetNumber(String number) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(number)), "street.streetNumber", "street.streetNumber") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getStreetNumber();
       }
    };
  }

  private Matcher<AddressV1> streetHasHouseNumber(String number) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(number)), "street.houseNumber", "street.houseNumber") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getHouseNumber();
       }
    };
  }

  private Matcher<AddressV1> streetHasAddressLine1(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "street.addressLine1", "street.addressLine1") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getAddressLine1();
       }
    };
  }

  private Matcher<AddressV1> streetHasAddressLine2(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "street.addressLine2", "street.addressLine2") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getAddressLine2();
       }
    };
  }

  private Matcher<AddressV1> locationHasZipCode(String zipCode) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(zipCode)), "location.zipCode", "location.zipCode") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getLocation().getZipCode();
       }
    };
  }

  private Matcher<AddressV1> locationHasCityName(String cityName) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(cityName)), "location.cityName", "location.cityName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getLocation().getCityName();
       }
    };
  }
}
