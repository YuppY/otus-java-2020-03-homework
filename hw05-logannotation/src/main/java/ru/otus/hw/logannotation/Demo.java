package ru.otus.hw.logannotation;

import ru.otus.hw.logannotation.annotations.Log;
import ru.otus.hw.logannotation.interfaces.Logged;

public class Demo {
  static class DemoClass implements Logged {

    @Override
    @Log
    public void doLoggedAction(int arg1, String arg2) {
      System.out.println("loggedAction");
    }

    @Override
    public void doNotLoggedAction(int arg1, String arg2) {
      System.out.println("notLoggedAction");
    }
  }

  public static void main(String[] args) {
    Logged obj = LogCalls.wrap(new DemoClass());
    obj.doLoggedAction(1, "foo");
    obj.doNotLoggedAction(2, "bar");
  }
}
