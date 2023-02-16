package ua.od.cepuii.library.repository.jdbc.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * QueryExecutorImpl is an implementation of the {@link QueryExecutor} interface. It provides methods
 * for executing various types of SQL queries, such as inserting, updating, and selecting data.
 *
 * @param <T> the type of {@link AbstractEntity} that this executor handles
 */
public class QueryExecutorImpl<T extends AbstractEntity> implements QueryExecutor<T> {

    private static final Logger log = LoggerFactory.getLogger(QueryExecutorImpl.class);

    /**
     * Inserts a new record into the database using the given SQL query and parameters.
     *
     * @param connection the {@link Connection} to use to execute the query
     * @param sql        the SQL query to execute
     * @param params     the parameters to use in the query
     * @return the generated ID of the new record
     * @throws SQLException if a database error occurs while executing the query
     */
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

    /**
     * Helper method to set the parameters in a {@link PreparedStatement}.
     *
     * @param params            the parameters to set
     * @param preparedStatement the {@link PreparedStatement} to set the parameters in
     * @throws SQLException if a database error occurs while setting the parameters
     */
    private static void setParams(List<Object> params, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }
    }

    /**
     * Selects a single record from the database based on the given parameters.
     *
     * @param connection the {@link Connection} to use to execute the query
     * @param sql        the SQL query to execute
     * @param params     the parameters to use in the query
     * @param rsHandler  a {@link Function} that processes the {@link ResultSet} and returns the desired result
     * @return an {@link Optional} containing the result of the query, or an empty Optional if no result was found
     * @throws SQLException if a database error occurs while executing the query
     */
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

    /**
     * Inserts data into a database table without generating a key for the inserted row.
     *
     * @param connection The Connection object used to execute the query.
     * @param sql        The SQL string to be executed.
     * @param params     A list of parameters to be set in the prepared statement.
     * @return true if the data was inserted, false otherwise.
     * @throws SQLException if an error occurs while executing the query.
     */
    @Override
    public boolean insertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            log.info("{}", preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    /**
     * Updates data in a database table.
     *
     * @param connection The Connection object used to execute the query.
     * @param sql        The SQL string to be executed.
     * @param params     A list of parameters to be set in the prepared statement.
     * @return true if the data was updated, false otherwise.
     * @throws SQLException if an error occurs while executing the query.
     */
    @Override
    public boolean update(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            boolean result = preparedStatement.executeUpdate() != 0;
            log.info("{}", preparedStatement);
            return result;
        }
    }

    /**
     * Checks if a result exists in the database for the given id.
     *
     * @param connection The Connection object used to execute the query.
     * @param sql        The SQL string to be executed.
     * @param id         The id to be searched for.
     * @return true if the result exists, false otherwise.
     * @throws SQLException if an error occurs while executing the query.
     */
    @Override
    public boolean isExistResultById(Connection connection, String sql, long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            log.info("{}", preparedStatement);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    /**
     * Checks if a result exists in the database for the given string value.
     *
     * @param connection The Connection object used to execute the query.
     * @param sql        The SQL string to be executed.
     * @param value      The string value to be searched for.
     * @return true if the result exists, false otherwise.
     * @throws SQLException if an error occurs while executing the query.
     */
    @Override
    public boolean isExistResultByString(Connection connection, String sql, String value) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            log.info("{}", preparedStatement);
            return preparedStatement.executeQuery().next();
        }
    }

    /**
     * Selects all records from a database and returns them in a collection of entities.
     *
     * @param connection The {@link Connection} to the database.
     * @param sql        The SQL query to be executed.
     * @param params     A list of parameters to be passed to the query.
     * @param rsHandler  A {@link Function} that maps a {@link ResultSet} to a collection of entities.
     * @return A collection of entities representing the results of the query.
     * @throws SQLException If an error occurs while executing the query.
     */
    @Override
    public Collection<T> selectAll(Connection connection, String sql, List<Object> params, Function<ResultSet, Collection<T>> rsHandler) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(params, preparedStatement);
            log.info("{}", preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }

    /**
     * Selects the count of records from a database.
     *
     * @param connection The {@link Connection} to the database.
     * @param sqlQuery   The SQL query to be executed.
     * @param strings    A list of parameters to be passed to the query.
     * @return The count of records in the database.
     * @throws SQLException If an error occurs while executing the query.
     */
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