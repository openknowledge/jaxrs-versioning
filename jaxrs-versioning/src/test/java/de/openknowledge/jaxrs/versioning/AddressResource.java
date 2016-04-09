package de.openknowledge.jaxrs.versioning;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

@Path("/{version}/addresses")
public class AddressResource {

  @Produces("application/json")
  @Path("/{id}")
  public AddressV1 getAddress(@PathParam("id") int id) {
    return new AddressV1(
        new StreetV1(
            "Samplestreet",
            "1"
        ),
        "12345 Samplecity");
  }
}
