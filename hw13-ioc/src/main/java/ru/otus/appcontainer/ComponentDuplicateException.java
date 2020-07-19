package ru.otus.appcontainer;

public class ComponentDuplicateException extends RuntimeException {
  public ComponentDuplicateException(String message) {
    super(message);
  }
}
