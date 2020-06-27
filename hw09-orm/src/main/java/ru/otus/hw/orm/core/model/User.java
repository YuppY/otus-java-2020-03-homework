package ru.otus.hw.orm.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.orm.jdbc.mapper.annotations.Id;

/** @author sergey created on 03.02.19. */
@Data
@AllArgsConstructor
public class User {
  @Id private final long id;
  private String name;
  private int age;
}
