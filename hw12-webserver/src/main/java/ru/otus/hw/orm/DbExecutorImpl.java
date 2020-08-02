package ru.otus.hw.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** @author sergey created on 03.02.19. */
public class DbExecutorImpl<T> implements DbExecutor<T> {
  private static final Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

  @Override
  public long executeInsert(Connection connection, String sql, List<Object> params)
      throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      for (int idx = 0; idx < params.size(); idx++) {
        pst.setObject(idx + 1, params.get(idx));
      }
      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  @Override
  public Optional<T> executeSelectByField(
          Connection connection, String sql, Object field, Function<ResultSet, T> rsHandler)
      throws SQLException {
    try (var pst = connection.prepareStatement(sql)) {
      pst.setObject(1, field);
      try (var rs = pst.executeQuery()) {
        return Optional.ofNullable(rsHandler.apply(rs));
      }
    }
  }

  @Override
  public Collection<T> executeSelectAll(Connection connection, String sql, Function<ResultSet, T> rsHandler) throws SQLException {
    var result = new ArrayList<T>();
    try (var pst = connection.prepareStatement(sql)) {
      try (var rs = pst.executeQuery()) {
        while (true) {
          var obj = rsHandler.apply(rs);
          if (obj == null) {
            break;
          }
          result.add(obj);
        }
      }
    }
    return result;
  }
}
