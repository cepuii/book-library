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

public class LoanService implements Service {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

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

    public Report delete(long loanId, long bookId) {
        Report report = Report.newInstance();
        if (loanRepository.deleteAndDecreaseBookBorrow(loanId, bookId)) {
            report.addReport(SUCCESS, "message.addOrder.success");
        } else {
            report.addError(WRONG_ACTION, "message.somethingWrong.create");
        }
        return report;
    }

    public Collection<LoanTO> getAll(FilterParams filter, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAll(filter, filter.getOrderBy(), page.getLimit(), page.getOffset()));
    }

    public Collection<LoanTO> getAllByUserId(long userId, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAllByUserId(userId, page.getLimit(), page.getOffset()));
    }

    public Report setOrderStatus(Loan loan, boolean fineSubtract) {
        Report report = Report.newInstance();
        if (loanRepository.updateStatus(loan, fineSubtract)) {
            report.addReport(SUCCESS, "message.order.setStatus");
        } else {
            report.addError(WRONG_ACTION, "message.somethingWrong.setStatus");
        }
        return report;
    }

    public Collection<Long> getBooksIdsByUserId(long userId) {
        return loanRepository.getBooksIdsByUserId(userId);
    }


    public Collection<LoanTO> getLoanHistory(long userId, Page page) {
        Collection<Loan> loanHistory = loanRepository.getLoanHistory(userId, page.getLimit(), page.getOffset());
        return Mapper.mapToLoanTO(loanHistory);
    }

    public void updateFine() {
        loanRepository.updateFine();
        log.info("update users fines");
    }

    @Override
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = loanRepository.getCount(filterParam);
        return (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
    }
}
