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

import java.lang.annotation.Annotation;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public interface VersionProperty {

  String getName();
  boolean isDefault(Object base);
  Object get(Object base);
  void set(Object base, Object value);
  <A extends Annotation> A getAnnotation(Class<A> type);
  Class<?> getType();
  Class<?> getCollectionElementType();
  boolean isSimple();
  boolean isCollection();
  boolean isCollectionOfSimpleTypes();
}
