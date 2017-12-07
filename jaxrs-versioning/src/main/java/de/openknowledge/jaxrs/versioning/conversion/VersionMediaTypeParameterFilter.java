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
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class VersionMediaTypeParameterFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private String parameterName;

  VersionMediaTypeParameterFilter(String parameterName) {
    this.parameterName = parameterName;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    MediaType mediaType = requestContext.getMediaType();
    String version = null;
    if (mediaType != null) {
      version = extractVersion(mediaType);
    } else {
      for (MediaType type : requestContext.getAcceptableMediaTypes()) {
        version = extractVersion(type);
        if (version != null) {
          break;
        }
      }
    }
    if (version != null) {
      Version.set(requestContext, version);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    String version = Version.get(requestContext);
    if (version != null) {
      MediaType mediaType = responseContext.getMediaType();
      if (mediaType != null) {
        Map<String, String> parameters = new HashMap<String, String>(mediaType.getParameters());
        parameters.put(parameterName, version);
        mediaType = new MediaType(mediaType.getType(), mediaType.getSubtype(), parameters);
        responseContext.getHeaders().putSingle("Content-Type", mediaType.toString());
      }
    }
  }

  private String extractVersion(MediaType mediaType) {
    return mediaType.getParameters().get(parameterName);
  }
}
