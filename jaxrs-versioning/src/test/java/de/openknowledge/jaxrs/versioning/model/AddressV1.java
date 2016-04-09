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

import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@SupportedVersion(version = "v1")
@JsonAutoDetect(creatorVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressV1 {

  private StreetV1 street;

  private String city;
  
  private String addressLine1;
  
  private String addressLine2;
  
  private LocationV1 location;

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

  public StreetV1 getStreet() {
    return street;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public LocationV1 getLocation() {
    return location;
  }
}
