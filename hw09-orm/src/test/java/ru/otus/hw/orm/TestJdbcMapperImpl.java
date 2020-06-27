package ru.otus.hw.orm;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.otus.hw.orm.core.model.Account;
import ru.otus.hw.orm.core.model.User;
import ru.otus.hw.orm.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.hw.orm.jdbc.mapper.JdbcMapper;
import ru.otus.hw.orm.jdbc.mapper.JdbcMapperImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestJdbcMapperImpl {
  private Connection connection;
  private JdbcMapper<User> userMapper;
  private JdbcMapper<Account> accountMapper;

  @BeforeAll
  @SneakyThrows
  void createDB() {
    connection = DriverManager.getConnection("jdbc:h2:mem:");
    connection.setAutoCommit(false);

    try (var pst = connection.prepareStatement("CREATE TABLE User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
      pst.executeUpdate();
    }

    try (var pst = connection.prepareStatement("CREATE TABLE Account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
      pst.executeUpdate();
    }

    userMapper = new JdbcMapperImpl<>(connection, new EntityClassMetaDataImpl<>(User.class));
    accountMapper = new JdbcMapperImpl<>(connection, new EntityClassMetaDataImpl<>(Account.class));
  }

  @Test
  void testInsertUser() {
    var user = new User(0, "foo", 33);
    userMapper.insert(user);

    Assertions.assertNotEquals(0, user.getId());
  }

  @Test
  void testInsertAccount() {
    var account = new Account(0, "foo", new BigDecimal("33"));
    accountMapper.insert(account);

    Assertions.assertNotEquals(0, account.getNo());
  }

  @Test
  @SneakyThrows
  void testFindByIdUser() {
    try (var pst = connection.prepareStatement("INSERT INTO User(id, name, age) VALUES (?, ?, ?)")) {
      pst.setInt(1, 99);
      pst.setString(2, "username");
      pst.setInt(3, 15);
      pst.executeUpdate();
    }
    connection.commit();

    var user = userMapper.findById(99);
    Assertions.assertEquals(new User(99, "username", 15), user);
  }

  @Test
  @SneakyThrows
  void testFindByIdAccount() {
    try (var pst = connection.prepareStatement("INSERT INTO Account(no, type, rest) VALUES (?, ?, ?)")) {
      pst.setInt(1, 99);
      pst.setString(2, "accountname");
      pst.setString(3, "15");
      pst.executeUpdate();
    }
    connection.commit();

    var account = accountMapper.findById(99);
    Assertions.assertEquals(new Account(99, "accountname", new BigDecimal("15")), account);
  }


  @Test
  void testUpdateUser() {
    var user = new User(0, "foo", 33);
    userMapper.insert(user);

    user.setName("bar");
    userMapper.update(user);

    Assertions.assertEquals(user, userMapper.findById(user.getId()));
  }

  @Test
  void testUpdateAccount() {
    var account = new Account(0, "foo", new BigDecimal("33"));
    accountMapper.insert(account);

    account.setType("bar");
    accountMapper.update(account);

    Assertions.assertEquals(account, accountMapper.findById(account.getNo()));
  }

  @Test
  void testInsertOrUpdateUser() {
    var user = new User(10, "foo", 33);
    userMapper.insertOrUpdate(user);
    Assertions.assertEquals(user, userMapper.findById(10));

    user.setName("bar");
    userMapper.insertOrUpdate(user);
    Assertions.assertEquals("bar", userMapper.findById(10).getName());
  }

  @Test
  void testInsertOrUpdateAccount() {
    var account = new Account(10, "foo", new BigDecimal("33"));
    accountMapper.insertOrUpdate(account);
    Assertions.assertEquals(account, accountMapper.findById(10));

    account.setType("bar");
    accountMapper.insertOrUpdate(account);
    Assertions.assertEquals("bar", accountMapper.findById(10).getType());
  }

  @AfterAll
  @SneakyThrows
  void closeConnection() {
    connection.close();
  }
}
