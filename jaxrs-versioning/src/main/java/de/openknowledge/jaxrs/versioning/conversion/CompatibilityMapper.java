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
    map(object, new VersionContext(object));
  }

  private void map(Object object, VersionContext context) {
    VersionType versionType = versionTypeFactory.get(object.getClass());

    for (VersionProperty versionProperty : versionType.getProperties()) {
      MovedFrom movedFrom = versionProperty.getAnnotation(MovedFrom.class);
      Added added = versionProperty.getAnnotation(Added.class);
      Removed removed = versionProperty.getAnnotation(Removed.class);

      if (movedFrom == null && added == null && removed == null) {
        continue;
      }
      Object value = versionProperty.get(object);
      if (value == null) {
        if (movedFrom != null) {
          value = getPrevious(versionType, movedFrom, context);
        } else if (added != null ) {
          value = getValue(versionProperty, added.defaultValue(), added.provider(), context);
        } else {// if (removed != null)
          value = getValue(versionProperty, removed.defaultValue(), removed.provider(), context);
        }
        // TODO check if more than one annotation is set
        if (value != null) {
          versionProperty.set(object, value);
        }
      } else {
        setValue(versionType, movedFrom, value, new VersionContext(object));
      }

    }

  }

  private Object getValue(VersionProperty property, String defaultValue, Class<? extends Provider> provider, VersionContext context) {
    // TODO check if both are set
    if (!defaultValue.equals("")) {
      return defaultValue;
    } else if (provider != Provider.class) {
      try {
        return provider.newInstance().get(context);
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    } else {
      Object value = versionTypeFactory.get(property.getType()).newInstance();
      map(value, context.getChildContext(value));
      return value;
    }
  }

  private void setValue(VersionType versionType, MovedFrom movedFrom, Object value, VersionContext context) {
    VersionPropertyValue propertyValue = getPropertyValue(versionType, movedFrom, context);
    propertyValue.set(value);
    movedFrom = propertyValue.getAnnotation(MovedFrom.class);
    if (movedFrom != null) {
      setValue(versionType, movedFrom, value, context);
    }
  }

  private VersionPropertyValue getPropertyValue(VersionType versionType, MovedFrom movedFrom, VersionContext context) {
    return getPropertyValue(versionType, movedFrom.value().split("/"), 0, context);
  }

  private VersionPropertyValue getPropertyValue(VersionType versionType, String[] pathElements, int index, VersionContext context) {
    if (pathElements[index].equals("..")) {
      context = context.getParentContext();
      return getPropertyValue(versionTypeFactory.get(context.getParent().getClass()), pathElements, index + 1, context);
    }
    VersionProperty property = versionType.getProperty(pathElements[index]);
    if (property == null) {
      throw new IllegalArgumentException("@MoveFrom contains unknown property " + pathElements[index]);
    }
    if (pathElements.length == index + 1) {
      return new VersionPropertyValue(property, context.getParent());
    }
    Object value = property.get(context.getParent());
    if (value == null) {
      value = versionTypeFactory.get(property.getType()).newInstance();
    }
    return getPropertyValue(versionTypeFactory.get(property.getType()), pathElements, index + 1, context.getChildContext(value));
  }


  private Object getPrevious(VersionType versionType, MovedFrom movedFrom, VersionContext context) {
    VersionPropertyValue propertyValue = getPropertyValue(versionType, movedFrom, context);
    Object value = propertyValue.get();
    if (value != null) {
      return value;
    }
    movedFrom = propertyValue.getAnnotation(MovedFrom.class);
    if (movedFrom == null) {
      //root
      return null;
    }
    return getPrevious(versionType, movedFrom, context);
  }
}
