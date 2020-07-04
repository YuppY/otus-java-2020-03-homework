package ru.otus.hw.logannotation;

import ru.otus.hw.logannotation.annotations.Log;

public class Demo {
  interface DemoInterface {
    void doLoggedAction(int arg1, String arg2);

    void doNotLoggedAction(int arg1, String arg2);
  }

  static class DemoClass implements DemoInterface {
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
    DemoInterface obj = LogCalls.wrap(DemoInterface.class, new DemoClass());
    obj.doLoggedAction(1, "foo");
    obj.doNotLoggedAction(2, "bar");
  }
}
