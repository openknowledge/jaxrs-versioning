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

import de.openknowledge.jaxrs.versioning.MovedFrom;

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
    VersionType versionType = versionTypeFactory.get(object.getClass());

    for (VersionProperty versionProperty : versionType.getProperties()) {
      MovedFrom movedFrom = versionProperty.getAnnotation(MovedFrom.class);

      if (movedFrom == null) {
        continue;
      }
      Object value = versionProperty.get(object);
      if (value == null) {
        value = getPrevious(versionType, movedFrom, new VersionContext(object));
        if (value != null) {
          versionProperty.set(object, value);
        }
      } else {
        setValue(versionType, movedFrom, value, new VersionContext(object));
      }

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
      return getPropertyValue(versionType, pathElements, index + 1, context.getParentContext());
    }
    VersionProperty property = versionType.getProperty(pathElements[index]);
    if (property == null) {
      throw new IllegalArgumentException("@MoveFrom contains unknown property " + pathElements[index]);
    }
    if (pathElements.length == index + 1) {
      return new VersionPropertyValue(property, context.getParent());
    }
    Object value = property.get(context.getParent());
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
