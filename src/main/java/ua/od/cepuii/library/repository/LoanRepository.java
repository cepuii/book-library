package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;

import java.util.Collection;

public interface LoanRepository extends AbstractEntityRepository<Loan> {
    Collection<Loan> getAllByUserId(long userId, int limit, int offset);

    boolean updateStatus(long loanId, LoanStatus status);
}
