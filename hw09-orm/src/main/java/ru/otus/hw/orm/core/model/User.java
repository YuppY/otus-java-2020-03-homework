package ru.otus.hw.orm.core.model;

import lombok.Data;
import ru.otus.hw.orm.jdbc.mapper.annotations.Id;

/** @author sergey created on 03.02.19. */
@Data
public class User {
  @Id private final long id;
  private final String name;
  private final int age;
}
