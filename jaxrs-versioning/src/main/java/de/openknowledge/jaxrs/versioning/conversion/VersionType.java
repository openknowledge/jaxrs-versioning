package de.openknowledge.jaxrs.versioning.conversion;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public interface VersionType {
  
  Object get(Object base, String name);
  void set(Object base, String name, Object value);
}
