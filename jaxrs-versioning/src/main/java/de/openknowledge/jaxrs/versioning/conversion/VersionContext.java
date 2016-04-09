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

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class VersionContext {

  private List<Object> parents;
  
  public VersionContext(Object parent) {
    parents = new ArrayList<>();
    parents.add(parent);
  }

  private VersionContext(List<Object> stack) {
    this.parents = stack.subList(0, stack.size());
  }

  public Object getParent() {
    return parents.get(parents.size() - 1);
  }

  VersionContext getParentContext() {
    return new VersionContext(parents.subList(0, parents.size() - 1));
  }

  VersionContext getChildContext(Object base) {
    List<Object> newParents = parents;
    newParents.add(base);
    parents = newParents.subList(0, newParents.size() - 1);
    return new VersionContext(newParents);
  }
}
