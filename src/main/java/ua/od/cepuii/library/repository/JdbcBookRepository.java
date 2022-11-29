package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.config.ConnectionPool;
import ua.od.cepuii.library.model.Book;

import java.sql.*;
import java.util.List;

public class JdbcBookRepository implements BookRepository {


    private static final String CREATE_BOOK = "INSERT INTO book (title, publication, date_publication, no_total)   " +
            "VALUES (?,?,?,?)";

    @Override
    public Book create(Book book) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublication());
            statement.setTimestamp(3, Timestamp.valueOf(book.getDatePublication().atStartOfDay()));
            statement.setInt(4, book.getNoTotal());
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt("id");
            book.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    @Override
    public Book getById(int id) {
        return null;
    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public Book getByTitle(String title) {
        return null;
    }

    @Override
    public Book getByAuthor(String author) {
        return null;
    }
}
