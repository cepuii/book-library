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
    public long insert(Connection connection, String sql, List<Object> params) throws SQLException {
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
    public Optional<T> selectByParams(Connection connection, String sql, List<Object> params, Function<ResultSet, Optional<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public boolean insertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            log.info("{}", preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    private static void setParams(List<Object> params, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }
    }

    @Override
    public Optional<T> selectById(Connection connection, String sql, long id, Function<ResultSet, Optional<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> selectAllByParam(Connection connection, String sql, Object param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, param);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> selectAllWithLimit(Connection connection, String sql, String firstParam, String secondParam, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
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
    public boolean update(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            boolean result = preparedStatement.executeUpdate() != 0;
            log.info("{}", preparedStatement);
            return result;
        }
    }


    //TODO rename this metgod
    @Override
    public boolean queryById(Connection connection, String sql, long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info("{}", preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public boolean queryByString(Connection connection, String sql, String value) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            log.info("{}", preparedStatement);
            return preparedStatement.execute();
        }
    }

    @Override
    public Collection<T> selectAllById(Connection connection, String sql, long id, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
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

    @Override
    public int selectCount(Connection connection, String sqlQuery, List<Object> strings) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParams(strings, statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }
}