package ua.od.cepuii.library.dto;

import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        Date start = getDate(startDate.atStartOfDay());
        Date end = getDate(startDate.plusDays(loan.getDuration()).atStartOfDay());
        return LoanTO.builder()
                .id(loan.getId())
                .bookId(loan.getBookId())
                .userId(loan.getUserId())
                .startDate(start)
                .endDate(end)
                .fine(loan.getFine())
                .bookInfo(loan.getBookInfo())
                .status(loan.getStatus().toString())
                .build();
    }

    private static Date getDate(LocalDateTime startDate) {
        return Date.from(startDate.toInstant(ZoneOffset.UTC));
    }

    public static BookTO getBookTO(Book book) {
        return BookTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .publicationType(book.getPublicationType())
                .datePublication(book.getDatePublication())
                .authors(book.getAuthors())
                .total(book.getTotal())
                .fine(book.getFine())
                .build();
    }

    //TODO add locale from session
    public static UserTO getUserTO(User user) {
        return UserTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .registered(getDate(user.getDateTime()))
                .blocked(user.isBlocked())
                .role(user.getRole())
                .fine(user.getFine())
                .build();
    }

    public static Collection<UserTO> mapToUserTO(Collection<User> users) {
        return users.stream().map(Mapper::getUserTO).collect(Collectors.toList());
    }

    public static Collection<BookTO> mapToBookTo(Collection<Book> books) {
        return books.stream().map(Mapper::getBookTO).collect(Collectors.toList());
    }
}
