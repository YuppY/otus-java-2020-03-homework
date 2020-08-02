package ru.otus.hw.orm.mapper;

import java.util.Collection;
import java.util.Optional;

public interface JdbcMapper<T> {
  void insert(T objectData);

  void update(T objectData);

  void insertOrUpdate(T objectData);

  Optional<T> findByField(String name, Object value);

  Collection<T> all();
}
