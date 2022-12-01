package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.config.ConnectionPool;
import ua.od.cepuii.library.model.Author;
import ua.od.cepuii.library.model.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcBookRepository implements BookRepository {

    private final DbExecutor<Book> dbExecutor;
    private static final String INSERT_BOOK = "INSERT INTO book (title, publication, date_publication, total)   " + "VALUES (?,?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id,title,author,publication,date_publication, total FROM book WHERE id=?";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?";
    private static final String UPDATE = "UPDATE book SET title=?, publication=?, date_publication=?, total=? WHERE id=?";
    private static final String SELECT_ALL = "SELECT book.id b_id, book.title b_title, a.id a_id, a.name a_name, " +
            "book.publication b_publication,book.date_publication b_date_publication, book.total b_total " +
            "FROM book JOIN book_author ba ON book.id = ba.book_id " +
            "JOIN author a ON a.id = ba.author_id ";
    private static final String SELECT_BY_TITLE = SELECT_ALL + " WHERE book.title LIKE ?";
    private static final String SELECT_ALL_BY_AUTHOR = SELECT_ALL + "WHERE a.name LIKE ?";

    public JdbcBookRepository() {
        dbExecutor = new DbExecutorImpl<>();
    }

    @Override
    public long insert(Book book) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            long id = dbExecutor.executeInsert(connection, INSERT_BOOK, List.of(book.getTitle(),
                    book.getPublication(), book.getDatePublication(), book.getTotal()));
            connection.commit();
            return id;
        }
    }

    @Override
    public Optional<Book> getById(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, SELECT_BY_ID, id, resultSet -> fillBooks(resultSet).stream().findFirst());
        }
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeUpdate(connection, UPDATE, List.of(book.getTitle(),
                    book.getPublication(), book.getDatePublication(), book.getTotal(), book.getId()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeDelete(connection, DELETE_BOOK, id);
        }
    }

    @Override
    public Collection<Book> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, JdbcBookRepository::fillBooks);
        }
    }

    private static Collection<Book> fillBooks(ResultSet resultSet) {
        Map<Long, Book> bookMap = new HashMap<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                long id = resultSet.getLong("b_id");
                if (!bookMap.containsKey(id)) {
                    Book book = new Book(id, resultSet.getString("b_title"), resultSet.getString("b_publication"),
                            resultSet.getInt("b_date_publication"), new HashSet<>(List.of(fillAuthor(resultSet))),
                            resultSet.getInt("b_total"));
                    bookMap.put(id, book);
                } else {
                    bookMap.get(id).getAuthorSet().add(fillAuthor(resultSet));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bookMap.values();
    }

    private static Author fillAuthor(ResultSet resultSet) throws SQLException {
        return new Author(resultSet.getInt("a_id"), resultSet.getString("a_name"));
    }

    @Override
    public Collection<Book> getByTitle(String title) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_BY_TITLE, prepareForLike(title), JdbcBookRepository::fillBooks);
        }
    }

    private String prepareForLike(String title) {
        return "%" + validateForLike(title) + "%";
    }

    private String validateForLike(String title) {
        return title.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "!]")
                .replace("]", "!]").replace("^", "!^");
    }

    @Override
    public Collection<Book> getByAuthor(String author) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_ALL_BY_AUTHOR, prepareForLike(author), JdbcBookRepository::fillBooks);
        }
    }
}
