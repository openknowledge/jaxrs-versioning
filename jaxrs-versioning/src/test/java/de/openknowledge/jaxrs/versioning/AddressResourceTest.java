package de.openknowledge.jaxrs.versioning;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import de.openknowledge.jaxrs.versioning.model.AddressV1;

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
        .addAsLibraries(pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .exportAsString()));
  }

  @Test
  public void getAddress(@ArquillianResource URL url) throws IOException {
    String test = IOUtils.toString(new URL(url, "v1/addresses/42").openStream());
    JSONObject address = new JSONObject(test);
    JSONObject street = (JSONObject)address.get("street");
    assertThat(street.get("name").toString(), is("Samplestreet"));
    assertThat(street.get("number").toString(), is("1"));
    assertThat(address.get("city").toString(), is("12345 Samplecity"));
  }
}
