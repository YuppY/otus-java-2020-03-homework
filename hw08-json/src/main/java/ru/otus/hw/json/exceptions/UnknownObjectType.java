package ru.otus.hw.json.exceptions;

public class UnknownObjectType extends Exception {
  public UnknownObjectType(String typeName) {
    super("Unknown object type: " + typeName);
  }
}
