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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
    return convertToLowerVersion(targetVersion, map(source, supportedVersion.previous(), new VersionContext()));
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
      mapper.map(source);
      return source;
    }
    Object previousVersion = convertToHigherVersion(supportedVersion.previous(), source, sourceVersion);
    return map(previousVersion, targetType, new VersionContext());
  }

  private Object map(Object previous, Class<?> targetType, VersionContext context) {
    VersionType targetVersionType = factory.get(targetType);
    Object target = null;
    VersionType previousVersionType = factory.get(previous.getClass());
    for (VersionProperty targetProperty: targetVersionType.getProperties()) {
      VersionProperty previousProperty = previousVersionType.getProperty(targetProperty.getName());
      if (match(targetProperty, previousProperty)) {
        if (target == null) {
          target = targetVersionType.newInstance();
          context = context.getChildContext(target);
        }
        if ((targetProperty.isSimple() && previousProperty.isSimple())
            || targetProperty.getType().isAssignableFrom(previousProperty.getType())) {
          targetProperty.set(target, previousProperty.get(previous));
        } else {
          targetProperty.set(target, map(previousProperty.get(previous), targetProperty.getType(), context));
        }
      }
    }
    if (target != null) {
      mapper.map(target, context);
    }
    return target;
  }

  private boolean match(VersionProperty property, VersionProperty previousProperty) {
    return match(property, previousProperty, new HashSet<Pair<VersionType,VersionType>>());
  }

  private boolean match(VersionProperty property, VersionProperty previousProperty, Set<Pair<VersionType, VersionType>> visited) {
    if (previousProperty == null) {
      return false;
    }
    if (property.isSimple() && previousProperty.isSimple()) {
      return true;
    }
    if (property.isSimple() || previousProperty.isSimple()) {
      return false;
    }
    return match(factory.get(property.getType()), factory.get(previousProperty.getType()), visited);
  }

  private boolean match(VersionType versionType, VersionType previousVersionType, Set<Pair<VersionType, VersionType>> visited) {
    ImmutablePair<VersionType, VersionType> pair = ImmutablePair.of(versionType, previousVersionType);
    if (visited.contains(pair)) {
      return true;
    }
    visited.add(pair);
    for (VersionProperty property: versionType.getProperties()) {
      if (match(property, previousVersionType.getProperty(property.getName()), visited)) {
        return true;
      }
    }
    return false;
  }
}
