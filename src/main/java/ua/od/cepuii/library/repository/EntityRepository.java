package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for the abstract entity repository.
 * These interface has methods to manage entities stored in the database and can perform CRUD operations.
 *
 * @param <T> The type of abstract entity this repository contains.
 */
public interface EntityRepository<T extends AbstractEntity> {

    /**
     * Inserts a new entity into the repository.
     *
     * @param entity The entity to insert.
     * @return The id of the newly inserted entity.
     */
    long insert(T entity);

    /**
     * Gets an entity from the repository by its id.
     *
     * @param id The id of the entity.
     * @return The entity, if found.
     */
    Optional<T> getById(long id);

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity The entity to update.
     * @return True if the update was successful.
     */
    boolean update(T entity);

    /**
     * Deletes an entity in the repository by its id.
     *
     * @param id The id of the entity to delete.
     * @return True if the deletion was successful.
     */
    boolean delete(long id);

    /**
     * Gets a collection of all entities from the repository that matches filter parameters,
     * a specified order, limit and offset.
     *
     * @param params  The filter parameters.
     * @param orderBy The order in which the entities should be returned.
     * @param limit   The maximum number of entities to be returned.
     * @param offset  The offset in which the entities should be returned.
     * @return The collection of entities.
     */
    Collection<T> getAll(FilterParams params, String orderBy, int limit, int offset);

    int getCount(FilterParams filterParam);
}
