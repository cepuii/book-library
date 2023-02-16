package ua.od.cepuii.library.entity.enums;

/**
 * Enum to represent the status of a loan.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public enum LoanStatus {
    /**
     * Loan is not processed.
     */
    RAW,
    /**
     * Loan is successfully processed and not overdue.
     */
    COMPLETE,
    /**
     * Loan is successfully processed and not overdue.
     */
    OVERDUE,
    /**
     * Loan is returned.
     */
    RETURNED;

}
