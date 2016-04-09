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

import static org.apache.commons.lang3.Validate.notNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class FieldVersionProperty implements VersionProperty {

  private String name;
  private Class<?> type;
  private Field field;

  public FieldVersionProperty(Field field) {
    this.field = notNull(field);
    this.name = field.getName();
    this.type = field.getType();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public Object get(Object base) {
    try {
      return field.get(base);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void set(Object base, Object value) {
    try {
      field.set(base, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> type) {
    return field.getAnnotation(type);
  }
}
