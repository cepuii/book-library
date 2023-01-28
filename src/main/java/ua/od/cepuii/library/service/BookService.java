package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BookService implements Service {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public boolean createOrUpdate(Book book) {
        if (ValidationUtil.isNew(book)) {
            long insert = bookRepository.insert(book);
            log.info("book create and save, bookId: {}", insert);
            return true;
        }
        return bookRepository.update(book);
    }

    public boolean delete(long id) {
        return bookRepository.delete(id);
    }

    public BookTO getById(long id) {
        Optional<Book> bookOpt = bookRepository.getById(id);
        return Mapper.getBookTO(bookOpt.orElseThrow(NoSuchElementException::new));
    }

    @Override
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = bookRepository.getCount(filterParam);
        log.info("get records amount: {}", recordsAmount);
        int noOfRecords = page.getNoOfRecords();
        int pageAmount = (recordsAmount % noOfRecords) == 0 ? (recordsAmount / noOfRecords) : (1 + (recordsAmount / noOfRecords));
        log.info("get page amount: {}", pageAmount);
        return pageAmount;
    }

    public Collection<BookTO> getAll(Page page, FilterParams filterParam) {
        String orderBy = (filterParam.getOrderBy().isBlank() ? "b_title" : filterParam.getOrderBy()) + (filterParam.isDescending() ? " DESC" : "");
        log.info("getAll books:filter {}; {}; order {}, descending {}, limit {}, offset {}",
                filterParam.getFirstParam(), filterParam.getSecondParam(), filterParam.getOrderBy(), filterParam.isDescending(), page.getLimit(), page.getOffset());
        Collection<Book> books = bookRepository.getAll(filterParam, orderBy, page.getLimit(), page.getOffset());
        return Mapper.mapToBookTo(books);
    }

    public boolean isExistTitle(Book book) {
        Optional<Book> bookOptional = bookRepository.getByTitle(book.getTitle());
        return bookOptional.isPresent();
    }
}
