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

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.InterceptorContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import de.openknowledge.jaxrs.versioning.SupportedVersion;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
@Provider
public class MessageBodyConverter implements ReaderInterceptor, WriterInterceptor {

  private VersionTypeFactory factory = new VersionTypeFactory();
  private CompatibilityMapper mapper = new CompatibilityMapper(factory);
  private InterversionConverter converter = new InterversionConverter(factory, mapper);
  
  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    if (!isVersioningSupported(context)) {
      return context.proceed();
    }
    String sourceVersion = Version.get(context);
    Type targetType = context.getGenericType();
    Type sourceType = getVersionType(targetType, sourceVersion);
    context.setType(toClass(sourceType));
    context.setGenericType(sourceType);
    Object sourceObject = context.proceed();
    Object target;
    if (sourceObject instanceof Collection) {
      target = convertCollectionToHigherVersion(targetType, (Collection<?>)sourceObject, sourceVersion);
    } else {
      target = converter.convertToHigherVersion(toClass(targetType), sourceObject, sourceVersion);
    }
    context.setType(toClass(targetType));
    context.setGenericType(targetType);
    return target;
  }

  private Collection<?> convertCollectionToHigherVersion(Type targetCollectionType, Collection<?> source, String sourceVersion) {
    Collection<Object> target = new ArrayList<>();
    Class<?> targetType = getTypeArgument(targetCollectionType);
    for (Object sourceObject: source) {
      mapper.map(sourceObject);
      target.add(converter.convertToHigherVersion(targetType, sourceObject, sourceVersion));
    }
    return target;
  }

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    if (!isVersioningSupported(context)) {
      context.proceed();
      return;
    }
    String targetVersion = Version.get(context);
    Object source = context.getEntity();
    if (source instanceof Collection) {
      context.setEntity(convertCollectionToLowerVersion(targetVersion, (Collection<?>)source));
    } else {
      mapper.map(source);
      context.setEntity(converter.convertToLowerVersion(targetVersion, source));
    }
    Type targetType = getVersionType(context.getGenericType(), targetVersion);
    context.setType(toClass(targetType));
    context.setGenericType(targetType);
    context.proceed();
    Version.unset(context);
  }

  private Collection<?> convertCollectionToLowerVersion(String targetVersion, Collection<?> source) {
    Collection<Object> target = new ArrayList<>();
    for (Object sourceObject: source) {
      mapper.map(sourceObject);
      target.add(converter.convertToLowerVersion(targetVersion, sourceObject));
    }
    return target;
  }

  private boolean isVersioningSupported(InterceptorContext context) {
    Class<?> simpleType = context.getType();
    if (Collection.class.isAssignableFrom(context.getType())) {
      simpleType = getTypeArgument(context.getGenericType());
      if (simpleType == null) {
        return false;
      }
    }
    return simpleType.isAnnotationPresent(SupportedVersion.class);
  }

  private Type getVersionType(Type type, String version) {
    Class<?> rawType = toClass(type);
    if (Collection.class.isAssignableFrom(rawType)) {
      Type sourceType = getVersionType(getTypeArgument(type), version);
      return new DefaultParameterizedType(((ParameterizedType)type).getOwnerType(), rawType, sourceType);
    }
    SupportedVersion supportedVersion = toClass(type).getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalVersionException(version);
    }
    if (supportedVersion.version().equals(version)) {
      return type;
    } else {
      return getVersionType(supportedVersion.previous(), version);
    }
  }

  private Class<?> toClass(Type type) {
    if (type instanceof Class) {
      return (Class<?>)type;
    } else if (type instanceof ParameterizedType) {
      return toClass(((ParameterizedType)type).getRawType());
    } else {
      return null;
    }
  }

  private Class<?> getTypeArgument(Type type) {
    if (!(type instanceof ParameterizedType)) {
      return null;
    }
    ParameterizedType parameterizedType = (ParameterizedType)type;
    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
    return toClass(actualTypeArgument);
  }
}
