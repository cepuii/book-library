package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.entity.enums.PublicationType;
import ua.od.cepuii.library.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

/**
 * This class provides utility methods for filling objects with data from the database.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class RepositoryUtil {

    private static final Logger log = LoggerFactory.getLogger(RepositoryUtil.class);

    private RepositoryUtil() {
    }

    /**
     * Returns a collection of Book objects filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Book data
     * @return a collection of Book objects
     */
    public static Collection<Book> fillBooks(ResultSet resultSet) {
        Collection<Book> books = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Book book = getBook(resultSet).orElseThrow();
                books.add(book);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("fill books, size {}", books.size());
        return books;
    }

    /**
     * Returns an Optional Book object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Book data
     * @return an Optional Book object
     */
    public static Optional<Book> getBook(ResultSet resultSet) {
        try {
            long id = resultSet.getLong("b_id");
            Book book = Book.builder()
                    .id(id)
                    .title(resultSet.getString("b_title"))
                    .publicationType(PublicationType.valueOf(resultSet.getString("pt_name")))
                    .datePublication(resultSet.getInt("b_date"))
                    .total(resultSet.getInt("b_total"))
                    .authors(fillAuthors(resultSet))
                    .build();
            return Optional.of(book);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Returns a collection of Author objects filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Author data
     * @return a collection of Author objects
     */
    public static Collection<Author> fillAuthors(ResultSet resultSet) {

        Collection<Author> authorSet = new TreeSet<>(Comparator.comparing(Author::getName));
        try {
            String[] authorsIds = resultSet.getString("authors_id").split(", ");
            String[] authors = resultSet.getString("authors").split(", ");
            for (int i = 0; i < authorsIds.length; i++) {
                authorSet.add(new Author(Long.parseLong(authorsIds[i]), authors[i]));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authorSet;
    }

    /**
     * Returns an Optional Author object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Author data
     * @return an Optional Author object
     */
    public static Optional<Author> fillAuthor(ResultSet resultSet) {
        try {
            return Optional.of(new Author(resultSet.getInt("a_id"), resultSet.getString("a_name")));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Returns an Optional User object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the User data
     * @return an Optional User object
     */
    public static Optional<User> fillUser(ResultSet resultSet) {
        try {
            resultSet.next();
            return getUser(resultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Returns an Optional User object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the User data
     * @return an Optional User object
     * @throws SQLException if a database access error occurs
     */
    private static Optional<User> getUser(ResultSet resultSet) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("users_id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .dateTime(resultSet.getTimestamp("registered").toLocalDateTime())
                .blocked(resultSet.getBoolean("blocked"))
                .role(Role.valueOf(resultSet.getString("role")))
                .fine(resultSet.getInt("fine"))
                .build();
        return Optional.of(user);
    }

    /**
     * Returns a collection of Users objects filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the User data
     * @return a collection of User objects
     */
    public static Collection<User> fillUsers(ResultSet resultSet) {
        try {
            Collection<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(getUser(resultSet).orElseThrow());
            }
            log.info("populate users: {}", users.size());
            return users;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Returns an Optional Loan object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Loan data
     * @return an Optional Loan object
     */
    public static Optional<Loan> fillLoan(ResultSet resultSet) {
        try {
            resultSet.next();
            return getLoan(resultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Returns an Optional Loan object filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Loan data
     * @return an Optional Loan object
     * @throws SQLException if a database access error occurs
     */
    private static Optional<Loan> getLoan(ResultSet resultSet) throws SQLException {
        Loan loan = Loan.builder()
                .bookId(resultSet.getInt("l_bookId"))
                .userId(resultSet.getInt("l_userId"))
                .startDate(toLocalDate(resultSet.getTimestamp("l_start_time")))
                .duration(resultSet.getInt("l_duration"))
                .bookInfo(resultSet.getString("b_title") + ", " + resultSet.getInt("b_date"))
                .status(LoanStatus.valueOf(resultSet.getString("l_status")))
                .userEmail(resultSet.getString("u_email"))
                .fine(resultSet.getInt("b_fine"))
                .build();
        log.info("loan load from db {}", loan);
        loan.setId(resultSet.getInt("l_id"));
        return Optional.of(loan);
    }

    private static LocalDate toLocalDate(Timestamp startTime) {
        return startTime.toLocalDateTime().toLocalDate();
    }

    /**
     * Returns a collection of Loan objects filled with data from the specified ResultSet.
     *
     * @param resultSet the ResultSet containing the Loan data
     * @return a collection of Loan objects
     */
    public static Collection<Loan> fillLoans(ResultSet resultSet) {
        Collection<Loan> loans = new HashSet<>();
        try {
            while (resultSet.next()) {
                Optional<Loan> loan = getLoan(resultSet);
                loans.add(loan.orElseThrow());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return loans;
    }


}