package ru.otus.hw;

import ru.otus.hw.annotations.After;
import ru.otus.hw.annotations.Before;
import ru.otus.hw.annotations.Test;

public class Tests {

  @Before
  void firstBefore() {
    System.out.println("firstBefore");
  }

  @Before
  void secondBefore() {
    System.out.println("secondBefore");
  }

  @Test
  void firstTest() {
    System.out.println("firstTest");
  }

  @Test
  void secondTest() {
    System.out.println("secondTest");
    throw new AssertionError("something is terribly wrong");
  }

  @After
  void firstAfter() {
    System.out.println("firstAfter");
  }

  @After
  void secondAfter() {
    System.out.println("secondAfter");
  }
}
