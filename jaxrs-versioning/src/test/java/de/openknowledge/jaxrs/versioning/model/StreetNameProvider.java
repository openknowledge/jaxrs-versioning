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
 * @author Arne Limburg - open knowledge GmbH
 */
public class StreetNameProvider implements Provider<String> {
  @Override
  public String get(VersionContext versionContext) {
    StreetV1 street = versionContext.getParent(StreetV1.class);
    String[] parts = street.getAddressLine1().split(" ");
    return parts[0];
  }
}
