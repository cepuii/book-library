package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.*;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.LoanRepository;

import java.util.Collection;
import java.util.Optional;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * A service class for managing loans. Implements the Pageable interface for pagination support.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class LoanService implements Pageable {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new loan and returns a report indicating success or failure.
     * If the duration of the loan is 0, an error is added to the report.
     * If the book with the given bookId is not found, an error is added to the report.
     * If the loan is created successfully, a report containing the loan ID and a success message is returned.
     *
     * @param loan the loan to create
     * @return a report indicating success or failure
     */
    public Report create(Loan loan) {
        Report report = Report.newInstance();
        if (loan.getDuration() == 0) {
            report.addError(WRONG_ACTION, "message.wrongDuration");
        } else {
            Optional<Book> book = bookRepository.getById(loan.getBookId());
            if (book.isEmpty()) {
                report.addError(WRONG_ACTION, "message.book.finished");
            }
        }
        if (!report.hasErrors()) {
            long insert = loanRepository.insert(loan);
            if (insert == -1) {
                report.addError(WRONG_ACTION, "message.somethingWrong.create");
            } else {
                report.addReport(LOAN_ID, String.valueOf(insert));
                report.addReport(SUCCESS, "message.addOrder.success");
            }
        }
        return report;
    }

    /**
     * Deletes a loan with the given loan ID and updates the book borrow count.
     * If the deletion is successful, a report containing a success message is returned.
     * If the deletion fails, an error is added to the report.
     *
     * @param loanId the ID of the loan to delete
     * @param bookId the ID of the book to update the borrow count for
     * @return a report indicating success or failure
     */
    public Report delete(long loanId, long bookId) {
        Report report = Report.newInstance();
        if (loanRepository.deleteAndDecreaseBookBorrow(loanId, bookId)) {
            report.addReport(SUCCESS, "message.addOrder.success");
        } else {
            report.addError(WRONG_ACTION, "message.somethingWrong.create");
        }
        return report;
    }

    /**
     * Retrieves all loans that match the given filter and paging parameters, and returns them
     * as LoanTO objects using the Mapper class.
     *
     * @param filter the filter parameters to use
     * @param page   the paging parameters to use
     * @return a collection of LoanTO objects
     */
    public Collection<LoanTO> getAll(FilterParams filter, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAll(filter, filter.getOrderBy(), page.getLimit(), page.getOffset()));
    }

    /**
     * Retrieves all loans for the given user ID and paging parameters, and returns them
     * as LoanTO objects using the Mapper class.
     *
     * @param userId the ID of the user to retrieve loans for
     * @param page   the paging parameters to use
     * @return a collection of LoanTO objects
     */
    public Collection<LoanTO> getAllByUserId(long userId, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAllByUserId(userId, page.getLimit(), page.getOffset()));
    }

    /**
     * Updates the status of a loan and optionally subtracts the user's fine.
     * If the update is successful, a report containing a success message is returned.
     * If the update fails, an error is added to the report.
     *
     * @param loan         the loan to update
     * @param fineSubtract whether or not to subtract the user's fine
     * @return a report indicating success or failure
     */
    public Report setOrderStatus(Loan loan, boolean fineSubtract) {
        Report report = Report.newInstance();
        if (loanRepository.updateStatus(loan, fineSubtract)) {
            report.addReport(SUCCESS, "message.order.setStatus");
        } else {
            report.addError(WRONG_ACTION, "message.somethingWrong.setStatus");
        }
        return report;
    }

    /**
     * Retrieves the IDs of all books borrowed by the given user.
     *
     * @param userId the ID of the user to retrieve book IDs for
     * @return a collection of book IDs
     */
    public Collection<Long> getBooksIdsByUserId(long userId) {
        return loanRepository.getBooksIdsByUserId(userId);
    }

    /**
     * Retrieves the loan history for the given user ID and paging parameters, and returns them
     * as LoanTO objects using the Mapper class.
     *
     * @param userId the ID of the user to retrieve loan history for
     * @param page   the paging parameters to use
     * @return a collection of LoanTO objects
     */
    public Collection<LoanTO> getLoanHistory(long userId, Page page) {
        Collection<Loan> loanHistory = loanRepository.getLoanHistory(userId, page.getLimit(), page.getOffset());
        return Mapper.mapToLoanTO(loanHistory);
    }

    /**
     * Updates the fines for all users who have overdue loans.
     */
    public void updateFine() {
        loanRepository.updateFine();
        log.info("update users fines");
    }

    /**
     * Returns the number of pages required to display all loans that match the given filter and paging parameters.
     *
     * @param page        the paging parameters to use
     * @param filterParam the filter parameters to use
     * @return the number of pages required to display the loans
     */
    @Override
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = loanRepository.getCount(filterParam);
        return (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
    }
}
