package ru.otus.appcontainer;

import lombok.SneakyThrows;
import lombok.Value;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {

  private final Map<Class<?>, Object> appComponentsByType = new HashMap<>();
  private final Map<String, Object> appComponentsByName = new HashMap<>();

  public AppComponentsContainerImpl(Class<?> initialConfigClass) {
    processConfig(initialConfigClass);
  }

  @Value
  static class MethodWithAnnotation<T extends Annotation> {
    Method method;
    T annotation;
  }

  @SneakyThrows({IllegalAccessException.class, InvocationTargetException.class})
  private void inject(MethodWithAnnotation<AppComponent> mwa, Object config) {
    var method = mwa.getMethod();
    var annotation = mwa.getAnnotation();
    var componentName = annotation.name();
    var componentType = method.getReturnType();
    var parameters =
        Arrays.stream(method.getParameterTypes())
            .map(
                requiredType -> {
                  var parameter = getAppComponent(requiredType);
                  if (parameter == null) {
                    throw new ComponentNotFoundException(
                        String.format(
                            "not found component for the type %s", requiredType.getName()));
                  }
                  return parameter;
                })
            .toArray();
    var component = method.invoke(config, parameters);

    if (appComponentsByName.putIfAbsent(componentName, component) != null) {
      throw new ComponentDuplicateException(
          String.format("duplicate app component with the name %s", componentName));
    }
    if (appComponentsByType.putIfAbsent(componentType, component) != null) {
      throw new ComponentDuplicateException(
          String.format("duplicate app component with the type %s", componentType.getName()));
    }
  }

  @SneakyThrows
  private void processConfig(Class<?> configClass) {
    checkConfigClass(configClass);

    var config = configClass.getConstructor().newInstance();
    Arrays.stream(configClass.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(AppComponent.class))
        .map(
            method ->
                new MethodWithAnnotation<>(
                    method, method.getDeclaredAnnotation(AppComponent.class)))
        .sorted(Comparator.comparingInt(mwa -> mwa.getAnnotation().order()))
        .forEachOrdered(mwa -> inject(mwa, config));
  }

  private void checkConfigClass(Class<?> configClass) {
    if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
      throw new IllegalArgumentException(
          String.format("Given class is not config %s", configClass.getName()));
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> C getAppComponent(Class<C> componentClass) {
    return (C) appComponentsByType.get(componentClass);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> C getAppComponent(String componentName) {
    return (C) appComponentsByName.get(componentName);
  }
}
