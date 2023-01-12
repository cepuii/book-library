package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public long create(Loan loan) throws SQLException {
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

    public Collection<Loan> getAll(int currentPage) {
        return null;
    }

    public Collection<LoanTO> getAllByUserId(long userId, Page currentPage) throws SQLException {
        return Mapper.mapToLoanTO(loanRepository.getAllByUserId(userId, 10, 0));
    }

    public Collection<Loan> getByReader(long userId) {
        return null;
    }
}
