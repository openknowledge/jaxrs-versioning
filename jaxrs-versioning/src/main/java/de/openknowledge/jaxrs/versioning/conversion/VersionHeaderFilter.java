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

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class VersionHeaderFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private String headerName;

  VersionHeaderFilter(String headerName) {
    this.headerName = headerName;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String version = requestContext.getHeaderString(headerName);
    System.out.println("###################Hello1");
    if (version != null) {
      Version.set(requestContext, version);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    String version = Version.get(requestContext);
    System.out.println("###################Hello2");
    if (version != null) {
      responseContext.getHeaders().putSingle(headerName, Version.get(requestContext));
    }
  }
}
