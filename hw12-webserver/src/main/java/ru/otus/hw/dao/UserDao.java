package ru.otus.hw.dao;

import ru.otus.hw.dao.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao extends AutoCloseable {

  void init();

  void insert(User user);

  Optional<User> findById(long id);

  Optional<User> findByLogin(String login);

  Collection<User> all();

  void close();
}
