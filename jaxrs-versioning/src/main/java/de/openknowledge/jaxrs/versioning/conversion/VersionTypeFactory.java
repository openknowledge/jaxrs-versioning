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

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class VersionTypeFactory {

  private ConcurrentHashMap<Class<?>, VersionType<?>> types = new ConcurrentHashMap<Class<?>, VersionType<?>>();

  public <T> VersionType<T> get(Class<T> type) {
    VersionType<T> versionType = (VersionType<T>)types.get(type);
    if (versionType != null) {
      return versionType;
    }
    types.putIfAbsent(type, create(type));
    return get(type);
  }

  private <T> VersionType<T> create(Class<T> type) {
    return new FieldVersionType<T>(type, this);
  }
}
