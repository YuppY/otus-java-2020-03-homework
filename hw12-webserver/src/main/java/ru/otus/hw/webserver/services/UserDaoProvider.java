package ru.otus.hw.webserver.services;

import ru.otus.hw.dao.UserDao;

public interface UserDaoProvider {
  UserDao getUserDao();
}
