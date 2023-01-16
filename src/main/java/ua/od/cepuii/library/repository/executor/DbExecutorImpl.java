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
            log.info(preparedStatement.toString());
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean executeInsertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("InsertSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            preparedStatement.execute();
            connection.commit();
            log.info(preparedStatement.toString());
            return true;
        } catch (SQLException e) {
            connection.rollback(savepoint);
            log.error(e.getMessage());
            return false;
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
            log.info(preparedStatement.toString());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> executeSelectAllByParam(Connection connection, String sql, String param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, param);
            log.info(preparedStatement.toString());
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
            log.info(preparedStatement.toString());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public boolean executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("UpdateSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            boolean result = preparedStatement.executeUpdate() != 0;
            connection.commit();
            log.info(preparedStatement.toString());
            return result;
        } catch (SQLException e) {
            connection.rollback(savepoint);
            log.error(e.getMessage());
            return false;
        }
    }


    @Override
    public boolean executeById(Connection connection, String sql, long id) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("DeleteSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info(preparedStatement.toString());
            boolean result = preparedStatement.executeUpdate() != 0;
            connection.commit();
            return result;
        } catch (SQLException e) {
            connection.rollback(savepoint);
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Collection<T> executeSelectAll(Connection connection, String sql, String orderBy, int limit, int offset, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(orderBy));
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            log.info(preparedStatement.toString());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }
}
