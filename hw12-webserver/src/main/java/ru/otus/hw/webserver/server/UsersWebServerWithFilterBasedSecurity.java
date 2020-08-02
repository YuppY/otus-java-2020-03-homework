package ru.otus.hw.webserver.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.hw.dao.UserDao;
import ru.otus.hw.webserver.services.TemplateProcessor;
import ru.otus.hw.webserver.services.UserAuthService;
import ru.otus.hw.webserver.servlet.AuthorizationFilter;
import ru.otus.hw.webserver.servlet.LoginServlet;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
  private final UserAuthService authService;

  public static final String LOGIN_URI = "/login";

  public UsersWebServerWithFilterBasedSecurity(
      int port,
      UserAuthService authService,
      UserDao userDao,
      Gson gson,
      TemplateProcessor templateProcessor) {
    super(port, userDao, gson, templateProcessor);
    this.authService = authService;
  }

  @Override
  protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
    servletContextHandler.addServlet(
        new ServletHolder(new LoginServlet(templateProcessor, authService)), LOGIN_URI);
    Arrays.stream(paths)
        .forEachOrdered(
            path -> servletContextHandler.addFilter(new FilterHolder(new AuthorizationFilter(LOGIN_URI)), path, null));
    return servletContextHandler;
  }
}
