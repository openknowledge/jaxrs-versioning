package de.openknowledge.jaxrs.versioning.conversion;

import java.util.Collection;

/**
 * @author Philipp Geers - open knowledge GmbH
 * @author Arne Limburg - open knowledge GmbH
 */
public interface VersionType {
  
  Collection<VersionProperty> getProperties();
  VersionProperty getProperty(String name);
}
