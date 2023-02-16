package ua.od.cepuii.library.repository.jdbc.executor;

import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * QueryExecutor interface is used to execute various queries to interact with a database.
 * It has various methods such as insert, update, selectByParams, etc. to perform the corresponding operations on the database.
 *
 * @param <T> the type of {@link AbstractEntity} this executor can operate on.
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface QueryExecutor<T extends AbstractEntity> {
    /**
     * This method inserts a new record into the database.
     *
     * @param connection the {@link Connection} instance to interact with the database.
     * @param sql        the SQL query to insert a new record.
     * @param params     the parameters to be passed along with the SQL query.
     * @return the id of the inserted record.
     * @throws SQLException if there is any error while executing the SQL query.
     */
    long insert(Connection connection, String sql, List<Object> params) throws SQLException;

    /**
     * This method inserts a new record into the database without returning the generated id.
     *
     * @param connection the {@link Connection} instance to interact with the database.
     * @param sql        the SQL query to insert a new record.
     * @param params     the parameters to be passed along with the SQL query.
     * @return true if the insertion was successful, false otherwise.
     * @throws SQLException if there is any error while executing the SQL query.
     */
    boolean insertWithoutGeneratedKey(Connection connection, String sql, List<Object> params) throws SQLException;

    /**
     * This method retrieves a single record from the database based on the specified parameters.
     *
     * @param connection the {@link Connection} instance to interact with the database.
     * @param sql        the SQL query to retrieve the record.
     * @param params     the parameters to be passed along with the SQL query.
     * @param rsHandler  the {@link Function} to handle the {@link ResultSet} and return an {@link Optional} of type T.
     * @return an {@link Optional} of type T representing the retrieved record.
     * @throws SQLException if there is any error while executing the SQL query.
     */
    Optional<T> selectByParams(Connection connection, String sql, List<Object> params, Function<ResultSet, Optional<T>> rsHandler) throws SQLException;

    /**
     * This method updates a record in the database.
     *
     * @param connection the {@link Connection} instance to interact with the database.
     * @param sql        the SQL query to update the record.
     * @param params     the parameters to be passed along with the SQL query.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if there is any error while executing the SQL query.
     */
    boolean update(Connection connection, String sql, List<Object> params) throws SQLException;

    /**
     * Executes a query that returns a boolean value, indicating whether a record with the specified id exists.
     *
     * @param connection The connection to the database.
     * @param sql        The SQL query to execute.
     * @param id         The id to use in the query.
     * @return A boolean value indicating whether a record with the specified id exists.
     * @throws SQLException If there is an error executing the query.
     */
    boolean isExistResultById(Connection connection, String sql, long id) throws SQLException;

    /**
     * Executes a query that returns a boolean value, indicating whether a record with the specified string value exists.
     *
     * @param connection The connection to the database.
     * @param sql        The SQL query to execute.
     * @param value      The string value to use in the query.
     * @return A boolean value indicating whether a record with the specified string value exists.
     * @throws SQLException If there is an error executing the query.
     */
    boolean isExistResultByString(Connection connection, String sql, String value) throws SQLException;

    /**
     * Executes a query that returns a collection of entities.
     *
     * @param connection The connection to the database.
     * @param sql        The SQL query to execute.
     * @param params     The parameters to use in the query.
     * @param rsHandler  A function that maps the ResultSet to a collection of entities.
     * @return A collection of entities.
     * @throws SQLException If there is an error executing the query.
     */
    Collection<T> selectAll(Connection connection, String sql, List<Object> params, Function<ResultSet, Collection<T>> rsHandler) throws SQLException;

    /**
     * Executes a query that returns the count of records that match a specified filter.
     *
     * @param connection           The connection to the database.
     * @param countSelectAllFilter The SQL query to execute.
     * @param strings              The parameters to use in the query.
     * @return The count of records that match the specified filter.
     * @throws SQLException If there is an error executing the query.
     */
    int selectCount(Connection connection, String countSelectAllFilter, List<Object> strings) throws SQLException;
}
