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

import java.util.ArrayList;
import java.util.List;

import de.openknowledge.jaxrs.versioning.VersionContext;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class DefaultVersionContext implements VersionContext {

  private String propertyName;
  private List<Object> parents;
  
  public DefaultVersionContext() {
    parents = new ArrayList<>();
  }

  public DefaultVersionContext(Object parent) {
    this();
    parents.add(parent);
  }

  private DefaultVersionContext(List<Object> stack) {
    this.parents = stack;
  }

  public Object getParent() {
    if (parents.isEmpty()) {
      return null;
    }
    return parents.get(parents.size() - 1);
  }

  public <T> T getParent(Class<T> type) {
    for (int i = parents.size() - 1; i >= 0; i--) {
      Object parent = parents.get(i);
      if (type.isInstance(parent)) {
        return (T)parent;
      }
    }
    return null;
  }
  
  public String getPropertyName() {
    return propertyName;
  }

  void setPropertyName(String name) {
    propertyName = name;
  }

  DefaultVersionContext getParentContext() {
    if (parents.isEmpty()) {
      return null;
    }
    return new DefaultVersionContext(parents.subList(0, parents.size() - 1));
  }

  DefaultVersionContext getChildContext(Object base) {
    List<Object> newParents = new ArrayList<Object>(parents);
    newParents.add(base);
    return new DefaultVersionContext(newParents);
  }
}



