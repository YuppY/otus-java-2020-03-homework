package ru.otus.hw.logannotation;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import ru.otus.hw.logannotation.annotations.Log;
import ru.otus.hw.logannotation.interfaces.Logged;

public class TestLogCalls {
  Logger logger = (Logger) LoggerFactory.getLogger(LogCalls.class);
  ListAppender<ILoggingEvent> logAppender = new ListAppender<>();

  @BeforeEach
  void addLogAppender() {
    logAppender.start();
    logger.addAppender(logAppender);
  }

  @AfterEach
  void detachLogAppender() {
    logAppender.stop();
    logger.detachAppender(logAppender);
  }

  class TestClass implements Logged {
    @Override
    @Log
    public void doLoggedAction(int arg1, String arg2) {}

    @Override
    public void doNotLoggedAction(int arg1, String arg2) {}
  }

  @Test
  void testDoLoggedAction() {
    var instance = Mockito.spy(new TestClass());
    var logged = LogCalls.wrap(instance);

    logged.doLoggedAction(1, "foo");

    Mockito.verify(instance).doLoggedAction(1, "foo");

    var logEntries = logAppender.list;
    Assertions.assertEquals(1, logEntries.size());
    Assertions.assertEquals(Level.INFO, logEntries.get(0).getLevel());
    Assertions.assertEquals(
        "invoking doLoggedAction with [1, foo]", logEntries.get(0).getMessage());
  }

  @Test
  void testDoNotLoggedAction() {
    var instance = Mockito.spy(new TestClass());
    var logged = LogCalls.wrap(instance);

    logged.doNotLoggedAction(2, "bar");

    Mockito.verify(instance).doNotLoggedAction(2, "bar");

    var logEntries = logAppender.list;
    Assertions.assertEquals(0, logEntries.size());
  }
}
