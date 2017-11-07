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

import java.util.Arrays;
import java.util.List;

import de.openknowledge.jaxrs.versioning.Added;
import de.openknowledge.jaxrs.versioning.MovedFrom;
import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@SupportedVersion(version = "v2", previous = AddressV1.class)
public class AddressV2 {

  protected String addressLine1;

  protected String addressLine2;

  @Added({
      @MovedFrom("addressLine1"),
      @MovedFrom("addressLine2")
  })
  protected List<String> addressLines;

  protected CityV2 location;

  @MovedFrom("location")
  protected CityV2 city;
  
  @Added(defaultValue = "PRIVATE")
  private AddressTypeV2 type;

  protected AddressV2() {
  }

  public AddressV2(String adressLine1, String adressLine2, CityV2 location) {
    this.addressLine1 = adressLine1;
    this.addressLine2 = adressLine2;
    this.location = location;
  }

  public AddressV2(CityV2 location, String... addressLines) {
    this.addressLines = Arrays.asList(addressLines);
    this.city = location;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public List<String> getAddressLines() {
    return addressLines;
  }

  public void setAddressLines(List<String> addressLines) {
    this.addressLines = addressLines;
  }

  public CityV2 getLocation() {
    return location;
  }

  public void setLocation(CityV2 location) {
    this.location = location;
  }

  public CityV2 getCity() {
    return city;
  }

  public void setCity(CityV2 city) {
    this.city = city;
  }

  public AddressTypeV2 getType() {
    return type;
  }

  public void setType(AddressTypeV2 type) {
    this.type = type;
  }
}
