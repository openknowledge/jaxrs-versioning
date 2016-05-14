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

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import de.openknowledge.jaxrs.versioning.Added;
import de.openknowledge.jaxrs.versioning.MovedFrom;
import de.openknowledge.jaxrs.versioning.Provider;
import de.openknowledge.jaxrs.versioning.Removed;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class CompatibilityMapper {

  private VersionTypeFactory versionTypeFactory;

  CompatibilityMapper(VersionTypeFactory factory) {
    versionTypeFactory = factory;
  }

  public void map(Object object) {
    map(object, new DefaultVersionContext(object));
  }

  public void map(Object object, DefaultVersionContext context) {
    VersionType<?> versionType = versionTypeFactory.get(object.getClass());

    for (VersionProperty versionProperty : versionType.getProperties()) {
      MovedFrom movedFrom = versionProperty.getAnnotation(MovedFrom.class);
      Added added = versionProperty.getAnnotation(Added.class);

      if (movedFrom == null && added == null) {
        if (versionProperty.isCollection() && !versionProperty.isCollectionOfSimpleTypes()) {
          if (!versionProperty.isDefault(object)) {
            Collection<?> collection = (Collection<?>)versionProperty.get(object);
            for (Object entry: collection) {
              map(entry, context.getChildContext(entry));
            }
          }
        } else if (!versionProperty.isSimple() && !versionProperty.isCollectionOfSimpleTypes()) {
          Object value;
          if (!versionProperty.isDefault(object)) {
            value = versionProperty.get(object);
          } else {
            value = versionTypeFactory.get(versionProperty.getType()).newInstance();
            versionProperty.set(object, value);
          }
          map(value, context.getChildContext(value));
        }
        continue;
      }
      if (versionProperty.isDefault(object)) {
        updateDependentValues(new VersionPropertyValue(versionProperty, context));
      } else {
        Object value = versionProperty.get(object);
        setDependentValues(versionType, movedFrom, added, versionProperty, value, context);
        if (!versionProperty.isSimple()) {
          map(value, context.getChildContext(value));
        }
      }
    }
  }

  private void updateDependentValues(VersionPropertyValue propertyValue) {
    DefaultVersionContext dependentContext = propertyValue.getContext();
    VersionType<?> dependentType = versionTypeFactory.get(dependentContext.getParent().getClass());

    MovedFrom movedFrom = propertyValue.getAnnotation(MovedFrom.class);
    if (movedFrom != null) {
      updateDependentValues(dependentType, propertyValue, movedFrom, dependentContext);
      if (!propertyValue.isDefault()) {
        return;
      }
    }
    String defaultValue = StringUtils.EMPTY;
    Class<? extends Provider> provider = Provider.class;

    Added added = propertyValue.getAnnotation(Added.class);
    if (added != null) {
      for (int i = 0; i < added.value().length; i++) {
        CollectionElementValue dependentValue = new CollectionElementValue(propertyValue.getProperty(), i, propertyValue.getContext());
        updateDependentValues(dependentType, dependentValue, added.value()[i], dependentContext);
      }
      if (!propertyValue.isDefault()) {
        return;
      }
      for (String dependency : added.dependsOn()) {
        VersionPropertyValue dependentValue = getPropertyValue(dependentType, dependency, dependentContext);
        if (dependentValue.isDefault()) {
          updateDependentValues(dependentValue);
        }
        if (dependentValue.isDefault()) {
          // we can't call the provider for now. Will be done later
          return;
        }
      }
      defaultValue = added.defaultValue();
      provider = added.provider();
    }
    setValue(propertyValue, provider, defaultValue, dependentContext);
  }

  private void updateDependentValues(VersionType<?> dependentType, VersionPropertyValue propertyValue, MovedFrom movedFrom, DefaultVersionContext dependentContext) {
    VersionPropertyValue dependentValue = getPropertyValue(dependentType, movedFrom.value(), dependentContext);
    if (dependentValue.isDefault()) {
      updateDependentValues(dependentValue);
    }
    if (!dependentValue.isDefault()) {
      propertyValue.set(dependentValue.get());
    }
  }

  private void setValue(VersionPropertyValue value, Class<? extends Provider> provider, String defaultValue, DefaultVersionContext context) {
    if (!provider.equals(Provider.class)) {
      Provider<?> providerInstance = (Provider<?>)versionTypeFactory.get(provider).newInstance();
      try {
        context.setPropertyName(value.getProperty().getName());
        value.set(providerInstance.get(context));
      } finally {
        context.setPropertyName(null);
      }
    } else if (!defaultValue.isEmpty()) {
      value.set(defaultValue);
    } else if (!value.getProperty().isSimple() && !value.getProperty().isCollection()) {
      Object instance = versionTypeFactory.get(value.getProperty().getType()).newInstance();
      value.set(instance);
      map(instance, context.getChildContext(instance));
    }
  }

  private void setDependentValues(VersionType<?> versionType, MovedFrom movedFrom, Added added, VersionProperty property, Object value, DefaultVersionContext context) {
    if (movedFrom != null) {
      setDependentValues(versionType, movedFrom.value(), value, context);
    }
    if (added != null) {
      for (int i = 0; i < added.value().length; i++) {
        setDependentValues(versionType, added.value()[i].value(), new CollectionElementValue(property, i, context).get(), context);
      }
      for (String dependency: added.dependsOn()) {
        setRemovedValues(versionType, dependency, context);
      }
    }
  }

  private void setDependentValues(VersionType<?> versionType, String path, Object value, DefaultVersionContext context) {
    VersionPropertyValue propertyValue = getPropertyValue(versionType, path, context);
    propertyValue.set(value);
    MovedFrom movedFrom = propertyValue.getAnnotation(MovedFrom.class);
    Added added = propertyValue.getAnnotation(Added.class);
    VersionType<?> propertyParentType = versionTypeFactory.get(propertyValue.getContext().getParent().getClass()); 
    setDependentValues(propertyParentType, movedFrom, added, propertyValue.getProperty(), value, propertyValue.getContext());
  }

  private void setRemovedValues(VersionType<?> versionType, String dependsOn, DefaultVersionContext context) {
    VersionPropertyValue propertyValue = getPropertyValue(versionType, dependsOn, context);
    if (propertyValue.isDefault()) {
      Removed removed = propertyValue.getAnnotation(Removed.class);
      if (removed != null) {
        for (String dependency: removed.isDependencyOf()) {
          if (getPropertyValue(versionType, dependency, context).isDefault()) {
            // we can't call the provider for now. Will be done later
            return;
          }
        }
        setValue(propertyValue, removed.provider(), removed.defaultValue(), context);
        if (!propertyValue.isDefault()) {
          Object value = propertyValue.get();
          MovedFrom movedFrom = propertyValue.getAnnotation(MovedFrom.class);
          Added added = propertyValue.getAnnotation(Added.class);
          setDependentValues(versionType, movedFrom, added, propertyValue.getProperty(), value, context);
        }
      }
    }
  }

  private VersionPropertyValue getPropertyValue(VersionType<?> versionType, String path, DefaultVersionContext context) {
    return getPropertyValue(versionType, path.split("/"), 0, context);
  }

  private VersionPropertyValue getPropertyValue(VersionType<?> versionType, String[] pathElements, int index, DefaultVersionContext context) {
    String propertyName = pathElements[index];
    if (propertyName.equals("..")) {
      context = context.getParentContext();
      return getPropertyValue(versionTypeFactory.get(context.getParent().getClass()), pathElements, index + 1, context);
    }
    int collectionIndex = -1;
    if (isCollectionProperty(propertyName)) {
      int bracketIndex = propertyName.lastIndexOf('[');
      propertyName = propertyName.substring(0, bracketIndex);
      collectionIndex = Integer.parseInt(pathElements[index].substring(bracketIndex + 1, pathElements[index].length() - 1));
      if (collectionIndex < 0) {
        throw new IndexOutOfBoundsException("index in " + pathElements[index] + " may not be negative");
      }
    }
    VersionProperty property = versionType.getProperty(propertyName);
    if (property == null) {
      throw new IllegalArgumentException("@MoveFrom contains unknown property " + propertyName);
    }
    if (pathElements.length == index + 1) {
      return createPropertyValue(property, collectionIndex, context);
    }
    Object value = null;
    if (isCollection(collectionIndex)) {
      CollectionElementValue collectionElementValue = new CollectionElementValue(property, collectionIndex, context);
      value = collectionElementValue.get();
    } else if (!property.isDefault(context.getParent())) {
      value = property.get(context.getParent());
    }
    if (value == null) {
      value = versionTypeFactory.get(property.getType()).newInstance();
    }
    return getPropertyValue(versionTypeFactory.get(property.getType()), pathElements, index + 1, context.getChildContext(value));
  }
  
  private boolean isCollectionProperty(String propertyName) {
    return propertyName.endsWith("]");
  }

  private VersionPropertyValue createPropertyValue(VersionProperty property, int collectionIndex, DefaultVersionContext context) {
    if (isCollection(collectionIndex)) {
      return new CollectionElementValue(property, collectionIndex, context);
    } else {
      return new VersionPropertyValue(property, context);
    }
  }

  private boolean isCollection(int collectionIndex) {
    return collectionIndex > -1;
  }
}
