package ru.otus.hw.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T> {

    long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> executeSelectByField(Connection connection, String sql, Object field, Function<ResultSet, T> rsHandler) throws SQLException;

    Collection<T> executeSelectAll(Connection connection, String sql, Function<ResultSet, T> rsHandler) throws SQLException;
}
