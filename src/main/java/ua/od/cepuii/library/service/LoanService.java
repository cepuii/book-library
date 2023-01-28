package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;
import ua.od.cepuii.library.resource.MessageManager;

import java.util.Collection;
import java.util.Optional;

public class LoanService implements Service {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    public long create(Loan loan) {
        Optional<Book> book = bookRepository.getById(loan.getBookId());
        if (book.isEmpty()) {
            throw new RepositoryException(MessageManager.getProperty("message.nullPage"));
        }
        return loanRepository.insert(loan);
    }

    public boolean delete(long loanId, long bookId) {
        return loanRepository.deleteAndDecreaseBookBorrow(loanId, bookId);
    }

    public Collection<LoanTO> getAll(FilterParams filter, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAll(filter, filter.getOrderBy(), page.getLimit(), page.getOffset()));
    }

    public Collection<LoanTO> getAllByUserId(long userId, Page page) {
        return Mapper.mapToLoanTO(loanRepository.getAllByUserId(userId, page.getLimit(), page.getOffset()));
    }

    public boolean setOrderStatus(Loan loan, boolean fineSubtract) {
        return loanRepository.updateStatus(loan, fineSubtract);
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
