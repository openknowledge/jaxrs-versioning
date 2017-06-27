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
package de.openknowledge.jaxrs.versioning;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.openknowledge.jaxrs.versioning.conversion.MessageBodyConverter;
import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.AddressV2;
import de.openknowledge.jaxrs.versioning.resources.VersionHeaderAddressResource;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@RunAsClient
@RunWith(Arquillian.class)
public class VersionHeaderFeatureTest {

  @Deployment
  public static WebArchive deployment() {
    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    return ShrinkWrap.create(WebArchive.class)
        .addClasses(SampleApplication.class, VersionHeaderAddressResource.class)
        .addPackage(AddressV1.class.getPackage())
        .addPackage(MovedFrom.class.getPackage())
        .addPackage(MessageBodyConverter.class.getPackage())
        .addAsLibraries(pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .addAsLibraries(pom.resolve("org.json:json").withTransitivity().asFile())
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .version("3.0")
          .exportAsString()));
  }

  @Test
  public void postAddressV20(@ArquillianResource URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)new URL(url, "addresses/42").openConnection();
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Api-Version", "v2");
    PrintWriter writer = new PrintWriter(connection.getOutputStream());
    for (String line: IOUtils.readLines(AddressV2.class.getResourceAsStream("address_v2_0.json"))) {
      writer.write(line);
    }
    writer.close();
    
    JSONObject address = new JSONObject(IOUtils.toString(connection.getInputStream()));
    JSONObject location = address.getJSONObject("location");
    assertThat(address.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(address.getString("addressLine2"), is(" "));
    assertThat(location.getString("zipCode"), is("12345"));
    assertThat(location.getString("cityName"), is("Samplecity"));
  }

  @Test
  public void postAddressWithoutVersionInfo(@ArquillianResource URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)new URL(url, "addresses/42").openConnection();
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    PrintWriter writer = new PrintWriter(connection.getOutputStream());
    for (String line: IOUtils.readLines(AddressV1.class.getResourceAsStream("address_v1_0.json"))) {
      writer.write(line);
    }
    writer.close();
    
    connection.connect();
    assertThat(connection.getResponseCode(), is(404));
  }
}
