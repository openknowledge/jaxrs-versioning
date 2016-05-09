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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class FieldVersionTypeTest {

  private char characterValue;
  private boolean booleanValue;
  private boolean trueValue = true;
  private double doubleValue;
  private byte byteValue;
  private Integer integerValue = 0;
  private Collection<String> collectionOfSimpleTypes;
  private Collection<FieldVersionTypeTest> collection;
  
  private FieldVersionType<FieldVersionTypeTest> fieldVersionType;
  
  @Before
  public void createType() {
    fieldVersionType = new FieldVersionType<FieldVersionTypeTest>(FieldVersionTypeTest.class, null);
  }

  @Test
  public void fieldVersionPropertyIsCollection() {
    assertThat(fieldVersionType.getProperty("booleanValue").isCollection(), is(false));
    assertThat(fieldVersionType.getProperty("collectionOfSimpleTypes").isCollection(), is(true));
    assertThat(fieldVersionType.getProperty("collection").isCollection(), is(true));
  }

  @Test
  public void fieldVersionPropertyIsCollectionOfSimpleTypes() {
    assertThat(fieldVersionType.getProperty("booleanValue").isCollectionOfSimpleTypes(), is(false));
    assertThat(fieldVersionType.getProperty("collectionOfSimpleTypes").isCollectionOfSimpleTypes(), is(true));
    assertThat(fieldVersionType.getProperty("collection").isCollectionOfSimpleTypes(), is(false));
  }

  @Test
  public void fieldVersionPropertyIsDefault() {
    assertThat(fieldVersionType.getProperty("booleanValue").isDefault(this), is(true));
    assertThat(fieldVersionType.getProperty("trueValue").isDefault(this), is(false));
    assertThat(fieldVersionType.getProperty("characterValue").isDefault(this), is(true));
    assertThat(fieldVersionType.getProperty("doubleValue").isDefault(this), is(true));
    assertThat(fieldVersionType.getProperty("byteValue").isDefault(this), is(true));
    assertThat(fieldVersionType.getProperty("integerValue").isDefault(this), is(false));
    assertThat(fieldVersionType.getProperty("collectionOfSimpleTypes").isDefault(this), is(true));
    assertThat(fieldVersionType.getProperty("collection").isDefault(this), is(true));
  }

  @Test
  public void toStringContainsName() {
    assertThat(fieldVersionType.toString(), containsString("FieldVersionTypeTest"));
    assertThat(fieldVersionType.getProperty("booleanValue").toString(), containsString("booleanValue"));
  }
}
