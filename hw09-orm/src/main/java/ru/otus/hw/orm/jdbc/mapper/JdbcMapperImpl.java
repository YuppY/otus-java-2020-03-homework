package ru.otus.hw.orm.jdbc.mapper;

import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw.orm.jdbc.DbExecutor;
import ru.otus.hw.orm.jdbc.DbExecutorImpl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class JdbcMapperImpl<T> implements JdbcMapper<T> {
  private final Connection connection;
  private final EntityClassMetaData<T> metadata;
  private final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
  private final DbExecutor<T> executor = new DbExecutorImpl<>();

  private List<String> getFieldNames(Collection<Field> fields) {
    return fields.stream().map(Field::getName).collect(Collectors.toList());
  }

  private List<Object> getFieldValues(Collection<Field> fields, T objectData) {
    return fields.stream()
        .map(
            field -> {
              try {
                return field.get(objectData);
              } catch (IllegalAccessException e) {
                throw new AssertionError(e); // impossible to get here
              }
            })
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private T newInstance(Collection<Object> fieldValues) {
    return metadata.getConstructor().newInstance(fieldValues.toArray());
  }

  @Override
  @SneakyThrows
  public void insert(T objectData) {
    var dataFields = metadata.getFieldsWithoutId();
    var sql =
        String.format(
            "INSERT INTO %s (%s) VALUES (%s)",
            metadata.getName(),
            String.join(", ", getFieldNames(dataFields)),
            String.join(", ", Collections.nCopies(dataFields.size(), "?")));
    var id = executor.executeInsert(connection, sql, getFieldValues(dataFields, objectData));
    metadata.getIdField().set(objectData, id);
  }

  @Override
  @SneakyThrows
  public void update(T objectData) {
    var dataFields = metadata.getFieldsWithoutId();
    var idField = metadata.getIdField();
    var sql =
        String.format(
            "UPDATE %s SET %s WHERE %s = ?",
            metadata.getName(),
            getFieldNames(dataFields).stream()
                .map(fieldName -> String.format("%s = ?", fieldName))
                .collect(Collectors.joining(", ")),
            idField.getName());
    executor.executeInsert(
        connection,
        sql,
        getFieldValues(
            Stream.concat(dataFields.stream(), Stream.of(idField)).collect(Collectors.toList()),
            objectData));
  }

  @Override
  @SneakyThrows
  public void insertOrUpdate(T objectData) {
    var fields = metadata.getAllFields();
    var idField = metadata.getIdField();
    var sql =
        String.format(
            "MERGE INTO %s (%s) KEY (%s) VALUES (%s)",
            metadata.getName(),
            String.join(", ", getFieldNames(fields)),
                idField.getName(),
            String.join(", ", Collections.nCopies(fields.size(), "?")));
    executor.executeInsert(connection, sql, getFieldValues(fields, objectData));
  }

  @Override
  @SneakyThrows
  public T findById(long id) {
    var fieldNames = getFieldNames(metadata.getAllFields());
    var sql =
        String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            String.join(", ", fieldNames), metadata.getName(), metadata.getIdField().getName());
    return executor
        .executeSelect(
            connection,
            sql,
            id,
            rs -> {
              try {
                if (rs.next()) {
                  return newInstance(
                      fieldNames.stream()
                          .map(
                              fieldName -> {
                                try {
                                  return rs.getObject(fieldName);
                                } catch (SQLException e) {
                                  throw new AssertionError(e);
                                }
                              })
                          .collect(Collectors.toList()));
                }
              } catch (SQLException e) {
                throw new AssertionError(e);
              }
              return null;
            })
        .orElse(null);
  }
}
