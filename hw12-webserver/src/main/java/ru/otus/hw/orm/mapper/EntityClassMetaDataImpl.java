package ru.otus.hw.orm.mapper;

import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import ru.otus.hw.orm.mapper.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
  private final Class<T> entityClass;
  private final Field idField;
  private final List<Field> dataFields;

  public EntityClassMetaDataImpl(@NotNull Class<T> entityClass) {
    Field idField = null;
    var dataFields = new ArrayList<Field>();
    for (var field : entityClass.getDeclaredFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        continue;
      }
      field.setAccessible(true);
      if (field.isAnnotationPresent(Id.class)) {
        idField = field;
      } else {
        dataFields.add(field);
      }
    }

    this.entityClass = entityClass;
    this.idField = idField;
    this.dataFields = Collections.unmodifiableList(dataFields);
  }

  @Override
  public String getName() {
    return entityClass.getSimpleName();
  }

  @Override
  @SneakyThrows
  public Constructor<T> getConstructor() {
    var constructorFields = this.getAllFields().stream().map(Field::getType).toArray(Class[]::new);
    return entityClass.getConstructor(constructorFields);
  }

  @Override
  public List<Field> getAllFields() {
    return Stream.concat(Stream.of(this.idField), dataFields.stream()).collect(Collectors.toList());
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    return this.dataFields;
  }
}
