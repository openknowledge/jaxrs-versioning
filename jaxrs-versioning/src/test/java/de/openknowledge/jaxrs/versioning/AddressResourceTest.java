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
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.openknowledge.jaxrs.versioning.conversion.MessageBodyConverter;
import de.openknowledge.jaxrs.versioning.model.AddressV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class AddressResourceTest {

  @Deployment
  public static WebArchive deployment() {
    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    return ShrinkWrap.create(WebArchive.class)
        .addClasses(SampleApplication.class, AddressResource.class)
        .addPackage(AddressV1.class.getPackage())
        .addPackage(MessageBodyConverter.class.getPackage())
        .addAsLibraries(pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .addAsLibraries(pom.resolve("org.json:json").withTransitivity().asFile())
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .exportAsString()));
  }

  @Test
  public void getAddressV10(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("name"), is("Samplestreet"));
    assertThat(street.getString("number"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Test
  public void postAddressV10(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_0.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("name"), is("Samplestreet"));
    assertThat(street.getString("number"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void getAddressV11(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("streetName"), is("Samplestreet"));
    assertThat(street.getString("streetNumber"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void postAddressV11(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_1.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("streetName"), is("Samplestreet"));
    assertThat(street.getString("streetNumber"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void getAddressV12(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("streetName"), is("Samplestreet"));
    assertThat(street.getString("houseNumber"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void postAddressV12(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_2.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("streetName"), is("Samplestreet"));
    assertThat(street.getString("houseNumber"), is("1"));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void getAddressV13(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(street.getString("addressLine2"), is(""));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void postAddressV13(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_3.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(street.getString("addressLine2"), is(""));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  @Ignore
  @Test
  public void getAddressV14(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    assertThat(address.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(address.getString("addressLine2"), is(""));
    assertThat(address.getString("zipCode"), is("12345"));
    assertThat(address.getString("cityName"), is("Samplecity"));
  }

  @Ignore
  @Test
  public void postAddressV14(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_4.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    assertThat(address.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(address.getString("addressLine2"), is(""));
    assertThat(address.getString("zipCode"), is("12345"));
    assertThat(address.getString("cityName"), is("Samplecity"));
  }

  @Ignore
  @Test
  public void getAddressV15(@ArquillianResource URL url) throws IOException {
    JSONObject address = new JSONObject(IOUtils.toString(new URL(url, "v1/addresses/42").openStream()));
    JSONObject location = address.getJSONObject("location");
    assertThat(address.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(address.getString("addressLine2"), is(""));
    assertThat(location.getString("zipCode"), is("12345"));
    assertThat(location.getString("cityName"), is("Samplecity"));
  }

  @Ignore
  @Test
  public void postAddressV15(@ArquillianResource URL url) throws IOException {
    InputStream result = post(new URL(url, "v1/addresses/42"), "address_v1_5.json");
    
    JSONObject address = new JSONObject(IOUtils.toString(result));
    JSONObject street = address.getJSONObject("street");
    assertThat(street.getString("addressLine1"), is("Samplestreet 1"));
    assertThat(street.getString("addressLine2"), is(""));
    assertThat(address.getString("city"), is("12345 Samplecity"));
  }

  private InputStream post(URL url, String resource) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    PrintWriter writer = new PrintWriter(connection.getOutputStream());
    for (String line: IOUtils.readLines(AddressV1.class.getResourceAsStream(resource))) {
      writer.write(line);
    }
    writer.close();
    return connection.getInputStream();
  }
}
