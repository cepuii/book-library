package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.Loan;

import java.sql.SQLException;
import java.util.Collection;

public interface LoanRepository extends AbstractEntityRepository<Loan> {
    Collection<Loan> getAllByUserId(long userId, int limit, int offset) throws SQLException;
}
