package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.BookFilterParam;
import ua.od.cepuii.library.dto.Page;
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
        log.info(String.format("get by %s with param: %s", fieldName, value));
        if (fieldName.equals("title")) {
            books = bookRepository.getByTitle(value);
        } else {
            books = bookRepository.getByAuthor(value);
        }

        return books;
    }

    public int getPageSettings(Page page, BookFilterParam filterParam) {
        int recordsAmount = bookRepository.getCount(filterParam);
        int pageAmount = (int) Math.ceil((recordsAmount * 1.0) / page.getNoOfRecords());
        log.info("get records amount: {}", pageAmount);
        return pageAmount;
    }

    public Collection<Book> getAll(Page currentPage, BookFilterParam filterParam) throws SQLException {
        String orderBy = filterParam.getOrderBy() + (filterParam.isDescending() ? " DESC" : "");
        int limit = currentPage.getNoOfRecords();
        int offset = currentPage.getNoOfRecords() * (currentPage.getCurrentPage() - 1);
        log.info(String.format("getAll books:filter %s ; %s order %s, descending %b, limit %d , offset %d",
                filterParam.getTitle(), filterParam.getAuthor(), filterParam.getOrderBy(), filterParam.isDescending(), limit, offset));
        return bookRepository.getAllWithFilter(orderBy, filterParam.isDescending(), limit, offset, filterParam);
    }
}
