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
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.AddressV2;
import de.openknowledge.jaxrs.versioning.model.LocationV1;
import de.openknowledge.jaxrs.versioning.model.LocationV2;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class InterVersionConverterTest {

  private VersionTypeFactory factory = new VersionTypeFactory();
  private CompatibilityMapper mapper = new CompatibilityMapper(factory);
  private InterVersionConverter converter = new InterVersionConverter(factory, mapper);

  @Test
  public void convertToLowerVersion() {
    AddressV1 address = (AddressV1)converter.convertToLowerVersion("v1", new AddressV2("Samplestreet 1", "", new LocationV2("12345", "Samplecity")));
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(""));
    assertThat(address.getZipCode(), is("12345"));
    assertThat(address.getCityName(), is("Samplecity"));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
  }

  @Test
  public void convertToHigherVersion() {
    AddressV2 address = (AddressV2)converter.convertToHigherVersion(AddressV2.class, new AddressV1("Samplestreet 1", "", new LocationV1("12345", "Samplecity")), "v1");
    assertThat(address.getAddressLine1(), is("Samplestreet 1"));
    assertThat(address.getAddressLine2(), is(""));
    assertThat(address.getLocation().getZipCode(), is("12345"));
    assertThat(address.getLocation().getCityName(), is("Samplecity"));
  }
}
