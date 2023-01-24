package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.util.Collection;

public class LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public long create(Loan loan) {
        return loanRepository.insert(loan);
    }

    public boolean delete(long id) {
        return loanRepository.delete(id);
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
}
