package ru.otus.appcontainer;

public class ComponentNotFound extends RuntimeException {
  public ComponentNotFound(String message) {
    super(message);
  }
}
