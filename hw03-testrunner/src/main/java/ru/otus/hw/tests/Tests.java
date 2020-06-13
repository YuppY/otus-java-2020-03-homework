package ru.otus.hw.tests;

import ru.otus.hw.annotations.After;
import ru.otus.hw.annotations.Before;
import ru.otus.hw.annotations.Test;

public class Tests {

  @Before
  public void firstBefore() {
    System.out.println("firstBefore");
  }

  @Before
  public void secondBefore() {
    System.out.println("secondBefore");
  }

  @Test
  public void firstTest() {
    System.out.println("firstTest");
  }

  @Test
  public void secondTest() {
    System.out.println("secondTest");
    throw new AssertionError("something is terribly wrong");
  }

  @After
  public void firstAfter() {
    System.out.println("firstAfter");
  }

  @After
  public void secondAfter() {
    System.out.println("secondAfter");
  }
}
