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

public class BookService implements Service {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


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

    public Report delete(long id) {
        Report report = Report.newInstance();
        if (bookRepository.delete(id)) {
            report.addReport("success", "message.book.delete");
        } else {
            report.addError("wrongAction", "message.wrongAction.delete");
        }
        return report;
    }

    public BookTO getById(long id) {
        Optional<Book> bookOpt = bookRepository.getById(id);
        return Mapper.getBookTO(bookOpt.orElseThrow(NoSuchElementException::new));
    }

    @Override
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = bookRepository.getCount(filterParam);
        int noOfRecords = page.getNoOfRecords();
        return (recordsAmount % noOfRecords) == 0 ? (recordsAmount / noOfRecords) : (1 + (recordsAmount / noOfRecords));
    }

    public Collection<BookTO> getAll(Page page, FilterParams filterParam) {
        String orderBy = (filterParam.getOrderBy().isBlank() ? "b_title" : filterParam.getOrderBy()) + (filterParam.isDescending() ? " DESC" : "");
        Collection<Book> books = bookRepository.getAll(filterParam, orderBy, page.getLimit(), page.getOffset());
        return Mapper.mapToBookTo(books);
    }

    public Report isExistTitle(String title) {
        Report report = Report.newInstance();
        if (bookRepository.isExistTitle(title)) {
            report.addError("wrongAction", "message.book.title.exist");
        }
        return report;
    }
}
