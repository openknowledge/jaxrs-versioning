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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import de.openknowledge.jaxrs.versioning.Added;
import de.openknowledge.jaxrs.versioning.MovedFrom;
import de.openknowledge.jaxrs.versioning.Removed;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StreetV1 {

  protected String name;

  protected String number;

  @MovedFrom("name")
  @Removed(provider = StreetNameProvider.class)
  protected String streetName;

  @MovedFrom("number")
  protected int streetNumber;

  @MovedFrom("streetNumber")
  @Removed(provider = HouseNumberProvider.class)
  protected String houseNumber;

  @Added(provider = StreetAggregationProvider.class, dependsOn = {"streetName", "houseNumber"})
  protected String addressLine1;

  @Added(defaultValue = " ")
  protected String addressLine2;

  protected StreetV1() {}

  public StreetV1(String addressLineOne) {
    addressLine1 = addressLineOne;
  }

  public StreetV1(String name, String number) {
    this.name = name;
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public int getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(int streetNumber) {
    this.streetNumber = streetNumber;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
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
}
