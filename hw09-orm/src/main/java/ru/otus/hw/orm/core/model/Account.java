package ru.otus.hw.orm.core.model;

import lombok.Data;
import ru.otus.hw.orm.jdbc.mapper.annotations.Id;

import java.math.BigDecimal;

@Data
public class Account {
  @Id private final long no;
  private final String type;
  private final BigDecimal rest;
}
