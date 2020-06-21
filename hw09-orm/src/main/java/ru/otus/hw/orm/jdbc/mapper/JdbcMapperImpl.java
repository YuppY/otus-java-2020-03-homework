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
import java.util.Collections;
import java.util.stream.Collectors;

@Data
public class JdbcMapperImpl<T> implements JdbcMapper<T> {
  private final Connection connection;
  private final EntityClassMetaData<T> metadata;
  private final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
  private final DbExecutor<T> executor = new DbExecutorImpl<>();

  @Override
  @SneakyThrows
  public void insert(T objectData) {
    var dataFields = metadata.getFieldsWithoutId();
    var fieldNames = dataFields.stream().map(Field::getName).collect(Collectors.toList());
    var fieldValues =
        dataFields.stream()
            .map(
                field -> {
                  try {
                    return field.get(objectData);
                  } catch (IllegalAccessException e) {
                    return null;
                  }
                })
            .collect(Collectors.toList());
    var sql =
        String.format(
            "INSERT INTO %s (%s) VALUES (%s)",
            metadata.getName(),
            String.join(", ", fieldNames),
            String.join(", ", Collections.nCopies(dataFields.size(), "?")));
    var id = executor.executeInsert(connection, sql, fieldValues);

    metadata.getIdField().set(objectData, id);
  }

  @Override
  public void update(T objectData) {}

  @Override
  public void insertOrUpdate(T objectData) {}

  @Override
  @SneakyThrows
  public T findById(long id) {
    var fieldNames =
        metadata.getAllFields().stream().map(Field::getName).collect(Collectors.toList());
    var sql =
        String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            String.join(", ", fieldNames),
            metadata.getName(),
            metadata.getIdField().getName());
    return executor
        .executeSelect(
            connection,
            sql,
            id,
            rs -> {
              try {
                if (rs.next()) {
                  var fieldValues =
                      fieldNames.stream()
                          .map(
                              fieldName -> {
                                try {
                                  return rs.getObject(fieldName);
                                } catch (SQLException e) {
                                  logger.error("getObject", e);
                                  return null;
                                }
                              })
                          .toArray();
                  try {
                    return metadata.getConstructor().newInstance(fieldValues);
                  } catch (Exception e) {
                    logger.error("newInstance", e);
                    return null;
                  }
                }
              } catch (SQLException e) {
                logger.error(e.getMessage(), e);
              }
              return null;
            })
        .orElse(null);
  }
}
