package ru.otus.appcontainer;

public class DuplicateComponent extends RuntimeException {
  public DuplicateComponent(String message) {
    super(message);
  }
}
