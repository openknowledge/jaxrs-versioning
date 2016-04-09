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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class FieldVersionType implements VersionType {

  private Map<String, VersionProperty> fields;
  
  public FieldVersionType(Class<?> type, VersionTypeFactory factory) {
    ConcurrentHashMap<String, VersionProperty> fields = new ConcurrentHashMap<String, VersionProperty>();
    if (type.getSuperclass() != Object.class) {
      FieldVersionType parent = (FieldVersionType)factory.get(type.getSuperclass());
      fields.putAll(parent.fields);
    }
    for (Field field: type.getDeclaredFields()) {
      field.setAccessible(true);
      fields.put(field.getName(), new FieldVersionProperty(field));
    }
    this.fields = Collections.unmodifiableMap(fields);
  }

  @Override
  public Collection<VersionProperty> getProperties() {
    return fields.values();
  }

  @Override
  public VersionProperty getProperty (String name) {
    VersionProperty versionProperty = fields.get(name);
    if(versionProperty == null) {
      throw new IllegalArgumentException("@MovedFrom contains unknown property: " + name );
    }
    return versionProperty;
  }
}
