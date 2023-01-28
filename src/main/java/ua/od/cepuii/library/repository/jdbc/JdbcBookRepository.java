package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.AuthorRepository;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository implements BookRepository {

    private final DbExecutor<Book> dbExecutor;
    private final ConnectionPool connectionPool;

    private final AuthorRepository authorRepository;
    private static final String INSERT_BOOK = "INSERT INTO book (title, publication_id, date_publication, total)   " +
            "VALUES (?,?,?,?);";
    private static final String INSERT_BOOK_AUTHORS = "INSERT INTO book_author (book_id, author_id) VALUES (?,?);";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?;";
    private static final String UPDATE = "UPDATE book SET title=?, publication_id=?, date_publication=?, total=? WHERE id=?";
    private static final String SELECT_ALL = "SELECT book.id b_id, book.title b_title,a.id a_id,a.name a_name, pt.type pt_name, " +
            "book.date_publication b_date, book.total b_total " +
            "FROM book JOIN book_author ba ON book.id = ba.book_id " +
            "JOIN author a ON a.id = ba.author_id " +
            "JOIN publication_type pt ON pt.id = book.publication_id ";
    private static final String SELECT_BY_ID = SELECT_ALL + "WHERE book.id=?;";
    private static final String SELECT_BY_TITLE = SELECT_ALL + " WHERE book.title LIKE ? ;";
    private static final String SELECT_ALL_BY_AUTHOR = SELECT_ALL + "WHERE a.name LIKE ? ;";

    public JdbcBookRepository(DbExecutor<Book> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
        authorRepository = new JdbcAuthorRepository(new DbExecutorImpl<>(), connectionPool);
    }

    @Override
    public long insert(Book book) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                long bookId = dbExecutor.executeInsert(connection, INSERT_BOOK, List.of(book.getTitle(),
                        book.getPublicationType().ordinal(), book.getDatePublication(), book.getTotal()));
                for (Author author : book.getAuthorSet()) {
                    long authorId = authorRepository.insert(author);
                    dbExecutor.executeInsertWithoutGeneratedKey(connection, INSERT_BOOK_AUTHORS, List.of(bookId, authorId));
                }
                connection.commit();
                return bookId;
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException(e);
            }
        }
    }

    @Override
    public Optional<Book> getById(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, SELECT_BY_ID, id, resultSet -> RepositoryUtil.fillBooks(resultSet).stream().findFirst());
        }
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeUpdate(connection, UPDATE, List.of(book.getTitle(),
                    book.getPublicationType().ordinal(), book.getDatePublication(), book.getTotal(), book.getId()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeDelete(connection, DELETE_BOOK, id);
        }
    }

    @Override
    public Collection<Book> getAll() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, RepositoryUtil::fillBooks);
        }
    }




    @Override
    public Collection<Book> getByTitle(String title) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_BY_TITLE, prepareForLike(title), RepositoryUtil::fillBooks);
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
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_ALL_BY_AUTHOR, prepareForLike(author), RepositoryUtil::fillBooks);
        }
    }
}
