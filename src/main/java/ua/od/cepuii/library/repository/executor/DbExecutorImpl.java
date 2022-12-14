package ua.od.cepuii.library.repository.executor;

import ua.od.cepuii.library.model.AbstractEntity;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl<T extends AbstractEntity> implements DbExecutor<T> {

    @Override
    public long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("InsertSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(params, preparedStatement);
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                long id = rs.getLong("id");
                connection.commit();
                return id;
            }
        } catch (SQLException e) {
            connection.rollback(savepoint);
            throw e;
        }
    }

    @Override
    public void executeInsertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("InsertSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepoint);
            throw e;
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
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    @Override
    public Collection<T> executeSelectAllByParam(Connection connection, String sql, String param, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, param);
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
            return result;
        } catch (SQLException e) {
            connection.rollback(savepoint);
            throw e;
        }
    }


    @Override
    public boolean executeDelete(Connection connection, String sql, long id) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("DeleteSavePoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            boolean result = preparedStatement.executeUpdate() != 0;
            connection.commit();
            return result;
        } catch (SQLException e) {
            connection.rollback(savepoint);
            throw e;
        }
    }

    @Override
    public Collection<T> executeSelectAll(Connection connection, String sql, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            return rsHandler.apply(rs);
        }
    }
}
