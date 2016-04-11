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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
  private InterVersionConverter converter = new InterVersionConverter(factory, mapper);

  @Context
  private UriInfo uriInfo;
  
  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    if (!context.getType().isAnnotationPresent(SupportedVersion.class)) {
      return context.proceed();
    }
    String sourceVersion = getVersion();
    Class<?> targetType = context.getType();
    Class<?> sourceType = getSourceType(targetType, sourceVersion);
    context.setType(sourceType);
    context.setGenericType(sourceType);
    Object sourceObject = context.proceed();
    mapper.map(sourceObject);
    Object target = converter.convertToHigherVersion(targetType, sourceObject, sourceVersion);
    context.setType(targetType);
    context.setGenericType(targetType);
    return target;
  }

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    if (!context.getType().isAnnotationPresent(SupportedVersion.class)) {
      context.proceed();
      return;
    }
    String targetVersion = getVersion();
    Object source = context.getEntity();
    mapper.map(source);
    Object target = converter.convertToLowerVersion(targetVersion, source);
    context.setEntity(target);
    context.proceed();
  }

  private String getVersion() {
    return uriInfo.getPathParameters().get("version").iterator().next();
  }

  private Class<?> getSourceType(Class<?> type, String version) {
    SupportedVersion supportedVersion = factory.get(type).getAnnotation(SupportedVersion.class);
    if (supportedVersion == null) {
      throw new IllegalArgumentException("unsupported version " + version + " for type " + type);
    }
    if (supportedVersion.version().equals(version)) {
      return type;
    } else {
      return getSourceType(supportedVersion.previous(), version);
    }
  }
}
