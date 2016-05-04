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
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v11;
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v12;
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v13;
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v14;
import static de.openknowledge.jaxrs.versioning.model.AddressMatchers.v15;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.AddressV2;
import de.openknowledge.jaxrs.versioning.model.AddressV3;
import de.openknowledge.jaxrs.versioning.model.CityV2;
import de.openknowledge.jaxrs.versioning.model.CityV3;
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

  @Test
  public void mapV20() {
    AddressV2 address = createV20();
    mapper.map(address);
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getAddressLines(), hasItems("Samplestreet 1", " "));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
  }

  @Test
  public void mapV21() {
    AddressV2 address = createV21();
    mapper.map(address);
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getAddressLines(), hasItems("Samplestreet 1", " "));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
  }

  @Test
  public void mapV30() {
    AddressV3 address = createV30();
    mapper.map(address);
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(" "));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
  }

  @Test
  public void mapV31() {
    AddressV3 address = createV31();
    mapper.map(address);
    assertThat(address.getAddressLines().size(), is(2));
    assertThat(address.getAddressLines().iterator().next(), is("Samplestreet 1"));
    assertThat(address.getAddressLines(), hasItem(" "));
    assertThat(address.getCity().getZipCode(), is("12345"));
    assertThat(address.getCity().getCityName(), is("Samplecity"));
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
        streetNumber = 1;
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
        addressLine2 = " ";
      }};
      city = "12345 Samplecity";
    }};
  }

  public AddressV1 createV14() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = " ";
      zipCode = "12345";
      cityName = "Samplecity";
    }};
  }

  public AddressV1 createV15() {
    return new AddressV1() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = " ";
      location = new LocationV1() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  public AddressV2 createV20() {
    return new AddressV2() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = " ";
      location = new CityV2() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  public AddressV2 createV21() {
    return new AddressV2() {{
      addressLines = Arrays.asList("Samplestreet 1", " ");
      city = new CityV2() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  public AddressV3 createV30() {
    return new AddressV3() {{
      addressLines.addAll(asList("Samplestreet 1", " "));
      city = new CityV3() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }

  public AddressV3 createV31() {
    return new AddressV3() {{
      addressLine1 = "Samplestreet 1";
      addressLine2 = " ";
      city = new CityV3() {{
        zipCode = "12345";
        cityName = "Samplecity";
      }};
    }};
  }
}
