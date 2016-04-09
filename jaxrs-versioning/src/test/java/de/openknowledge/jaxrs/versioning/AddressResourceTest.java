package de.openknowledge.jaxrs.versioning;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class AddressResourceTest {

  @Deployment
  public static WebArchive deployment() {
    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    return ShrinkWrap.create(WebArchive.class)
        .addClass(AddressResource.class)
        .addAsLibraries(pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .exportAsString()));
  }

  @Test
  public void getAddress() {
    
  }
}
