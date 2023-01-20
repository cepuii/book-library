package ua.od.cepuii.library.repository.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl<T extends AbstractEntity> implements DbExecutor<T> {

    private static final Logger log = LoggerFactory.getLogger(DbExecutorImpl.class);

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(params, preparedStatement);
            preparedStatement.executeUpdate();
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    @Override
    public boolean executeInsertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            preparedStatement.execute();
            log.info("{}", preparedStatement);
            return true;
        }
    }

    private static void setParams(List<Object> params, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, long id, Function<ResultSet, Optional<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> executeSelectAllByParam(Connection connection, String sql, Object param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, param);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> executeSelectAllWithLimit(Connection connection, String sql, String firstParam, String secondParam, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstParam);
            preparedStatement.setString(2, secondParam);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public boolean executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            boolean result = preparedStatement.executeUpdate() != 0;
            log.info("{}", preparedStatement);
            return result;
        }
    }


    @Override
    public boolean executeById(Connection connection, String sql, long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info("{}", preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public Collection<T> executeSelectAllById(Connection connection, String sql, long id, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            log.info("id {}, limit {}, offset {}", id, limit, offset);
            preparedStatement.setLong(1, id);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }
}
