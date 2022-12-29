package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.sql.SQLException;
import java.util.Collection;

public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
    BookRepository bookRepository = repositoryFactory.getBookRepository();

    public long create(Book book) {
        return 0;
    }

    public boolean update(Book book) {
        return false;
    }

    public boolean delete(long id) throws SQLException {

        return bookRepository.delete(id);
    }

    public Collection<Book> findBy(String fieldName, String value) throws SQLException {
        Collection<Book> books;
        log.info("get by %s with param: %s", fieldName, value);
        if (fieldName.equals("title")) {
            books = bookRepository.getByTitle(value);
        } else {
            books = bookRepository.getByAuthor(value);
        }

        return books;
    }

    public Collection<Book> getAll(int currentPage) throws SQLException {
        return bookRepository.getAll();
    }
}
