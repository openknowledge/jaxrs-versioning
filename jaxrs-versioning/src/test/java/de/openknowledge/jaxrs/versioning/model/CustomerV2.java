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
import java.util.HashSet;
import java.util.Set;

import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@SupportedVersion(version = "v2", previous = CustomerV1.class)
public class CustomerV2 {

  private String name;
  private Set<AddressV2> addresses;

  protected CustomerV2() {
  }
  
  public CustomerV2(String name, AddressV2... addresses) {
    this.name = name;
    this.addresses = new HashSet<AddressV2>(Arrays.asList(addresses));
  }

  public String getName() {
    return name;
  }

  public Set<AddressV2> getAddresses() {
    return addresses;
  }
}
