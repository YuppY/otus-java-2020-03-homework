package ru.otus.hw.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.helpers.PasswordHelpers;
import ru.otus.hw.orm.mapper.annotations.Id;

/** @author sergey created on 03.02.19. */
@Data
@AllArgsConstructor
public class User {
  static final long UNDEFINED_ID = -1;

  @Id private long id = UNDEFINED_ID;
  private String login;
  private String passwordSalt;
  private String passwordHash;

  public User(String login, String password) {
    var salt = PasswordHelpers.generateSalt();
    this.login = login;
    this.passwordSalt = salt;
    this.passwordHash = PasswordHelpers.hashPassword(salt, password);
  }

  public boolean verifyPassword(String password) {
    return PasswordHelpers.hashPassword(passwordSalt, password).equals(passwordHash);
  }
}
