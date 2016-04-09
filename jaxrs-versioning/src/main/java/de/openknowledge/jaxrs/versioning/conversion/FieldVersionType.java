package de.openknowledge.jaxrs.versioning.conversion;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;


public class FieldVersionType implements VersionType {

  private ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<String, Field>();
  
  public FieldVersionType(Class<?> type, VersionTypeFactory factory) {
    if (type.getSuperclass() != Object.class) {
      FieldVersionType parent = (FieldVersionType)factory.get(type.getSuperclass());
      fields.putAll(parent.fields);
    }
    for (Field field: type.getDeclaredFields()) {
      field.setAccessible(true);
      fields.put(field.getName(), field);
    }
  }

  @Override
  public Object get(Object base, String name) {
    try {
      return fields.get(name).get(base);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void set(Object base, String name, Object value) {
    try {
      fields.get(name).set(base, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }
}
