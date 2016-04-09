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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class CompatibilityMapperTest {

  private VersionTypeFactory factory = new VersionTypeFactory();

  private CompatibilityMapper mapper = new CompatibilityMapper(factory);

  @Test
  public void mapV10To11() {
    StreetV1 streetV1 = new StreetV1() {
      {
        name = "Samplestreet";
        number = "1";
      }
    };
    mapper.map(streetV1);
    assertThat(streetV1.getStreetName(), is("Samplestreet"));
    assertThat(streetV1.getStreetNumber(), is("1"));
  }

  @Test
  public void mapV11To10() {

  }

  @Test
  public void mapV10To14() {
    AddressV1 address = new AddressV1(
        new StreetV1("Samplestreet", "1"),
        "12345 Samplecity");
    mapper.map(address);
    assertThat(address.getAddressLine1(), is("Samplestreet"));
    assertThat(address.getAddressLine2(), is(nullValue()));
  }
}
