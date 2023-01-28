package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.AbstractEntity;

import java.util.Collection;
import java.util.Optional;
//Layer supertype https://martinfowler.com/eaaCatalog/layerSupertype.html
public interface AbstractEntityRepository<T extends AbstractEntity> {

    long insert(T entity);

    Optional<T> getById(long id);

    boolean update(T entity);

    boolean delete(long id);

    Collection<T> getAll(FilterParams params, String orderBy, int limit, int offset);
}
