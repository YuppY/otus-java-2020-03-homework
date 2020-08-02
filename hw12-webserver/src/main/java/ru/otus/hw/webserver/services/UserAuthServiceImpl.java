package ru.otus.hw.webserver.services;

import ru.otus.hw.dao.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

  private final UserDao userDao;

  public UserAuthServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public boolean authenticate(String login, String password) {
    return userDao.findByLogin(login).map(user -> user.verifyPassword(password)).orElse(false);
  }
}
