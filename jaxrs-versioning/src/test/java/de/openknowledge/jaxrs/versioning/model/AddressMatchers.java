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
package de.openknowledge.jaxrs.versioning.model;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class AddressMatchers {

  public static Matcher<AddressV1> v10() {
    return allOf(
        streetHasName("Samplestreet"),
        streetHasNumber("1"),
        hasCity("12345 Samplecity"));
  }

  public static Matcher<AddressV1> v11() {
    return allOf(
        streetHasStreetName("Samplestreet"),
        streetHasStreetNumber(1),
        hasCity("12345 Samplecity"));
  }

  public static Matcher<AddressV1> v12() {
    return allOf(
        streetHasStreetName("Samplestreet"),
        streetHasHouseNumber("1"),
        hasCity("12345 Samplecity"));
  }

  public static Matcher<AddressV1> v13() {
    return allOf(
        streetHasAddressLine1("Samplestreet 1"),
        streetHasAddressLine2(" "),
        hasCity("12345 Samplecity"));
  }

  public static Matcher<AddressV1> v14() {
    return allOf(
        hasAddressLine1("Samplestreet 1"),
        hasAddressLine2(" "),
        hasZipCode("12345"),
        hasCityName("Samplecity"));
  }

  public static Matcher<AddressV1> v15() {
    return allOf(
        hasAddressLine1("Samplestreet 1"),
        hasAddressLine2(" "),
        locationHasZipCode("12345"),
        locationHasCityName("Samplecity"));
  }

  public static Matcher<AddressV2> v2() {
    return allOf(
        hasAddressLineOne("Samplestreet 1"),
        hasAddressLineTwo(" "),
        cityHasZipCode("12345"),
        cityHasName("Samplecity"));
  }

  private static Matcher<AddressV1> hasAddressLine1(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "addressLine1", "addressLine1") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getAddressLine1();
       }
    };
  }

  private static Matcher<AddressV1> hasAddressLine2(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "addressLine2", "addressLine2") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getAddressLine2();
       }
    };
  }

  private static Matcher<AddressV1> hasCity(String city) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(city)), "city", "city") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getCity();
       }
    };
  }

  private static Matcher<AddressV1> hasZipCode(String zipCode) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(zipCode)), "zipCode", "zipCode") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getZipCode();
       }
    };
  }

  private static Matcher<AddressV1> hasCityName(String city) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(city)), "cityName", "cityName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getCityName();
       }
    };
  }

  private static Matcher<AddressV1> streetHasName(String name) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(name)), "street.name", "street.name") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getName();
       }
    };
  }

  private static Matcher<AddressV1> streetHasNumber(String number) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(number)), "street.number", "street.number") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getNumber();
       }
    };
  }

  private static Matcher<AddressV1> streetHasStreetName(String name) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(name)), "street.streetName", "street.streetName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getStreetName();
       }
    };
  }

  private static Matcher<AddressV1> streetHasStreetNumber(int number) {
    return new FeatureMatcher<AddressV1, Integer>(is(equalTo(number)), "street.streetNumber", "street.streetNumber") {
       protected Integer featureValueOf(AddressV1 actual) {
          return actual.getStreet().getStreetNumber();
       }
    };
  }

  private static Matcher<AddressV1> streetHasHouseNumber(String number) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(number)), "street.houseNumber", "street.houseNumber") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getHouseNumber();
       }
    };
  }

  private static Matcher<AddressV1> streetHasAddressLine1(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "street.addressLine1", "street.addressLine1") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getAddressLine1();
       }
    };
  }

  private static Matcher<AddressV1> streetHasAddressLine2(String line) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(line)), "street.addressLine2", "street.addressLine2") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getStreet().getAddressLine2();
       }
    };
  }

  private static Matcher<AddressV1> locationHasZipCode(String zipCode) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(zipCode)), "location.zipCode", "location.zipCode") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getLocation().getZipCode();
       }
    };
  }

  private static Matcher<AddressV1> locationHasCityName(String cityName) {
    return new FeatureMatcher<AddressV1, String>(is(equalTo(cityName)), "location.cityName", "location.cityName") {
       protected String featureValueOf(AddressV1 actual) {
          return actual.getLocation().getCityName();
       }
    };
  }

  private static Matcher<AddressV2> hasAddressLineOne(String line) {
    return new FeatureMatcher<AddressV2, String>(is(equalTo(line)), "addressLine1", "addressLine1") {
       protected String featureValueOf(AddressV2 actual) {
          return actual.getAddressLine1();
       }
    };
  }

  private static Matcher<AddressV2> hasAddressLineTwo(String line) {
    return new FeatureMatcher<AddressV2, String>(is(equalTo(line)), "addressLine2", "addressLine2") {
       protected String featureValueOf(AddressV2 actual) {
          return actual.getAddressLine2();
       }
    };
  }

  private static Matcher<AddressV2> cityHasZipCode(String zipCode) {
    return new FeatureMatcher<AddressV2, String>(is(equalTo(zipCode)), "city.zipCode", "city.zipCode") {
       protected String featureValueOf(AddressV2 actual) {
          return actual.getCity().getZipCode();
       }
    };
  }

  private static Matcher<AddressV2> cityHasName(String city) {
    return new FeatureMatcher<AddressV2, String>(is(equalTo(city)), "city.cityName", "city.cityName") {
       protected String featureValueOf(AddressV2 actual) {
          return actual.getCity().getCityName();
       }
    };
  }
}
