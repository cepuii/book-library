package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.BookFilterParam;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.AuthorRepository;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository implements BookRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcBookRepository.class);

    private final DbExecutor<Book> dbExecutor;
    private final ConnectionPool connectionPool;

    private final AuthorRepository authorRepository;
    private static final String INSERT_BOOK = "INSERT INTO book (title, publication_id, date_publication, total)   " +
            "VALUES (?,?,?,?);";
    private static final String INSERT_BOOK_AUTHORS = "INSERT INTO book_author (book_id, author_id) VALUES (?,?);";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?;";
    private static final String UPDATE = "UPDATE book SET title=?, publication_id=?, date_publication=?, total=? WHERE id=?";
    private static final String SELECT_ALL = "SELECT * " +
            "FROM (SELECT book.id                     b_id, " +
            "             book.title                  b_title, " +
            "             pt.type                     pt_name, " +
            "             book.date_publication       b_date, " +
            "             string_agg(a.name, ', ') as authors " +
            "      FROM book " +
            "               JOIN book_author ba ON book.id = ba.book_id " +
            "               JOIN author a ON a.id = ba.author_id " +
            "               JOIN publication_type pt ON pt.id = book.publication_id " +
            "      WHERE book.title LIKE ? " +
            "      GROUP BY book.id, book.title, pt.type " +
            "      ORDER BY ";
    private static final String SELECT_ALL_PART2 = ") as bbap " +
            "WHERE authors LIKE ? " +
            "LIMIT ? OFFSET ?;";
    private static final String SELECT_BY_ID = SELECT_ALL + "WHERE book.id=?;";
    private static final String SELECT_BY_TITLE = SELECT_ALL + " WHERE book.title LIKE ? ;";
    private static final String SELECT_ALL_BY_AUTHOR = SELECT_ALL + "WHERE a.name LIKE ? ;";
    private static final String COUNT_SELECT_ALL_FILTER = "SELECT count(DISTINCT title) FROM book " +
            "JOIN book_author ba on book.id = ba.book_id " +
            "JOIN author a on a.id = ba.author_id " +
            "WHERE title LIKE ? AND a.name LIKE ? ";

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
                        book.getPublicationType().ordinal(), book.getDatePublication()));
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
                    book.getPublicationType().ordinal(), book.getDatePublication(), book.getId()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeDelete(connection, DELETE_BOOK, id);
        }
    }

    @Override
    public Collection<Book> getAll(String orderBy, boolean descending, int limit, int offset) throws SQLException {
        String query = SELECT_ALL;
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, query, orderBy, limit, offset, RepositoryUtil::fillBooks);
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

    @Override
    public Collection<Book> getAllWithFilter(String orderBy, boolean descending, int limit, int offset, BookFilterParam filterParam) throws SQLException {
        String titleFilter = prepareForLike(validateForLike(filterParam.getTitle()));
        String authorFilter = prepareForLike(validateForLike(filterParam.getAuthor()));
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllWithLimit(connection, SELECT_ALL + orderBy + SELECT_ALL_PART2, titleFilter, authorFilter, orderBy, limit, offset, RepositoryUtil::fillBooks);
        }
    }

    @Override
    public int getCount(BookFilterParam filterParam) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_SELECT_ALL_FILTER)) {
            String titleForSearch = prepareForLike(validateForLike(filterParam.getTitle()));
            String authorForSearch = prepareForLike(validateForLike(filterParam.getAuthor()));
            statement.setString(1, titleForSearch);
            statement.setString(2, authorForSearch);
            log.info(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException("failed to get number of records", e);
        }
    }
}
