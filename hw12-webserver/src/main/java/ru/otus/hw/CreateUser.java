package ru.otus.hw;

import lombok.Cleanup;
import ru.otus.hw.dao.UserDao;
import ru.otus.hw.dao.model.User;
import ru.otus.hw.webserver.services.UserDaoProviderImpl;

import java.util.Scanner;

public class CreateUser {
  public static void main(String[] args) {
    @Cleanup UserDao userDao = new UserDaoProviderImpl().getUserDao();

    var in = new Scanner(System.in);
    System.out.println("Login:");
    var login = in.next();
    System.out.println("Password:");
    var password = in.next();

    userDao.insert(new User(login, password));
  }
}
