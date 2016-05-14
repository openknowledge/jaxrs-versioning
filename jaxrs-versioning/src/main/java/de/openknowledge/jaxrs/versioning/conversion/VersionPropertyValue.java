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

import java.lang.annotation.Annotation;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class VersionPropertyValue {

  private VersionProperty property;
  private DefaultVersionContext context;

  public VersionPropertyValue(VersionProperty property, DefaultVersionContext context) {
    this.property = property;
    this.context = context;
  }

  public VersionProperty getProperty() {
    return property;
  }

  public boolean isDefault() {
    return property.isDefault(context.getParent());
  }

  public Object get() {
    return property.get(context.getParent());
  }
  
  public void set(Object value) {
    property.set(context.getParent(), value);
  }

  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    return property.getAnnotation(annotationType);
  }

  public DefaultVersionContext getContext() {
    return context;
  }
}
