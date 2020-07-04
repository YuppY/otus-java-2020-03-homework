package ru.otus.hw.logannotation;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw.logannotation.annotations.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogCalls {
  private static final Logger logger = LoggerFactory.getLogger(LogCalls.class);

  private LogCalls() {}

  @SneakyThrows
  public static <T> T wrap(Class<T> interfaceClass, T instance) {
    Set<String> loggedMethodNames =
        Stream.of(instance.getClass().getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Log.class))
            .map(Method::getName)
            .collect(Collectors.toUnmodifiableSet());

    return interfaceClass.cast(
        Proxy.newProxyInstance(
            LogCalls.class.getClassLoader(),
            new Class<?>[] {interfaceClass},
            (proxy, method, args) -> {
              var methodName = method.getName();
              if (loggedMethodNames.contains(methodName)) {
                logger.info(
                    String.format("invoking %s with %s", methodName, Arrays.toString(args)));
              }
              return method.invoke(instance, args);
            })
    );
  }
}
