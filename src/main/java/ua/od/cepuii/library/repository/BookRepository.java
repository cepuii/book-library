package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.BookFilterParam;
import ua.od.cepuii.library.entity.Book;

import java.sql.SQLException;
import java.util.Collection;

public interface BookRepository extends AbstractEntityRepository<Book> {

    Collection<Book> getByTitle(String title) throws SQLException;

    Collection<Book> getByAuthor(String author) throws SQLException;

    Collection<Book> getAllWithLimit(int limit, int offset) throws SQLException;

    int getCount(BookFilterParam filterParam);
}
