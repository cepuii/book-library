package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.sql.SQLException;
import java.util.Collection;

public class LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
    private final LoanRepository loanRepository = repositoryFactory.getLoanRepository();

    public long create(Loan loan) {
        return loanRepository.insert(loan);
    }

    public boolean update(Loan loan) {

        return false;
    }

    public boolean delete(long id) throws SQLException {
        return loanRepository.delete(id);
    }

    public Collection<Loan> getByStatusRaw() {
        return null;
    }

    public Collection<LoanTO> getAll(FilterAndSortParams filter, Page currentPage) {
        //TODO create page parameter
        return Mapper.mapToLoanTO(loanRepository.getAll(filter, filter.getOrderBy(), 10, 0));
    }

    public Collection<LoanTO> getAllByUserId(long userId, Page currentPage) {
        //TODO create page parameter
        return Mapper.mapToLoanTO(loanRepository.getAllByUserId(userId, 10, 0));
    }

    public Collection<Loan> getByReader(long userId) {
        return null;
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
}
