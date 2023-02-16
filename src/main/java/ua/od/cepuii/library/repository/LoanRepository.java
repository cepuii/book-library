package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.Loan;

import java.util.Collection;

/**
 * Interface for a repository of {@link Loan} entities.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface LoanRepository extends EntityRepository<Loan> {
    /**
     * Deletes a loan and decreases the count of borrowed books for the related
     * book.
     *
     * @param loanId the id of the loan to delete
     * @param bookId the id of the book related to the loan
     * @return true if the loan and book were successfully deleted and updated, false otherwise
     */
    boolean deleteAndDecreaseBookBorrow(long loanId, long bookId);

    /**
     * Gets a collection of loans for a given user id.
     *
     * @param userId the id of the user to retrieve loans for
     * @param limit  the maximum number of loans to retrieve
     * @param offset the number of loans to skip before starting to retrieve loans
     * @return a collection of loans for the given user id
     */
    Collection<Loan> getAllByUserId(long userId, int limit, int offset);

    /**
     * Updates the status of a loan and optionally subtracts the fine from the
     * user's balance.
     *
     * @param loan         the loan to update
     * @param fineSubtract true if the fine should be subtracted from the user's balance, false otherwise
     * @return true if the loan was successfully updated, false otherwise
     */
    boolean updateStatus(Loan loan, boolean fineSubtract);

    /**
     * Gets a collection of book ids for a given user id.
     *
     * @param userId the id of the user to retrieve book ids for
     * @return a collection of book ids for the given user id
     */
    Collection<Long> getBooksIdsByUserId(long userId);

    /**
     * Gets a collection of loan history for a given user id.
     *
     * @param userId the id of the user to retrieve loan history for
     * @param limit  the maximum number of loan history to retrieve
     * @param offset the number of loan history to skip before starting to retrieve loan history
     * @return a collection of loan history for the given user id
     */
    Collection<Loan> getLoanHistory(long userId, int limit, int offset);

    /**
     * Updates the fine for all loans.
     */
    void updateFine();

}
