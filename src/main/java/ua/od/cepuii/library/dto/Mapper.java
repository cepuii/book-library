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

/**
 * The Mapper class provides methods for mapping entities to transfer objects.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class Mapper {

    private Mapper() {
    }

    /**
     * Maps a collection of loans to a collection of loan transfer objects.
     *
     * @param loans the collection of loans to be mapped
     * @return the collection of loan transfer objects
     */
    public static Collection<LoanTO> mapToLoanTO(Collection<Loan> loans) {
        return loans.stream().map(Mapper::getLoanTO).collect(Collectors.toList());
    }

    /**
     * Maps a loan to a loan transfer object.
     *
     * @param loan the loan to be mapped
     * @return the loan transfer object
     */
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
                .userEmail(loan.getUserEmail())
                .build();
    }

    private static Date getDate(LocalDateTime startDate) {
        return Date.from(startDate.toInstant(ZoneOffset.UTC));
    }

    /**
     * Maps a book to a book transfer object.
     *
     * @param book the book to be mapped
     * @return the book transfer object
     */
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

    /**
     * Maps a user to a user transfer object.
     *
     * @param user the user to be mapped
     * @return the user transfer object
     */
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

    /**
     * Maps a collection of users to a collection of user transfer objects.
     *
     * @param users the collection of users to be mapped
     * @return the collection of user transfer objects
     */
    public static Collection<UserTO> mapToUserTO(Collection<User> users) {
        return users.stream().map(Mapper::getUserTO).collect(Collectors.toList());
    }

    /**
     * Maps a collection of books to a collection of book transfer objects.
     *
     * @param books the collection of books to be mapped
     * @return the collection of book transfer objects
     */
    public static Collection<BookTO> mapToBookTo(Collection<Book> books) {
        return books.stream().map(Mapper::getBookTO).collect(Collectors.toList());
    }
}
