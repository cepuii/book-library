package ua.od.cepuii.library.repository.jdbc.executor;

import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T extends AbstractEntity> {

    long insert(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> selectByParams(Connection connection, String sql, List<Object> params, Function<ResultSet, Optional<T>> rsHandler) throws SQLException;

    boolean insertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> selectById(Connection connection, String sql, long id, Function<ResultSet, Optional<T>> rsHandler) throws SQLException;

    boolean update(Connection connection, String sql, List<Object> params) throws SQLException;

    boolean queryById(Connection connection, String sql, long id) throws SQLException;

    boolean queryByString(Connection connection, String sql, String value) throws SQLException;

    Collection<T> selectAllByParam(Connection connection, String sql, Object param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    Collection<T> selectAllWithLimit(Connection connection, String sql, String titleFilter, String authorFilter, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    Collection<T> selectAllById(Connection connection, String sql, long id, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    int selectCount(Connection connection, String countSelectAllFilter, List<Object> strings) throws SQLException;
}
