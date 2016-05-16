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

import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v10;
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v2;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.AddressV2;
import de.openknowledge.jaxrs.versioning.model.AddressV3;
import de.openknowledge.jaxrs.versioning.model.CityV2;
import de.openknowledge.jaxrs.versioning.model.CityV3;
import de.openknowledge.jaxrs.versioning.model.CustomerV1;
import de.openknowledge.jaxrs.versioning.model.CustomerV2;
import de.openknowledge.jaxrs.versioning.model.LocationV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class InterversionConverterTest {

  private VersionTypeFactory factory = new VersionTypeFactory();
  private CompatibilityMapper mapper = new CompatibilityMapper(factory);
  private InterversionConverter converter = new InterversionConverter(factory, mapper);

  @Before
  public void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("GTM"));
  }

  @After
  public void resetTimeZone() {
    TimeZone.setDefault(null);
  }

  @Test
  public void convertAddressFromV2ToV1() {
    AddressV1 address = converter.convertToLowerVersion("v1", new AddressV2("Samplestreet 1", " ", new CityV2("12345", "Samplecity")));
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getZipCode(), is("12345"));
    assertThat(address.getCityName(), is("Samplecity"));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertAddressFromV1ToV2() {
    AddressV2 address = converter.convertToHigherVersion(AddressV2.class,
        new AddressV1("Samplestreet 1", " ", new LocationV1("12345", "Samplecity")), "v1");
    assertThat(address.getAddressLines().get(0), is("Samplestreet 1"));
    assertThat(address.getAddressLines().get(1), is(" "));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertAddressFromV3ToV2() {
    AddressV2 address = converter.convertToLowerVersion("v2",
        new AddressV3(new CityV3("12345", "Samplecity"), "Samplestreet 1", " "));
    assertThat(address.getAddressLines().size(), is(2));
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertAddressFromV2ToV3() {
    AddressV3 address = converter.convertToHigherVersion(AddressV3.class, new AddressV2("Samplestreet 1", " ", new CityV2("12345", "Samplecity")), "v2");
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getAddressLines().size(), is(2));
    assertThat(address.getAddressLines(), hasItem("Samplestreet 1"));
    assertThat(address.getAddressLines(), hasItem(" "));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertAddressFromV3ToV1() {
    AddressV1 address = converter.convertToLowerVersion("v1", new AddressV3("Samplestreet 1", " ", new CityV3("12345", "Samplecity")));
    assertThat(address.getStreet().getName(), is("Samplestreet"));
    assertThat(address.getStreet().getNumber(), is("1"));
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getCity(), is("12345 Samplecity"));
  }

  @Test
  public void convertAddressFromV1ToV3() {
    AddressV3 address = converter.convertToHigherVersion(AddressV3.class, new AddressV1(new StreetV1("Samplestreet", "1"), "12345 Samplecity"), "v1");
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getAddressLines().size(), is(2));
    assertThat(address.getAddressLines(), hasItem("Samplestreet 1"));
    assertThat(address.getAddressLines(), hasItem(" "));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertCustomerFromV1ToV2() {
    CustomerV2 customer = converter.convertToHigherVersion(CustomerV2.class, new CustomerV1() {{
      name = "customer";
      dateOfBirth = "01/01/1970";
      addresses = Arrays.asList(new AddressV1(new StreetV1("Samplestreet", "1"), "12345 Samplecity"));
    }}, "v1");
    assertThat(customer.getName(), is("customer"));
    assertThat(customer.getDateOfBirth(), is(new Date(0)));
    assertThat(customer.getAddresses().size(), is(1));
    assertThat(customer.getAddresses(), hasItem(v2()));
  }

  @Test
  public void convertCustomerFromV2ToV1() {
    CustomerV1 customer = converter.convertToLowerVersion("v1",
        new CustomerV2("customer", new Date(0), new AddressV2("Samplestreet 1", " ", new CityV2("12345", "Samplecity"))));
    assertThat(customer.getName(), is("customer"));
    assertThat(customer.getDateOfBirth(), is("Thu Jan 01 00:00:00 GMT 1970"));
    assertThat(customer.getAddresses().size(), is(1));
    assertThat(customer.getAddresses(), hasItem(v10()));
  }

  @Test
  public void convertWithNullList() {
    CustomerV2 customer = converter.convertToHigherVersion(CustomerV2.class, new CustomerV1() {{
      name = "customer";
      dateOfBirth = "01/01/1970";
      addresses = null;
    }}, "v1");
    assertThat(customer.getName(), is("customer"));
    assertThat(customer.getDateOfBirth(), is(new Date(0)));
    assertThat(customer.getAddresses(), is(nullValue()));
  }

  @Test(expected = IllegalVersionException.class)
  public void convertToLowerVersionThrowsExceptionForUnversionedObject() {
    converter.convertToLowerVersion("v1", new LocationV1("12345", "Samplecity"));
  }

  @Test(expected = IllegalVersionException.class)
  public void convertToLowerVersionThrowsExceptionForUnsupportedVersion() {
    converter.convertToLowerVersion("v0", new AddressV1("Samplestreet 1", " ", new LocationV1("12345", "Samplecity")));
  }

  @Test(expected = IllegalVersionException.class)
  public void convertToHigherVersionThrowsExceptionForUnversionedObject() {
    converter.convertToHigherVersion(LocationV1.class, new LocationV1("12345", "Samplecity"), "v1");
  }

  @Test(expected = IllegalVersionException.class)
  public void convertToHigherVersionThrowsExceptionForUnsupportedVersion() {
    converter.convertToHigherVersion(AddressV1.class, new AddressV1("Samplestreet 1", " ", new LocationV1("12345", "Samplecity")), "v0");
  }
}
