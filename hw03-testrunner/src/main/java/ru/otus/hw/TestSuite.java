package ru.otus.hw;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.hw.annotations.After;
import ru.otus.hw.annotations.Before;
import ru.otus.hw.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class TestSuite {
  private final Constructor<?> instanceConstructor;
  private final List<Method> testMethods;
  private final List<Method> beforeMethods;
  private final List<Method> afterMethods;

  @SneakyThrows
  static TestSuite forClass(Class<?> testsClass) {
    var testMethods = new ArrayList<Method>();
    var beforeMethods = new ArrayList<Method>();
    var afterMethods = new ArrayList<Method>();

    for (var method : testsClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        testMethods.add(method);
      }

      if (method.isAnnotationPresent(Before.class)) {
        beforeMethods.add(method);
      }

      if (method.isAnnotationPresent(After.class)) {
        afterMethods.add(method);
      }
    }

    return new TestSuite(
        testsClass.getConstructor(),
        Collections.unmodifiableList(testMethods),
        Collections.unmodifiableList(beforeMethods),
        Collections.unmodifiableList(afterMethods));
  }

  @SneakyThrows
  TestResults run() {
    var failures = 0;

    for (var testMethod : testMethods) {
      var testInstance = instanceConstructor.newInstance();

      for (var beforeMethod : beforeMethods) {
        beforeMethod.invoke(testInstance);
      }

      try {
        testMethod.invoke(testInstance);
      } catch (Exception e) {
        failures++;
      }

      for (var afterMethod : afterMethods) {
        afterMethod.invoke(testInstance);
      }
    }

    return new TestResults(testMethods.size() - failures, failures);
  }

  @Data
  static class TestResults {
    private final int passes;
    private final int failures;

    public int getTotal() {
      return passes + failures;
    }
  }
}
