package ru.otus.hw.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.hw.dao.ORMUserDao;
import ru.otus.hw.dao.UserDao;
import ru.otus.hw.dao.model.User;
import ru.otus.hw.webserver.server.UsersWebServer;
import ru.otus.hw.webserver.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.hw.webserver.services.TemplateProcessor;
import ru.otus.hw.webserver.services.TemplateProcessorImpl;
import ru.otus.hw.webserver.services.UserAuthService;
import ru.otus.hw.webserver.services.UserAuthServiceImpl;

import java.util.Scanner;

/*
    Страница пользователей
    http://localhost:8080

    REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServer {
  private static final int WEB_SERVER_PORT = 8080;
  private static final String TEMPLATES_DIR = "/templates/";
  private static final String DATABASE_FILE = "./webserver";

  private static void createUser(UserDao userDao) {
    var in = new Scanner(System.in);
    System.out.println("Login:");
    var login = in.next();
    System.out.println("Password:");
    var password = in.next();

    userDao.insert(new User(login, password));
  }

  public static void main(String[] args) throws Exception {
    try (UserDao userDao = new ORMUserDao(String.format("jdbc:h2:file:%s", DATABASE_FILE))) {
      userDao.init();

      if (args.length == 1 && args[0].equals("createUser")) {
        createUser(userDao);
        return;
      }

      Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
      TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
      UserAuthService authService = new UserAuthServiceImpl(userDao);

      UsersWebServer usersWebServer =
          new UsersWebServerWithFilterBasedSecurity(
              WEB_SERVER_PORT, authService, userDao, gson, templateProcessor);

      usersWebServer.start();
      usersWebServer.join();
    }
  }
}
