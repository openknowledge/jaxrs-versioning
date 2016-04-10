package de.openknowledge.jaxrs.versioning.model;

import de.openknowledge.jaxrs.versioning.Provider;
import de.openknowledge.jaxrs.versioning.conversion.VersionContext;

/**
 * @author Philipp Geers - open knowledge GmbH
 */
public class StreetAggregationProvider implements Provider{

  @Override
  public Object get(VersionContext versionContext) {
    Object streetParent = versionContext.getParent();
    String streetName = ((StreetV1)streetParent).getStreetName();
    String number = ((StreetV1)streetParent).getHouseNumber();
    return streetName + " " + number;
  }
}
