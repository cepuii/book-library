package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.AbstractEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface AbstractEntityRepository<T extends AbstractEntity> {

    long insert(T entity) throws SQLException;

    Optional<T> getById(long id) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(long id) throws SQLException;

    Collection<T> getAll() throws SQLException;
}
