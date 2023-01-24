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

public class RepositoryUtil {

    private static final Logger log = LoggerFactory.getLogger(RepositoryUtil.class);

    private RepositoryUtil() {
    }


    public static Collection<Book> fillBooks(ResultSet resultSet) {
        Collection<Book> books = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                long id = resultSet.getLong("b_id");
                Book book = Book.builder()
                        .id(id)
                        .title(resultSet.getString("b_title"))
                        .publicationType(PublicationType.valueOf(resultSet.getString("pt_name")))
                        .datePublication(resultSet.getInt("b_date"))
                        .total(resultSet.getInt("b_total"))
                        .authors(fillAuthors(resultSet))
                        .build();
                books.add(book);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        log.info("fill books, size {}", books.size());
        return books;
    }

    public static Collection<Author> fillAuthors(ResultSet resultSet) {
        String[] authorsIds = new String[0];
        Collection<Author> authorSet = new TreeSet<>(Comparator.comparing(Author::getName));
        try {
            authorsIds = resultSet.getString("authors_id").split(", ");
            String[] authors = resultSet.getString("authors").split(", ");
            for (int i = 0; i < authorsIds.length; i++) {
                authorSet.add(new Author(Long.parseLong(authorsIds[i]), authors[i]));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return authorSet;
    }

    public static Optional<Author> fillAuthor(ResultSet resultSet) {
        try {
            return Optional.of(new Author(resultSet.getInt("a_id"), resultSet.getString("a_name")));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<User> fillUser(ResultSet resultSet) {
        try {
            resultSet.next();
            return getUser(resultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

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

    public static Optional<Loan> fillLoan(ResultSet resultSet) {
        try {
            resultSet.next();
            return getLoan(resultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    private static Optional<Loan> getLoan(ResultSet resultSet) throws SQLException {
        Loan loan = Loan.builder()
                .bookId(resultSet.getInt("l_bookId"))
                .userId(resultSet.getInt("l_userId"))
                .startDate(toLocalDate(resultSet.getTimestamp("l_start_time")))
                .duration(resultSet.getInt("l_duration"))
                .bookInfo(resultSet.getString("b_title") + " ," + resultSet.getInt("b_date"))
                .status(LoanStatus.valueOf(resultSet.getString("l_status")))
                .fine(resultSet.getInt("b_fine"))
                .build();
        log.info("loan load from db {}", loan);
        loan.setId(resultSet.getInt("l_id"));
        return Optional.of(loan);
    }

    private static LocalDate toLocalDate(Timestamp startTime) {
        return startTime.toLocalDateTime().toLocalDate();
    }

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


    public static String prepareForLike(String title) {
        return "%" + validateForLike(title) + "%";
    }

    public static String validateForLike(String title) {
        return title.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "!]").replace("]", "!]").replace("^", "!^");
    }
}