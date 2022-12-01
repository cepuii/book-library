package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.Book;

import java.sql.SQLException;
import java.util.Collection;

public interface BookRepository extends AbstractEntityRepository<Book> {

    Collection<Book> getByTitle(String title) throws SQLException;

    Collection<Book> getByAuthor(String author) throws SQLException;

}
