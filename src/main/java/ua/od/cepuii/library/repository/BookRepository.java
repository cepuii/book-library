package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Book;

public interface BookRepository extends AbstractEntityRepository<Book> {
    int getCount(FilterParams filterParam);

    boolean isExistTitle(String title);
}
