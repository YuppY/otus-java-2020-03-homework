package ru.otus.hw;

import ru.otus.hw.suite.TestSuite;
import ru.otus.hw.tests.Tests;

public class TestRunner {
  static void run(Class<?> testsClass) {
    var results = TestSuite.forClass(testsClass).run();
    System.out.printf(
        "%s tests: %s passed, %s failed",
        results.getTotal(), results.getPasses(), results.getFailures());
  }

  public static void main(String... args) {
    run(Tests.class);
  }
}
