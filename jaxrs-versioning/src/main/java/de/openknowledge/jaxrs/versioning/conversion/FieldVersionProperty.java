package de.openknowledge.jaxrs.versioning.conversion;

import static org.apache.commons.lang3.Validate.notNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class FieldVersionProperty implements VersionProperty {

  private Field field;

  public FieldVersionProperty(Field field) {
    this.field = notNull(field);
  }

  @Override
  public Object get(Object base) {
    try {
      return field.get(base);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void set(Object base, Object value) {
    try {
      field.set(base, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> type) {
    return field.getAnnotation(type);
  }
}
