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

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import de.openknowledge.jaxrs.versioning.Added;
import de.openknowledge.jaxrs.versioning.MovedFrom;
import de.openknowledge.jaxrs.versioning.Removed;
import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@SupportedVersion(version = "v1")
@JsonAutoDetect(creatorVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressV1 {

  @Removed
  protected StreetV1 street;

  @Removed(provider = CityAggregationProvider.class, isDependencyOf = {"zipCode", "cityName"})
  protected String city;
  
  @Added(provider = CitySplitProvider.class, dependsOn = "city")
  protected String zipCode;

  @Added(provider = CitySplitProvider.class, dependsOn = "city")
  protected String cityName;
  
  @MovedFrom("street/addressLine1")
  protected String addressLine1;
  
  @MovedFrom("street/addressLine2")
  protected String addressLine2;

  @Added
  protected LocationV1 location;

  protected AddressV1() {
  }

  public AddressV1(StreetV1 street, String city) {
    this.street = street;
    this.city = city;
  }

  public AddressV1(String addressLine1, String addressLine2, LocationV1 location) {
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.location = location;
  }

  public void setStreet(StreetV1 street) {
    this.street = street;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public void setLocation(LocationV1 location) {
    this.location = location;
  }

  public StreetV1 getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }
  
  public String getZipCode() {
    return zipCode;
  }

  public String getCityName() {
    return cityName;
  }

  public LocationV1 getLocation() {
    return location;
  }
}
