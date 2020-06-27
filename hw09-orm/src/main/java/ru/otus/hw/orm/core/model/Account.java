package ru.otus.hw.orm.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.orm.jdbc.mapper.annotations.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {
  @Id private final long no;
  private String type;
  private BigDecimal rest;
}
