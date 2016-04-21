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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public class DefaultParameterizedType implements ParameterizedType {

  private Type ownerType;
  private Type rawType;
  private Type[] actualTypeArguments;
  
  public DefaultParameterizedType(Type ownerType, Type rawType, Type... actualTypeArguments) {
    this.ownerType = ownerType;
    this.rawType = rawType;
    this.actualTypeArguments = actualTypeArguments.clone();
  }

  @Override
  public Type getOwnerType() {
    return ownerType;
  }
  
  @Override
  public Type getRawType() {
    return rawType;
  }

  @Override
  public Type[] getActualTypeArguments() {
    return actualTypeArguments.clone();
  }
}
