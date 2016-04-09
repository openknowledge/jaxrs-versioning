package de.openknowledge.jaxrs.versioning.conversion;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
  public VersionProperty getProperty(String name) {
    return fields.get(name);
  }
}
