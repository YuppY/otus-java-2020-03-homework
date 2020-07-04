package ru.otus.appcontainer;

import lombok.SneakyThrows;
import lombok.Value;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class AppComponentsContainerImpl implements AppComponentsContainer {

  private final List<Object> appComponents = new ArrayList<>();
  private final Map<String, Object> appComponentsByName = new HashMap<>();

  public AppComponentsContainerImpl(Class<?> initialConfigClass) {
    processConfig(initialConfigClass);
  }

  @Value
  static class MethodWithAnnotation<T extends Annotation> {
    Method method;
    T annotation;
  }

  @SneakyThrows
  private void processConfig(Class<?> configClass) {
    checkConfigClass(configClass);

    var config = configClass.getConstructor().newInstance();
    var componentsByType = new HashMap<Class<?>, Object>();
    Arrays.stream(configClass.getDeclaredMethods())
        .map(
            method ->
                new MethodWithAnnotation<>(
                    method, method.getDeclaredAnnotation(AppComponent.class)))
        .filter(mwa -> mwa.getAnnotation() != null)
        .sorted(Comparator.comparingInt(mwa -> mwa.getAnnotation().order()))
        .forEachOrdered(
            new Consumer<>() {
              @Override
              @SneakyThrows({IllegalAccessException.class, InvocationTargetException.class})
              public void accept(MethodWithAnnotation<AppComponent> mwa) {
                var method = mwa.getMethod();
                var annotation = mwa.getAnnotation();
                var componentName = annotation.name();
                var componentType = method.getReturnType();
                var parameters =
                    Arrays.stream(method.getParameterTypes())
                        .map(
                            requiredType -> {
                              var parameter = componentsByType.get(requiredType);
                              if (parameter == null) {
                                throw new ComponentNotFound(
                                    String.format(
                                        "not found component for the type %s",
                                        requiredType.getName()));
                              }
                              return parameter;
                            })
                        .toArray();
                var component = method.invoke(config, parameters);

                if (appComponentsByName.putIfAbsent(componentName, component) != null) {
                  throw new DuplicateComponent(
                      String.format("duplicate app component with the name %s", componentName));
                }
                if (componentsByType.putIfAbsent(componentType, component) != null) {
                  throw new DuplicateComponent(
                      String.format(
                          "duplicate app component with the type %s", componentType.getName()));
                }
              }
            });

    appComponents.addAll(componentsByType.values());
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
    return (C) appComponents.stream().filter(componentClass::isInstance).findFirst().orElse(null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> C getAppComponent(String componentName) {
    return (C) appComponentsByName.get(componentName);
  }
}
