package ru.otus.hw.webserver.servlet;

import ru.otus.hw.dao.UserDao;
import ru.otus.hw.dao.model.User;
import ru.otus.hw.webserver.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class UsersServlet extends HttpServlet {

  private static final String USERS_PAGE_TEMPLATE = "users.html";
  private static final String PARAM_LOGIN = "login";
  private static final String PARAM_PASSWORD = "password";
  private static final String TEMPLATE_ATTR_ALL_USERS = "users";

  private final UserDao userDao;
  private final TemplateProcessor templateProcessor;

  public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
    this.templateProcessor = templateProcessor;
    this.userDao = userDao;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    response
        .getWriter()
        .println(
            templateProcessor.getPage(
                USERS_PAGE_TEMPLATE, Map.of(TEMPLATE_ATTR_ALL_USERS, userDao.all())));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    var user = new User(request.getParameter(PARAM_LOGIN), request.getParameter(PARAM_PASSWORD));
    userDao.insert(user);
    response.sendRedirect("/users");
  }
}
