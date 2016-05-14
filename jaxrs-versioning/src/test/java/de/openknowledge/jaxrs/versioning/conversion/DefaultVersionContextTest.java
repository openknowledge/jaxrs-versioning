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
package de.openknowledge.jaxrs.versioning.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.openknowledge.jaxrs.versioning.conversion.DefaultVersionContext;
import de.openknowledge.jaxrs.versioning.model.AddressV1;
import de.openknowledge.jaxrs.versioning.model.CustomerV1;
import de.openknowledge.jaxrs.versioning.model.StreetV1;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class DefaultVersionContextTest {

  @Test
  public void emptyContext() {
    DefaultVersionContext context = new DefaultVersionContext();
    assertThat(context.getParentContext(), is(nullValue()));
    assertThat(context.getParent(), is(nullValue()));
    assertThat(context.getParent(CustomerV1.class), is(nullValue()));
  }

  @Test
  public void simpleContext() {
    CustomerV1 parent = new CustomerV1();
    DefaultVersionContext context = new DefaultVersionContext(parent);
    assertThat(context.getParentContext(), is(not(nullValue())));
    assertThat(context.getParentContext().getParent(), is(nullValue()));
    assertThat(context.getParent(), is((Object)parent));
    assertThat(context.getParent(CustomerV1.class), is(parent));
    assertThat(context.getParent(AddressV1.class), is(nullValue()));
  }

  @Test
  public void nestedContext() {
    CustomerV1 grandParent = new CustomerV1();
    AddressV1 parent = new AddressV1(new StreetV1("Samplestreet 1"), "12345 Samplecity");
    DefaultVersionContext context = new DefaultVersionContext(grandParent).getChildContext(parent);
    assertThat(context.getParentContext(), is(not(nullValue())));
    assertThat(context.getParentContext().getParent(), is((Object)grandParent));
    assertThat(context.getParent(), is((Object)parent));
    assertThat(context.getParent(CustomerV1.class), is(grandParent));
    assertThat(context.getParent(AddressV1.class), is(parent));
  }
}
