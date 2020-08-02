package ru.otus.hw.webserver.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

  private ServletContext context;
  private String loginUri;

  public AuthorizationFilter(String loginUri) {
    this.loginUri = loginUri;
  }

  @Override
  public void init(FilterConfig filterConfig) {
    this.context = filterConfig.getServletContext();
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String uri = request.getRequestURI();
    this.context.log("Requested Resource:" + uri);

    System.out.println(uri);

    HttpSession session = request.getSession(false);
    if (!uri.equals(this.loginUri) && session == null) {
      response.sendRedirect(this.loginUri);
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {}
}
