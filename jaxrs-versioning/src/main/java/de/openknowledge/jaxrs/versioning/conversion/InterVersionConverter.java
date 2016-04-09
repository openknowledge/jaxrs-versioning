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

import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class InterVersionConverter {

  private VersionTypeFactory factory;
  private CompatibilityMapper mapper;

  public InterVersionConverter(VersionTypeFactory factory, CompatibilityMapper mapper) {
    this.factory = factory;
    this.mapper = mapper;
  }

  public Object convertToLowerVersion(String targetVersion, Object source) {
    Class<?> sourceType = source.getClass();
    if (sourceType == Object.class) {
      throw new IllegalArgumentException("unsupported version: " + targetVersion);
    }
    VersionType versionType = factory.get(sourceType);
    SupportedVersion supportedVersion = versionType.getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalArgumentException("unsupported version: " + targetVersion + sourceType.getName());
    }
    if (targetVersion.equals(supportedVersion.version())) {
      return source;
    }
    return convertToLowerVersion(targetVersion, map(source, supportedVersion.previous()));
  }

  public Object convertToHigherVersion(Class<?> targetType, Object source, String sourceVersion) {
    if (targetType == Object.class) {
      throw new IllegalArgumentException("unsupported version: " + sourceVersion);
    }
    VersionType versionType = factory.get(targetType);
    SupportedVersion supportedVersion = versionType.getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalArgumentException("unsupported version: " + sourceVersion);
    }
    if (supportedVersion.version().equals(sourceVersion)) {
      return source;
    }
    Object previousVersion = convertToHigherVersion(supportedVersion.previous(), source, sourceVersion);
    return map(previousVersion, targetType);
  }

  private Object map(Object previous, Class<?> targetType) {
    VersionType targetVersionType = factory.get(targetType);
    Object target = targetVersionType.newInstance();
    VersionType previousVersionType = factory.get(previous.getClass());
    for (VersionProperty property: targetVersionType.getProperties()) {
      VersionProperty previousProperty = previousVersionType.getProperty(property.getName());
      if (previousProperty != null) {
        property.set(target, previousProperty.get(previous));
      }
    }
    mapper.map(target);
    return target;
  }
}
