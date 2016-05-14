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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class CollectionElementValue extends VersionPropertyValue {

  private int index;
  
  public CollectionElementValue(VersionProperty property, int index, DefaultVersionContext context) {
    super(property, context);
    this.index = index;
  }

  public Object get() {
    Iterator<?> iterator = getCollection().iterator();
    for (int i = 0; i < index; i++) {
      if (iterator.hasNext()) {
        iterator.next();
      } else {
        return null;
      }
    }
    return iterator.hasNext()? iterator.next(): null;
  }
  
  public void set(Object value) {
    Collection<Object> collection = getCollection();
    if (collection instanceof List) {
      while (collection.size() <= index) {
        collection.add(null);
      }
      List<Object> list = (List<Object>)collection;
      list.set(index, value);
    } else if (!collection.contains(value)) {
      collection.add(value);
    }
  }

  private <T> Collection<T> getCollection() {
    Collection<T> collection = (Collection<T>)super.get();
    if (collection == null) {
      collection = createCollection();
    }
    return collection;
  }

  private <T> Collection<T> createCollection() {
    Class<?> collectionType = getProperty().getType();
    Collection<T> collection;
    if (collectionType.equals(SortedSet.class)) {
      collection = new TreeSet<T>();
    } else if (collectionType.equals(Set.class)) {
      collection = new HashSet<T>();
    } else {
      collection = new ArrayList<T>();
    }
    super.set(collection);
    return collection;
  }
}
