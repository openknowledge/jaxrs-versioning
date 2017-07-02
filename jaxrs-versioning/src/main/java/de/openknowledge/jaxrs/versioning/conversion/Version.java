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

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.ext.InterceptorContext;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class Version {

  private static final String VERSION_PROPERTY_NAME = Version.class.getName().toLowerCase();

  private static final ThreadLocal<String> VERSION = new ThreadLocal<String>();

  public static String get(InterceptorContext context) {
    return VERSION.get();
//    return (String)context.getProperty(VERSION_PROPERTY_NAME);
  }

  public static String get(ContainerRequestContext context) {
    return VERSION.get();
//    return (String)context.getProperty(VERSION_PROPERTY_NAME);
  }
  
  public static void set(ContainerRequestContext context, String version) {
    VERSION.set(version);
//    context.setProperty(VERSION_PROPERTY_NAME, version);
  }

  public static void unset(InterceptorContext context) {
    VERSION.remove();
//    context.removeProperty(VERSION_PROPERTY_NAME);
  }
}
