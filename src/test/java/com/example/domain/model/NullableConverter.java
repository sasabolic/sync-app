package com.example.domain.model;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public final class NullableConverter extends SimpleArgumentConverter {

  @Override
  protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
    if ("null".equals(o)) {
      return null;
    }
    return DefaultArgumentConverter.INSTANCE.convert(o, aClass);
  }
}
