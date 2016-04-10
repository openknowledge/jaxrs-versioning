package de.openknowledge.jaxrs.versioning.model;

import de.openknowledge.jaxrs.versioning.Provider;
import de.openknowledge.jaxrs.versioning.conversion.VersionContext;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class ZipCodeProvider implements Provider {
  @Override
  public Object get(VersionContext versionContext) {
    AddressV1 address = (AddressV1) versionContext.getParent();
    String[] parts = address.getCity().split(" ");
    return parts[1];
  }
}
