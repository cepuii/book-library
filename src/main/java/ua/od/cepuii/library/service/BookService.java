package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;
import ua.od.cepuii.library.util.ValidationUtil;

import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BookService implements Service {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
    BookRepository bookRepository = repositoryFactory.getBookRepository();

    public long create(Book book) throws SQLException {
        return bookRepository.insert(book);
    }

    public boolean createOrUpdate(Book book) throws SQLException {
        if (ValidationUtil.isNew(book)) {
            long insert = bookRepository.insert(book);
            log.info("book create and save, bookId: {}", insert);
            return true;
        }
        return bookRepository.update(book);
    }

    public boolean delete(long id) throws SQLException {
        return bookRepository.delete(id);
    }

    public BookTO getById(long id) throws SQLException {
        Optional<Book> bookOpt = bookRepository.getById(id);
        return Mapper.getBookTO(bookOpt.orElseThrow(NoSuchElementException::new));
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

    public boolean addAuthor(long bookId, Author author) throws SQLException {
        return bookRepository.addAuthor(bookId, author);
    }

    @Override
    public int getPageAmount(Page page, FilterAndSortParams filterParam) {
        int recordsAmount = bookRepository.getCount(filterParam);
        log.info("get records amount: {}", recordsAmount);
        int pageAmount = (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
        log.info("get page amount: {}", pageAmount);
        return pageAmount;
    }

    public Collection<Book> getAll(Page currentPage, FilterAndSortParams filterParam) throws SQLException {
        String orderBy = (filterParam.getOrderBy().isBlank() ? "b_title" : filterParam.getOrderBy()) + (filterParam.isDescending() ? " DESC" : "");
        int limit = currentPage.getNoOfRecords();
        int offset = currentPage.getNoOfRecords() * (currentPage.getCurrentPage() - 1);
        log.info(String.format("getAll books:filter %s ; %s order %s, descending %b, limit %d , offset %d",
                filterParam.getFirstParam(), filterParam.getSecondParam(), filterParam.getOrderBy(), filterParam.isDescending(), limit, offset));
        return bookRepository.getAll(filterParam, orderBy, limit, offset);
    }
}
