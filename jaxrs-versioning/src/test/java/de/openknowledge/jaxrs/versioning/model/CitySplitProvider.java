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

import de.openknowledge.jaxrs.versioning.Provider;
import de.openknowledge.jaxrs.versioning.VersionContext;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class CitySplitProvider implements Provider<String> {
  @Override
  public String get(VersionContext versionContext) {
    AddressV1 address = versionContext.getParent(AddressV1.class);
    String[] parts = address.getCity().split(" ");
    if (versionContext.getPropertyName().equals("zipCode")) {
      return parts[0];
    } else if (versionContext.getPropertyName().equals("cityName")) {
      return parts[1];
    } else {
      throw new IllegalArgumentException("Unsupported property '" + versionContext.getPropertyName() + "'");
    }
  }
}
