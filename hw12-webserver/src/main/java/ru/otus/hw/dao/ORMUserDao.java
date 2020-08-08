package ru.otus.hw.dao;

import lombok.SneakyThrows;
import ru.otus.hw.dao.model.User;
import ru.otus.hw.orm.mapper.EntityClassMetaDataImpl;
import ru.otus.hw.orm.mapper.JdbcMapper;
import ru.otus.hw.orm.mapper.JdbcMapperImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Optional;

public class ORMUserDao implements UserDao, AutoCloseable {
  private final Connection connection;
  private final JdbcMapper<User> userMapper;

  @SneakyThrows
  public ORMUserDao(String dbUrl) {
    connection = DriverManager.getConnection(dbUrl);
    connection.setAutoCommit(false);
    userMapper = new JdbcMapperImpl<>(connection, new EntityClassMetaDataImpl<>(User.class));
  }

  @SneakyThrows
  public void init() {
    try (var pst =
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS User("
                + "id BIGINT(20) NOT NULL AUTO_INCREMENT, "
                + "login VARCHAR(255) UNIQUE, "
                + "passwordSalt VARCHAR(684), "
                + "passwordHash VARCHAR(684)"
                + ")")) {
      pst.executeUpdate();
    }
    connection.commit();
  }

  @Override
  @SneakyThrows
  public void insert(User user) {
    userMapper.insert(user);
    connection.commit();
  }

  @Override
  public Optional<User> findById(long id) {
    return userMapper.findByField("id", id);
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return userMapper.findByField("login", login);
  }

  @Override
  public Collection<User> all() {
    return userMapper.all();
  }

  @Override
  @SneakyThrows
  public void close() {
    connection.close();
  }
}
