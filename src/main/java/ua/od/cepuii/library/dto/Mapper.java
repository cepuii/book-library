package ua.od.cepuii.library.dto;

import ua.od.cepuii.library.entity.Loan;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class Mapper {

    private Mapper() {
    }

    public static Collection<LoanTO> mapToLoanTO(Collection<Loan> loans) {
        return loans.stream().map(Mapper::getLoanTO).collect(Collectors.toList());
    }

    public static LoanTO getLoanTO(Loan loan) {
        LocalDate startDate = loan.getStartDate();
        Date start = getDate(startDate);
        Date end = getDate(startDate.plusDays(loan.getDuration()));
        return LoanTO.builder()
                .id(loan.getId())
                .bookId(loan.getBookId())
                .userId(loan.getUserId())
                .startDate(start)
                .endDate(end)
                .bookInfo(loan.getBookInfo())
                .status(loan.getStatus().toString())
                .build();
    }

    private static Date getDate(LocalDate startDate) {
        return new Date((startDate.getYear() - 1900), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
    }

}
