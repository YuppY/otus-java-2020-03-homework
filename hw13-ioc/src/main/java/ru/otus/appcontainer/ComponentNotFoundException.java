package ru.otus.appcontainer;

public class ComponentNotFoundException extends RuntimeException {
  public ComponentNotFoundException(String message) {
    super(message);
  }
}
