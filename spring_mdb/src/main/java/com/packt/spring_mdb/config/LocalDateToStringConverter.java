package com.packt.spring_mdb.config;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalDate;

public class LocalDateToStringConverter implements Converter<LocalDate, String> {
  @Override
  public String convert(LocalDate source) {
    return source == null ? null : source.toString();
  }
}
