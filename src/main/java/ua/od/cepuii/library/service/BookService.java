package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.*;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ua.od.cepuii.library.constants.AttributesName.SUCCESS;
import static ua.od.cepuii.library.constants.AttributesName.WRONG_ACTION;

/**
 * A service class for managing books. Implements the Pageable interface for pagination support.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class BookService implements Pageable {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creates or updates a book. Depends on book`s id, if it`s equal zero - book create, else book update.
     *
     * @param book the book to create or update
     * @return a report indicating whether the operation was successful or not
     */
    public Report createOrUpdate(Book book) {

        Report report = isExistTitle(book.getTitle());

        if (!report.hasErrors()) {
            if (ValidationUtil.isNew(book)) {
                long insert = bookRepository.insert(book);
                log.info("book create, bookId: {}", insert);
                report.addReport(SUCCESS, "message.book.add");
                return report;
            }

            if (bookRepository.update(book)) {
                report.addReport(SUCCESS, "message.book.save");
            } else {
                report.addError(WRONG_ACTION, "message.wrongAction.edit");
            }
        }
        return report;
    }

    /**
     * Deletes a book with the given ID.
     *
     * @param id the ID of the book to delete
     * @return a report indicating whether the operation was successful or not
     */
    public Report delete(long id) {
        Report report = Report.newInstance();
        if (bookRepository.delete(id)) {
            report.addReport("success", "message.book.delete");
        } else {
            report.addError("wrongAction", "message.wrongAction.delete");
        }
        return report;
    }

    /**
     * Gets the book with the given ID.
     *
     * @param id the ID of the book to get
     * @return the BookTO object corresponding to the book
     * @throws NoSuchElementException if the book with the given ID doesn't exist
     */
    public BookTO getById(long id) {
        Optional<Book> bookOpt = bookRepository.getById(id);
        return Mapper.getBookTO(bookOpt.orElseThrow(NoSuchElementException::new));
    }

    /**
     * Calculates the number of pages required to display all the books with the given filter parameters.
     *
     * @param page        the page object representing the current page
     * @param filterParam the filter parameters to apply
     * @return the number of pages required to display all the books
     */
    @Override
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = bookRepository.getCount(filterParam);
        int noOfRecords = page.getNoOfRecords();
        return (recordsAmount % noOfRecords) == 0 ? (recordsAmount / noOfRecords) : (1 + (recordsAmount / noOfRecords));
    }

    /**
     * Gets all the books with the given filter parameters.
     *
     * @param page        the page object representing the current page
     * @param filterParam the filter parameters to apply
     * @return a collection of BookTO objects corresponding to the books
     */
    public Collection<BookTO> getAll(Page page, FilterParams filterParam) {
        String orderBy = (filterParam.getOrderBy().isBlank() ? "b_title" : filterParam.getOrderBy()) + (filterParam.isDescending() ? " DESC" : "");
        Collection<Book> books = bookRepository.getAll(filterParam, orderBy, page.getLimit(), page.getOffset());
        return Mapper.mapToBookTo(books);
    }

    /**
     * Check if the book with the specified title already exists.
     *
     * @param title the title of the book to check
     * @return a report indicating whether book exists or not
     */
    public Report isExistTitle(String title) {
        Report report = Report.newInstance();
        if (bookRepository.isExistTitle(title)) {
            report.addError("wrongAction", "message.book.title.exist");
        }
        return report;
    }
}
