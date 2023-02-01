package ua.od.cepuii.library.repository.jdbc.executor;

import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface QueryExecutor<T extends AbstractEntity> {

    long insert(Connection connection, String sql, List<Object> params) throws SQLException;

    boolean insertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> selectByParams(Connection connection, String sql, List<Object> params, Function<ResultSet, Optional<T>> rsHandler) throws SQLException;

    boolean update(Connection connection, String sql, List<Object> params) throws SQLException;

    boolean queryById(Connection connection, String sql, long id) throws SQLException;

    boolean queryByString(Connection connection, String sql, String value) throws SQLException;

    Collection<T> selectAll(Connection connection, String sql, List<Object> params, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    int selectCount(Connection connection, String countSelectAllFilter, List<Object> strings) throws SQLException;
}
