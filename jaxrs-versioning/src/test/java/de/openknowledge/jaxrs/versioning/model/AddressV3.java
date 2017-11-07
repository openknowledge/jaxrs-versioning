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

import static java.util.Arrays.asList;
import static java.util.Collections.reverseOrder;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.openknowledge.jaxrs.versioning.MovedFrom;
import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@SupportedVersion(version = "v3", previous = AddressV2.class)
public class AddressV3 {

  protected SortedSet<String> addressLines = new TreeSet<String>(reverseOrder());

  @MovedFrom("addressLines[0]")
  protected String addressLine1;

  @MovedFrom("addressLines[1]")
  protected String addressLine2;

  protected CityV3 city;

  private AddressTypeV3 type;

  protected AddressV3() {
  }

  public AddressV3(String adressLine1, String adressLine2, CityV3 location) {
    this.addressLine1 = adressLine1;
    this.addressLine2 = adressLine2;
    this.city = location;
  }

  public AddressV3(CityV3 location, String... addressLines) {
    this.addressLines.addAll(asList(addressLines));
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

  public Set<String> getAddressLines() {
    return addressLines;
  }

  public void setAddressLines(SortedSet<String> addressLines) {
    this.addressLines = addressLines;
  }

  public CityV3 getCity() {
    return city;
  }

  public void setCity(CityV3 city) {
    this.city = city;
  }

  public AddressTypeV3 getType() {
    return type;
  }

  public void setType(AddressTypeV3 type) {
    this.type = type;
  }
}
