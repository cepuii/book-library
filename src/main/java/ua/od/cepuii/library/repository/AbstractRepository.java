package ua.od.cepuii.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An abstract repository class that implements common CRUD operations for entities
 * that extend the AbstractEntity class.
 *
 * @param <T> the type of the entity being managed by the repository.
 */
public abstract class AbstractRepository<T extends AbstractEntity> implements EntityRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractRepository.class);
    protected final ConnectionPool connectionPool;

    protected AbstractRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Inserts the given entity into the database and returns its ID. If the insertion fails,
     * this method returns -1.
     *
     * @param entity the entity to insert.
     * @return the ID of the inserted entity, or -1 if the insertion fails.
     */
    @Override
    public long insert(T entity) {

        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            long newEntityId = insertAndGetId(connection, entity);

            if (newEntityId != -1) {
                connection.commit();
                return newEntityId;
            }

            connection.rollback();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    /**
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection the connection to use for accessing the database.
     * @param entity     the entity to insert.
     * @return the ID of the inserted entity, or -1 if the insertion fails.
     * @throws SQLException if a database error occurs.
     */
    protected abstract long insertAndGetId(Connection connection, T entity) throws SQLException;

    /**
     * Returns the total count of entities that match the given set of filter parameters.
     *
     * @param filterParam the filter parameters to use for counting entities.
     * @return the total count of entities that match the given set of filter parameters.
     */
    @Override
    public int getCount(FilterParams filterParam) {
        try (Connection connection = connectionPool.getConnection()) {
            return getCount(connection, filterParam);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    /**
     * Returns the total count of entities that match the given set of filter parameters.
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection            the connection to use for accessing the database.
     * @param filterParamsForSearch the filter parameters to use for counting entities.
     * @return the total count of entities that match the given set of filter parameters.
     * @throws SQLException if a database error occurs.
     */
    protected abstract int getCount(Connection connection, FilterParams filterParamsForSearch) throws SQLException;

    /**
     * Returns the entity with the given ID, or Optional.empty() if no such entity exists.
     *
     * @param id the ID of the entity to retrieve.
     * @return the entity with the given ID, or Optional.empty() if no such entity exists.
     */
    @Override
    public Optional<T> getById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            return selectById(connection, id);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Returns the entity with the given ID, or Optional.empty() if no such entity exists.
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection the connection to use for accessing the database
     * @param id         the ID of the entity to retrieve.
     */
    protected abstract Optional<T> selectById(Connection connection, long id) throws SQLException;

    /**
     * Updates the given entity in the repository.
     *
     * @param entity The entity to update.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     */
    @Override
    public boolean update(T entity) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            boolean executeUpdate = update(connection, entity);
            if (executeUpdate) {
                connection.commit();
                return true;
            }
            connection.rollback();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * Updates the given entity in the repository using the given database connection.
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection The database connection to use for the update.
     * @param entity     The entity to update.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     * @throws SQLException If there was an error executing the update statement.
     */
    protected abstract boolean update(Connection connection, T entity) throws SQLException;

    /**
     * Deletes the entity with the given ID from the repository.
     *
     * @param id The ID of the entity to delete.
     * @return {@code true} if the deleted was successful, {@code false} otherwise.
     */
    @Override
    public boolean delete(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            if (delete(connection, id)) {
                connection.commit();
                return true;
            }
            connection.rollback();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * Deletes the entity with the given ID from the repository using the given database connection.
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection The database connection to use for the deleted.
     * @param id         The ID of the entity to delete.
     * @return {@code true} if the deleted was successful, {@code false} otherwise.
     */
    protected abstract boolean delete(Connection connection, long id);

    /**
     * Retrieves a collection of entities from the repository that match the given filter parameters,
     * ordered by the given column, with the given limit and offset.
     *
     * @param params  The filter parameters to use for the query.
     * @param orderBy The column to use for ordering the results.
     * @param limit   The maximum number of entities to return.
     * @param offset  The index of the first entity to return.
     * @return A collection of entities that match the given criteria.
     */
    @Override
    public Collection<T> getAll(FilterParams params, String orderBy, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {

            return selectAll(connection,
                    List.of(params.getFirstParamForQuery(), params.getSecondParamForQuery(), limit, offset), orderBy);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves a collection of entities from the repository that match the given filter parameters,
     * ordered by the given column, with the given limit and offset, using the given database connection.
     * Subclasses must implement this method to provide entity-specific insertion logic.
     *
     * @param connection The database connection to use for the query.
     * @param params     The filter parameters to use for the query.
     * @param orderBy    The column to use for ordering the results.
     * @return A collection of entities that match the given criteria.
     * @throws SQLException If there was an error executing the query.
     */
    protected abstract Collection<T> selectAll(Connection connection, List<Object> params, String orderBy) throws SQLException;
}
