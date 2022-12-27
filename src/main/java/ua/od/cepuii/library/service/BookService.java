package ua.od.cepuii.library.service;

import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.util.Collection;

public class BookService {
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();

    public long create(Book book) {
        return 0;
    }

    public boolean update(Book book) {
        return false;
    }

    public boolean delete(long id) {
        return false;
    }

    public Collection<Book> findBy(String fieldName, String value) {

        return null;
    }

    Collection<Book> getAll(int currentPage) {

        return null;
    }
}
