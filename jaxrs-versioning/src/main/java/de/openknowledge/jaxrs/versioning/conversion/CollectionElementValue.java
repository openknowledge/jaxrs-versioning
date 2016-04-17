package de.openknowledge.jaxrs.versioning.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CollectionElementValue extends VersionPropertyValue {

  private int index;
  
  public CollectionElementValue(VersionProperty property, int index, VersionContext context) {
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
    if (collection.size() <= index) {
      collection.add(value);
    } else if (collection instanceof List) {
      List<Object> list = (List<Object>)collection;
      list.set(index, value);
    } else if (collection.contains(value)) {
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
