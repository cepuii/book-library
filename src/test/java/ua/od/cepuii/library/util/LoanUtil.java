package ua.od.cepuii.library.util;

import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;


public class LoanUtil {

    public static final long NOT_FOUND_ID = 999;
    public static final long NEW_LOAN_ID = 0;
    public static final long LOAN_ID = 1020;
    public static final long BOOK_ID = 1010;
    public static final long USER_ID = 1000;
    public static final String USER_EMAIL = "test@tast";

    public static final LocalDate START_DATE = LocalDate.of(2020, 2, 3);
    public static final int DURATION = 10;
    public static final LoanStatus STATUS = LoanStatus.COMPLETE;

    public static final String BOOK_INFO = "BOOK, 2020";
    public static final int FINE = 0;

    public static final Loan LOAN = Loan.builder()
            .id(LOAN_ID)
            .bookId(BOOK_ID)
            .userId(USER_ID)
            .startDate(START_DATE)
            .duration(DURATION)
            .status(STATUS)
            .bookInfo(BOOK_INFO)
            .userEmail(USER_EMAIL)
            .fine(FINE)
            .build();
    public static final Loan NEW_LOAN = Loan.builder()
            .id(NEW_LOAN_ID)
            .bookId(BOOK_ID)
            .userId(USER_ID)
            .startDate(START_DATE)
            .duration(DURATION)
            .status(STATUS)
            .bookInfo(BOOK_INFO)
            .fine(FINE)
            .build();
    public static final LoanTO LOAN_TO = LoanTO.builder()
            .id(LOAN_ID)
            .bookId(BOOK_ID)
            .userId(USER_ID)
            .startDate(Date.from(START_DATE.atStartOfDay().toInstant(ZoneOffset.MIN)))
            .endDate(Date.from(START_DATE.plusDays(DURATION).atStartOfDay().toInstant(ZoneOffset.MIN)))
            .status(STATUS.name())
            .bookInfo(BOOK_INFO)
            .userEmail(USER_EMAIL)
            .fine(FINE)
            .build();


}
