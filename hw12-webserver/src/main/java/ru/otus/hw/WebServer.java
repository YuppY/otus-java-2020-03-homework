package ru.otus.hw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;
import ru.otus.hw.dao.UserDao;
import ru.otus.hw.webserver.server.UsersWebServer;
import ru.otus.hw.webserver.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.hw.webserver.services.*;

/*
    Страница пользователей
    http://localhost:8080

    REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServer {
  private static final int WEB_SERVER_PORT = 8080;
  private static final String TEMPLATES_DIR = "/templates/";

  public static void main(String[] args) throws Exception {
    @Cleanup UserDao userDao = new UserDaoProviderImpl().getUserDao();

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
