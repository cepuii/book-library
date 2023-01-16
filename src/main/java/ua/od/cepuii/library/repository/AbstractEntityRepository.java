package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
//Layer supertype https://martinfowler.com/eaaCatalog/layerSupertype.html
public interface AbstractEntityRepository<T extends AbstractEntity> {

    long insert(T entity) throws SQLException;

    Optional<T> getById(long id) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(long id) throws SQLException;

    Collection<T> getAll(FilterAndSortParams params, String orderBy, int limit, int offset);
}
