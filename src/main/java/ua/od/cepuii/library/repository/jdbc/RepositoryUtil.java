package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.entity.enums.PublicationType;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.exception.RepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RepositoryUtil {

    private RepositoryUtil() {
    }


    public static Collection<Book> fillBooks(ResultSet resultSet) {
        Collection<Book> books = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                long id = resultSet.getLong("b_id");
                Book book = new Book.Builder()
                        .setId(id)
                        .setTitle(resultSet.getString("b_title"))
                        .setType(PublicationType.valueOf(resultSet.getString("pt_name")))
                        .setDatePublication(resultSet.getInt("b_date"))
                        .setAuthorSet(fillAuthorsFromString(resultSet.getString("authors")))
                        .build();

                books.add(book);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    private static List<Author> fillAuthorsFromString(String authors) {
        return Arrays.stream(authors.split(", ")).map(Author::new).collect(Collectors.toList());
    }

    public static Collection<Author> fillAuthors(ResultSet resultSet) {
        try {
            Collection<Author> authors = new HashSet<>();
            while (resultSet.next()) {
                Author author = new Author(resultSet.getInt("id"), resultSet.getString("name"));
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate authors from result set", e);
        }
    }

    public static Optional<Author> fillAuthor(ResultSet resultSet) {
        try {
            return Optional.of(new Author(resultSet.getInt("a_id"), resultSet.getString("a_name")));
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate author from result set", e);
        }
    }

    public static Optional<User> fillUser(ResultSet resultSet) {
        try {
            resultSet.next();
            return getUser(resultSet);
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate user from result set", e);
        }
    }

    private static Optional<User> getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setDateTime(resultSet.getTimestamp("registered").toLocalDateTime());
        user.setEnabled(resultSet.getBoolean("blocked"));
        user.setRole(Role.values()[resultSet.getInt("role_id")]);
        return Optional.of(user);
    }


    public static Collection<User> fillUsers(ResultSet resultSet) {
        try {
            Collection<User> users = new HashSet<>();
            while (resultSet.next()) {
                users.add(getUser(resultSet).orElseThrow());
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate users from resultSet", e);
        }
    }

    public static Optional<Loan> fillLoan(ResultSet resultSet) {
        try {
            resultSet.next();
            return getLoan(resultSet);
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate loan from result set", e);
        }
    }

    private static Optional<Loan> getLoan(ResultSet resultSet) throws SQLException {
        return Optional.of(new Loan(resultSet.getLong("id"), resultSet.getLong("user_id"),
                resultSet.getLong("book_id"), toLocalDate(resultSet.getTimestamp("start_time")),
                resultSet.getInt("duration"), LoanStatus.values()[resultSet.getInt("status_id")],
                resultSet.getInt("fine")));
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
            throw new RepositoryException("Can`t populate loans collection", e);
        }
        return loans;
    }
}