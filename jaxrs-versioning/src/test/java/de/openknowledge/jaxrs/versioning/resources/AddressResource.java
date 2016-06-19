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
package de.openknowledge.jaxrs.versioning.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.openknowledge.jaxrs.versioning.model.AddressV3;
import de.openknowledge.jaxrs.versioning.model.CityV3;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@Path("/{version}/addresses")
public class AddressResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<AddressV3> getAddresses() {
    return Arrays.asList(
        new AddressV3(
            "Samplestreet 1",
            " ",
            new CityV3(
                "12345",
                "Samplecity")),
        new AddressV3(
            "Samplestreet 2",
            " ",
            new CityV3(
                "12345",
                "Samplecity")));
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<AddressV3> setAddresses(List<AddressV3> addresses) {
    if (addresses.size() != 1) {
      throw new IllegalArgumentException("address list must contain only one address");
    }
    if (!addresses.get(0).getAddressLine1().equals("Samplestreet 1")) {
      throw new IllegalArgumentException("wrong address line 1");
    }
    if (!addresses.get(0).getAddressLine2().equals(" ")) {
      throw new IllegalArgumentException("wrong address line 2");
    }
    if (!addresses.get(0).getCity().getZipCode().equals("12345")) {
      throw new IllegalArgumentException("wrong zip code");
    }
    if (!addresses.get(0).getCity().getCityName().equals("Samplecity")) {
      throw new IllegalArgumentException("wrong zip code");
    }
    return addresses;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public AddressV3 getAddress(@PathParam("id") int id) {
    return new AddressV3(
        "Samplestreet 1",
        " ",
        new CityV3(
            "12345",
            "Samplecity"));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public AddressV3 createAddress(AddressV3 address) {
    if (!address.getAddressLine1().equals("Samplestreet 1")) {
      throw new IllegalArgumentException("wrong address line 1");
    }
    if (!address.getAddressLine2().equals(" ")) {
      throw new IllegalArgumentException("wrong address line 2");
    }
    if (!address.getCity().getZipCode().equals("12345")) {
      throw new IllegalArgumentException("wrong zip code");
    }
    if (!address.getCity().getCityName().equals("Samplecity")) {
      throw new IllegalArgumentException("wrong zip code");
    }
    return address;
  }

  @POST
  @Path("/noTypeInformation")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<?> getWithNoVersionInformation(List parameter) {
    return parameter;
  }
}
