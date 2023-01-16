package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;

import java.sql.SQLException;
import java.util.Collection;

public interface BookRepository extends AbstractEntityRepository<Book> {

    Collection<Book> getByTitle(String title) throws SQLException;

    Collection<Book> getByAuthor(String author) throws SQLException;

    int getCount(FilterAndSortParams filterParam);

    boolean addAuthor(long bookId, Author author) throws SQLException;
}
