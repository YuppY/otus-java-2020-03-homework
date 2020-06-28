package ru.otus.hw.logannotation.interfaces;

public interface Logged {
  void doLoggedAction(int arg1, String arg2);

  void doNotLoggedAction(int arg1, String arg2);
}
