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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class InterversionConverter {

  private VersionTypeFactory factory;
  private CompatibilityMapper mapper;

  public InterversionConverter(VersionTypeFactory factory, CompatibilityMapper mapper) {
    this.factory = factory;
    this.mapper = mapper;
  }

  public <T> T convertToLowerVersion(String targetVersion, Object source) {
    Class<?> sourceType = source.getClass();
    SupportedVersion supportedVersion = sourceType.getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalVersionException(targetVersion);
    }
    if (targetVersion.equals(supportedVersion.version())) {
      return (T)source;
    }
    if (supportedVersion.previous() == Object.class) {
      throw new IllegalVersionException(targetVersion);
    }
    return convertToLowerVersion(targetVersion, map(source, supportedVersion.previous(), new DefaultVersionContext()));
  }

  public <T> T convertToHigherVersion(Class<T> targetType, Object source, String sourceVersion) {
    SupportedVersion supportedVersion = targetType.getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalVersionException(sourceVersion);
    }
    if (supportedVersion.version().equals(sourceVersion)) {
      mapper.map(source);
      return targetType.cast(source);
    }
    if (supportedVersion.previous() == Object.class) {
      throw new IllegalVersionException(sourceVersion);
    }
    Object previousVersion = convertToHigherVersion(supportedVersion.previous(), source, sourceVersion);
    return map(previousVersion, targetType, new DefaultVersionContext());
  }

  private <T> T map(Object previous, Class<T> targetType, DefaultVersionContext context) {
    VersionType<T> targetVersionType = factory.get(targetType);
    T target = null;
    VersionType<?> previousVersionType = factory.get(previous.getClass());
    for (VersionProperty targetProperty: targetVersionType.getProperties()) {
      VersionProperty previousProperty = previousVersionType.getProperty(targetProperty.getName());
      if (match(targetProperty, previousProperty)) {
        if (target == null) {
          target = targetVersionType.newInstance();
          context = context.getChildContext(target);
        }
        if (targetProperty.isCollection()) {
          Collection<?> collection = (Collection<?>)previousProperty.get(previous);
          if (collection == null) {
            continue;
          }
          Iterator<?> source = collection.iterator();
          for (int i = 0; source.hasNext(); i++) {
            if (targetProperty.isCollectionOfSimpleTypes()
                || targetProperty.getCollectionElementType().isAssignableFrom(previousProperty.getCollectionElementType())) {
              new CollectionElementValue(targetProperty, i, context).set(source.next());
            } else {
              new CollectionElementValue(targetProperty, i, context).set(map(source.next(), targetProperty.getCollectionElementType(), context));
            }
          }
        } else if (targetProperty.isSimple() || targetProperty.getType().isAssignableFrom(previousProperty.getType())) {
          targetProperty.set(target, previousProperty.get(previous));
        } else if (!previousProperty.isDefault(previous)) {
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
    return match(property, previousProperty, new HashSet<Pair<VersionType<?>, VersionType<?>>>());
  }

  private boolean match(VersionProperty property, VersionProperty previousProperty, Set<Pair<VersionType<?>, VersionType<?>>> visited) {
    if (previousProperty == null) {
      return false;
    }
    if (property.isSimple() && previousProperty.isSimple()) {
      return true;
    }
    if (property.isSimple() || previousProperty.isSimple()) {
      return false;
    }
    if (property.isCollectionOfSimpleTypes() && previousProperty.isCollectionOfSimpleTypes()) {
      return true;
    }
    if (property.isCollection() && previousProperty.isCollection()) {
      return match(factory.get(property.getCollectionElementType()), factory.get(previousProperty.getCollectionElementType()), visited);
    }
    if (property.isCollection() || previousProperty.isCollection()) {
      return false;
    }
    return match(factory.get(property.getType()), factory.get(previousProperty.getType()), visited);
  }

  private boolean match(VersionType<?> versionType, VersionType<?> previousVersionType, Set<Pair<VersionType<?>, VersionType<?>>> visited) {
    Pair<?, ?> pair = ImmutablePair.of(versionType, previousVersionType);
    if (visited.contains(pair)) {
      return true;
    }
    visited.add((Pair<VersionType<?>, VersionType<?>>)pair);
    for (VersionProperty property: versionType.getProperties()) {
      if (match(property, previousVersionType.getProperty(property.getName()), visited)) {
        return true;
      }
    }
    return false;
  }
}
