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

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.LocationV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@Ignore("Fix provider")
public class CompatibilityMapperTest {

  private VersionTypeFactory factory = new VersionTypeFactory();

  private CompatibilityMapper mapper = new CompatibilityMapper(factory);

  @Test
  public void mapV10() {
    AddressV1 address = v10();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  @Test
  public void mapV11() {
    AddressV1 address = v11();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  @Test
  public void mapV12() {
    AddressV1 address = v12();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  @Test
  public void mapV13() {
    AddressV1 address = v13();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  @Test
  public void mapV14() {
    AddressV1 address = v14();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  @Test
  public void mapV15() {
    AddressV1 address = v15();
    mapper.map(address);
    assertThat(address).isV10();
    assertThat(address).isV11();
    assertThat(address).isV12();
    assertThat(address).isV13();
    assertThat(address).isV14();
    assertThat(address).isV15();
  }

  public AddressV1 v10() {
    return new AddressV1() {{
      street = new StreetV1() {{
        name = "Samplestreet";
        number = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 v11() {
    return new AddressV1() {{
      street = new StreetV1() {{
        streetName = "Samplestreet";
        streetNumber = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 v12() {
    return new AddressV1() {{
      street = new StreetV1() {{
        streetName = "Samplestreet";
        houseNumber = "1";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 v13() {
    return new AddressV1() {{
      street = new StreetV1() {{
        addressLine1 = "Samplestreet 1";
        addressLine2 = "";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 v14() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = "";
      zipCode = "12345";
      cityName = "Samplecity";
    }};
  }

  public AddressV1 v15() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = "";
      location = new LocationV1() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  public AddressMatcher assertThat(AddressV1 address) {
    return new AddressMatcher(address);
  }
  
  private <T> void assertThat(T actual, Matcher<? super T> matcher) {
    Assert.assertThat(actual, matcher);
  }

  public static class AddressMatcher {
    
    private AddressV1 address;
    
    private AddressMatcher(AddressV1 address) {
      this.address = address;
    }

    public void isV10() {
      assertThat(address.getStreet().getName(), is("Samplestreet"));
      assertThat(address.getStreet().getNumber(), is("1"));
      assertThat(address.getCity(), is("12345 Samplecity"));
    }

    public void isV11() {
      assertThat(address.getStreet().getStreetName(), is("Samplestreet"));
      assertThat(address.getStreet().getStreetNumber(), is("1"));
      assertThat(address.getCity(), is("12345 Samplecity"));
    }

    public void isV12() {
      assertThat(address.getStreet().getStreetName(), is("Samplestreet"));
      assertThat(address.getStreet().getHouseNumber(), is("1"));
      assertThat(address.getCity(), is("12345 Samplecity"));
    }

    public void isV13() {
      assertThat(address.getStreet().getAddressLine1(), is("Samplestreet 1"));
      assertThat(address.getStreet().getAddressLine2(), is(""));
      assertThat(address.getCity(), is("12345 Samplecity"));
    }

    public void isV14() {
      assertThat(address.getAddressLine1(), is("Samplestreet 1"));
      assertThat(address.getAddressLine2(), is(""));
      assertThat(address.getZipCode(), is("12345"));
      assertThat(address.getCityName(), is("Samplecity"));
    }

    public void isV15() {
      assertThat(address.getAddressLine1(), is("Samplestreet 1"));
      assertThat(address.getAddressLine2(), is(""));
      assertThat(address.getLocation().getZipCode(), is("12345"));
      assertThat(address.getLocation().getCityName(), is("Samplecity"));
    }
    
    private <T> void assertThat(T actual, Matcher<? super T> matcher) {
      Assert.assertThat(actual, matcher);
    }
  }
}
