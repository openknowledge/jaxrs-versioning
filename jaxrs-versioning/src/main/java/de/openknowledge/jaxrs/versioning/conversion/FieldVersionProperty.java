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
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.ClassUtils;

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
  public Class<?> getCollectionElementType() {
    // TODO improve generics resolution
    ParameterizedType type = (ParameterizedType)field.getGenericType();
    return (Class<?>)type.getActualTypeArguments()[0];
  }

  @Override
  public boolean isSimple() {
    return isSimple(type);
  }

  @Override
  public boolean isCollection() {
    return Collection.class.isAssignableFrom(type);
  }

  @Override
  public boolean isCollectionOfSimpleTypes() {
    return isCollection() && isSimple(getCollectionElementType());
  }

  @Override
  public boolean isDefault(Object base) {
    if (!type.isPrimitive()) {
      return get(base) == null;
    }
    Object value = get(base);
    if (value instanceof Boolean) {
      return !((Boolean)value).booleanValue();
    } else if (value instanceof Character) {
      return ((Character)value).equals('\u0000');
    } else {
      return ((Number)value).doubleValue() == 0;
    }
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
      field.set(base, convert(value));
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> type) {
    return field.getAnnotation(type);
  }

  public String toString() {
    return getClass().getSimpleName() + "[name=" + name + "]";
  }

  private boolean isSimple(Class<?> type) {
    return type == String.class || type == Date.class || Enum.class.isAssignableFrom(type) || ClassUtils.isPrimitiveOrWrapper(type);
  }

  private Object convert(Object value) {
    if (value == null || !isSimple() || type.isAssignableFrom(value.getClass())) {
      return value;
    }
    if (Enum.class.isAssignableFrom(type)) {
      return Enum.valueOf((Class<? extends Enum>)type, value.toString());
    }
    Class<?> targetType = type.isPrimitive() ? ClassUtils.primitiveToWrapper(type) : type;
    try {
      return targetType.getConstructor(String.class).newInstance(value.toString());
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }
}
