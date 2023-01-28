package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Book;

import java.util.Optional;

public interface BookRepository extends AbstractEntityRepository<Book> {
    int getCount(FilterParams filterParam);

    Optional<Book> getByTitle(String title);
}
