package de.openknowledge.jaxrs.versioning;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

@Path("/{version}/addresses")
public class AddressResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public AddressV1 getAddress(@PathParam("id") int id) {
    return new AddressV1(
        new StreetV1(
            "Samplestreet",
            "1"
        ),
        "12345 Samplecity");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public AddressV1 createAddress(String addressJson) {
    JSONObject address = new JSONObject(addressJson);
    if (!address.getJSONObject("street").getString("name").equals("Samplestreet")) {
      throw new IllegalArgumentException("wrong street");
    }
    if (!address.getJSONObject("street").getString("number").equals("1")) {
      throw new IllegalArgumentException("wrong number");
    }
    if (!address.getString("city").equals("12345 Samplecity")) {
      throw new IllegalArgumentException("wrong city");
    }
    return new AddressV1(
        new StreetV1(
            address.getJSONObject("street").getString("name"),
            address.getJSONObject("street").getString("number")),
        address.getString("city"));
  }
}
