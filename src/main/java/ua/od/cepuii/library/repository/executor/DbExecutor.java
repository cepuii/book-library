package ua.od.cepuii.library.repository.executor;

import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T extends AbstractEntity> {

    long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException;

    void executeInsertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> executeSelect(Connection connection, String sql, long id, Function<ResultSet, Optional<T>> rsHandler) throws SQLException;

    boolean executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException;

    boolean executeDelete(Connection connection, String sql, long id) throws SQLException;

    Collection<T> executeSelectAll(Connection connection, String sql, String orderBy, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    Collection<T> executeSelectAllByParam(Connection connection, String sql, String param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    Collection<T> executeSelectAllWithLimit(Connection connection, String sql, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;
}
