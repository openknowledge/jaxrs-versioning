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
        value = getPrevious(versionType, movedFrom, object);
        if (value != null) {
          versionProperty.set(object, value);
        }
      } else {
        setValue(versionType, movedFrom, object, value);
      }

    }

  }

  private void setValue(VersionType versionType, MovedFrom movedFrom, Object base, Object value) {
    VersionProperty property = getVersionProperty(versionType, movedFrom);
    property.set(base, value);
    movedFrom = property.getAnnotation(MovedFrom.class);
    if (movedFrom != null) {
      setValue(versionType, movedFrom, base, value);
    }
  }

  private VersionProperty getVersionProperty(VersionType versionType, MovedFrom movedFrom) {
    VersionProperty property = versionType.getProperty(movedFrom.value());
    if (property == null) {
      throw new IllegalArgumentException("@MoveFrom contains unknown property " + movedFrom.value());
    }
    return property;
  }


  private Object getPrevious(VersionType versionType, MovedFrom movedFrom, Object object) {
    VersionProperty property = getVersionProperty(versionType, movedFrom);
    Object value = property.get(object);
    if (value != null) {
      return value;
    }
    movedFrom = property.getAnnotation(MovedFrom.class);
    if (movedFrom == null) {
      //root
      return null;
    }
    return getPrevious(versionType, movedFrom, object);
  }
}
