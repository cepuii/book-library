package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.Loan;

import java.util.Collection;

public interface LoanRepository extends AbstractEntityRepository<Loan> {
    Collection<Loan> getAllByUserId(long userId, int limit, int offset);

    boolean updateStatus(Loan loan);

    Collection<Long> getBooksIdsByUserId(long userId);

    Collection<Loan> getLoanHistory(long userId, int limit, int offset);

    void updateFine();
}
