package de.openknowledge.jaxrs.versioning.conversion;

import java.lang.annotation.Annotation;


public interface VersionProperty {

  Object get(Object base);
  void set(Object base, Object value);
  <A extends Annotation> A getAnnotation(Class<A> type);
}
