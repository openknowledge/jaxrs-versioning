package de.openknowledge.jaxrs.versioning.conversion;

import java.util.concurrent.ConcurrentHashMap;

public class VersionTypeFactory {

  private ConcurrentHashMap<Class<?>, VersionType> types = new ConcurrentHashMap<Class<?>, VersionType>();

  public VersionType get(Class<?> type) {
    VersionType versionType = types.get(type);
    if (versionType != null) {
      return versionType;
    }
    types.putIfAbsent(type, create(type));
    return get(type);
  }

  private VersionType create(Class<?> type) {
    return new FieldVersionType(type, this);
  }
}
