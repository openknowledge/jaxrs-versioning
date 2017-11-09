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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@PreMatching
@Provider
public class VersionedMediaTypeFilter implements ContainerRequestFilter {

  private static final String VERSION_PATTERN = "{version}";
  private static ConcurrentMap<VersionedMediaType, MediaType> customMediaTypes = new ConcurrentHashMap<VersionedMediaType, MediaType>();

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    MediaType mediaType = requestContext.getMediaType();
    VersionedMediaType customMediaType = null;
    if (mediaType != null) {
      customMediaType = getMatchedMediaType(mediaType);
    } else {
      for (MediaType type : requestContext.getAcceptableMediaTypes()) {
        customMediaType = getMatchedMediaType(type);
        if (customMediaType != null) {
          mediaType = type;
          List<String> headers = new LinkedList<>();
          // TomEE seems to use the first entry of accept headers to determine the content-type
          // we want to preserve the original headers to be able to use them in other context
          headers.add(customMediaTypes.get(customMediaType).toString());
          headers.addAll(requestContext.getHeaders().get(HttpHeaders.ACCEPT));
          requestContext.getHeaders().put(HttpHeaders.ACCEPT, headers);
          break;
        }
      }
    }
    if (mediaType != null && customMediaType != null) {
      Version.set(requestContext, customMediaType.extractVersionFrom(mediaType));
      requestContext.getHeaders().putSingle("Content-Type", customMediaTypes.get(customMediaType).toString());
    }
  }

  static void register(String customMediaType, MediaType mappedMediaType) {
    customMediaTypes.put(new VersionedMediaType(customMediaType), mappedMediaType);
  }

  private VersionedMediaType getMatchedMediaType(MediaType mediaType) {
    for (VersionedMediaType customMediaType: customMediaTypes.keySet()) {
      if (customMediaType.matches(mediaType)) {
        return customMediaType;
      }
    }
    return null;
  }

  private static class VersionedMediaType {

    private final String mediaType;
    private transient String type;
    private transient String subtype;
    private transient String subtypePrefix;
    private transient String subtypeSuffix;

    public VersionedMediaType(String type) {
      if (type == null) {
        throw new IllegalArgumentException("media type is required");
      }
      this.mediaType = type;
    }

    public boolean matches(MediaType mediaType) {
      if (!isInitialized()) {
        initializeTypes();
      }
      if (!type.equals(mediaType.getType())) {
        return false;
      }
      return mediaType.getSubtype().startsWith(subtypePrefix) && mediaType.getSubtype().endsWith(subtypeSuffix);
    }

    public String extractVersionFrom(MediaType mediaType) {
      if (!isInitialized()) {
        initializeTypes();
      }
      int index = subtype.indexOf(VERSION_PATTERN);
      return mediaType.getSubtype().substring(index, index + mediaType.getSubtype().length() - subtype.length() + VERSION_PATTERN.length());
    }

    @Override
    public int hashCode() {
      return mediaType.hashCode();
    }

    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }
      if (!(object instanceof VersionedMediaType)) {
        return false;
      }
      return mediaType.equals(((VersionedMediaType)object).mediaType);
    }

    private boolean isInitialized() {
      return type != null;
    }

    private void initializeTypes() {
      int slashIndex = mediaType.indexOf('/');
      type = mediaType.substring(0, slashIndex);
      subtype = mediaType.substring(slashIndex + 1);
      int versionPatternIndex = subtype.indexOf(VERSION_PATTERN);
      subtypePrefix = subtype.substring(0, versionPatternIndex);
      subtypeSuffix = subtype.substring(versionPatternIndex + VERSION_PATTERN.length());
    }
  }
}
