package ru.otus.hw.webserver.services;

import lombok.NonNull;
import ru.otus.hw.dao.ORMUserDao;
import ru.otus.hw.dao.UserDao;

public class UserDaoProviderImpl implements UserDaoProvider {
  private static final String DATABASE_FILE = "./webserver";

  @Override
  public @NonNull UserDao getUserDao() {
    var userDao = new ORMUserDao(String.format("jdbc:h2:file:%s", DATABASE_FILE));
    userDao.init();
    return userDao;
  }
}
