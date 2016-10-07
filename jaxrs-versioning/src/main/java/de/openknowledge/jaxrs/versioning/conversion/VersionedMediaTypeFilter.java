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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@PreMatching
@Provider
public class VersionedMediaTypeFilter implements ContainerRequestFilter {

  private static final String VERSION_PATTERN = "{version}";
  private static ConcurrentMap<MediaType, MediaType> customMediaTypes = new ConcurrentHashMap<MediaType, MediaType>();

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    MediaType mediaType = requestContext.getMediaType();
    if (mediaType == null) {
      return;
    }
    MediaType customMediaType = getMatchedMediaType(mediaType);
    if (customMediaType != null) {
      Version.set(requestContext, getVersion(mediaType, customMediaType));
      requestContext.getHeaders().putSingle("Content-Type", customMediaTypes.get(customMediaType).toString());
    }
  }

  static void register(MediaType customMediaType, MediaType mappedMediaType) {
    customMediaTypes.put(customMediaType, mappedMediaType);
  }

  private MediaType getMatchedMediaType(MediaType mediaType) {
    for (MediaType customMediaType: customMediaTypes.keySet()) {
      if (!customMediaType.getType().equals(mediaType.getType())) {
        continue;
      }
      int index = customMediaType.getSubtype().indexOf(VERSION_PATTERN);
      if (mediaType.getSubtype().startsWith(customMediaType.getSubtype().substring(0, index))
          && mediaType.getSubtype().endsWith(customMediaType.getSubtype().substring(index + VERSION_PATTERN.length()))) {
        return customMediaType;
      }
    }
    return null;
  }

  private String getVersion(MediaType mediaType, MediaType customMediaType) {
    int index = customMediaType.getSubtype().indexOf(VERSION_PATTERN);
    return mediaType.getSubtype().substring(index, index + mediaType.getSubtype().length() - customMediaType.getSubtype().length() + VERSION_PATTERN.length());
  }
}
