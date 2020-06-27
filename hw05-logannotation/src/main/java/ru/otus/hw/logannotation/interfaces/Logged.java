package ru.otus.hw.logannotation.interfaces;

import ru.otus.hw.logannotation.annotations.Log;

public interface Logged {
  @Log
  void doLoggedAction(int arg1, String arg2);

  void doNotLoggedAction(int arg1, String arg2);
}
