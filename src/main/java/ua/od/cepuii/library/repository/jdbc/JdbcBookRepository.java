package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.AuthorRepository;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ValidationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.*;

public class JdbcBookRepository implements BookRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcBookRepository.class);

    private final DbExecutor<Book> dbExecutor;
    private final ConnectionPool connectionPool;
    private final AuthorRepository authorRepository;
    private static final String INSERT_BOOK = "INSERT INTO book (title, publication_id, date_publication, fine, total) VALUES (?,?,?,?,?);";
    private static final String INSERT_BOOK_AUTHORS = "INSERT INTO book_author (book_id, author_id) VALUES (?,?);";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?;";
    private static final String UPDATE = "UPDATE book SET title=?, publication_id=?, date_publication=?, fine =?,  total=? WHERE id=?";
    private static final String SELECT_ALL = "SELECT * " + "FROM (SELECT book.id b_id, " + "book.title b_title, " + "pt.type pt_name, " + "book.date_publication b_date, " + "book.total b_total, " + "string_agg(cast(a.id as VARCHAR), ', ') as authors_id, " + "string_agg(a.name, ', ') as authors " + "FROM book " + "JOIN book_author ba ON book.id = ba.book_id " + "JOIN author a ON a.id = ba.author_id " + "JOIN publication_type pt ON pt.id = book.publication_id " + "WHERE (book.total - book.no_of_borrow) > 0 AND book.title LIKE ? " + "GROUP BY book.id, book.title, pt.type " + "ORDER BY ";
    private static final String SELECT_ALL_PART2 = ") as bbap " + "WHERE authors LIKE ? " + "LIMIT ? OFFSET ?;";
    private static final String SELECT_BY_ID = "SELECT * " + "FROM (SELECT book.id b_id, " + "book.title b_title, " + "pt.type pt_name, " + "book.date_publication b_date, " + "book.total b_total, " + "string_agg(cast(a.id as VARCHAR), ', ') as authors_id, " + "string_agg(a.name, ', ') as authors " + "FROM book " + "JOIN book_author ba ON book.id = ba.book_id " + "JOIN author a ON a.id = ba.author_id " + "JOIN publication_type pt ON pt.id = book.publication_id " + "WHERE (book.total - book.no_of_borrow) > 0 " + "GROUP BY book.id, book.title, pt.type) as bbap WHERE b_id=?";
    private static final String SELECT_BY_TITLE = SELECT_ALL + " WHERE book.title LIKE ? ;";
    private static final String SELECT_ALL_BY_AUTHOR = SELECT_ALL + "WHERE a.name LIKE ? ;";
    private static final String COUNT_SELECT_ALL_FILTER = "SELECT count(DISTINCT title) FROM book " +
            "JOIN book_author ba on book.id = ba.book_id " + "JOIN author a on a.id = ba.author_id " +
            "WHERE (book.total - book.no_of_borrow) > 0 AND title LIKE ? AND a.name LIKE ? ";

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
                long bookId = dbExecutor.executeInsert(connection, INSERT_BOOK, getBookFields(book));
                updateAuthors(book.getAuthors(), connection, bookId);
                connection.commit();
                return bookId;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
                return -1;
            }
        }
    }

    private static List<Object> getBookFields(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal());
    }

    @Override
    public Optional<Book> getById(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, SELECT_BY_ID, id, resultSet -> fillBooks(resultSet).stream().findFirst());
        }

    }

    private static List<Object> getBookFieldsForUpdate(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal(), book.getId());
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean executeUpdate = dbExecutor.executeUpdate(connection, UPDATE, getBookFieldsForUpdate(book));
                if (executeUpdate) {
                    updateAuthors(book.getAuthors(), connection, book.getId());
                }
                connection.commit();
                return executeUpdate;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
                return false;
            }
        }

    }

    private void updateAuthors(Collection<Author> authors, Connection connection, long bookId) throws SQLException {
        for (Author author : authors) {
            if (ValidationUtil.isNew(author)) {
                long authorId = authorRepository.insert(author);
                dbExecutor.executeInsertWithoutGeneratedKey(connection, INSERT_BOOK_AUTHORS, List.of(bookId, authorId));
            } else {
                authorRepository.update(author);
            }
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeById(connection, DELETE_BOOK, id);
        }
    }

    @Override
    public Collection<Book> getAll(FilterAndSortParams params, String orderBy, int limit, int offset) {
        String titleFilter = prepareForLike(validateForLike(params.getFirstParam()));
        String authorFilter = prepareForLike(validateForLike(params.getSecondParam()));
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllWithLimit(connection, SELECT_ALL + orderBy + SELECT_ALL_PART2, titleFilter, authorFilter, limit, offset, RepositoryUtil::fillBooks);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }


    @Override
    public Collection<Book> getByTitle(String title) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_BY_TITLE, prepareForLike(title), RepositoryUtil::fillBooks);
        }
    }


    @Override
    public Collection<Book> getByAuthor(String author) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_ALL_BY_AUTHOR, prepareForLike(author), RepositoryUtil::fillBooks);
        }
    }

    @Override
    public int getCount(FilterAndSortParams filterParam) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_SELECT_ALL_FILTER)) {
            String titleForSearch = prepareForLike(validateForLike(filterParam.getFirstParam()));
            String authorForSearch = prepareForLike(validateForLike(filterParam.getSecondParam()));
            statement.setString(1, titleForSearch);
            statement.setString(2, authorForSearch);
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

    @Override
    public boolean addAuthor(long bookId, Author author) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                long authorId = authorRepository.insert(author);
                boolean executeResult = dbExecutor.executeInsertWithoutGeneratedKey(connection, INSERT_BOOK_AUTHORS, List.of(bookId, authorId));
                if (executeResult) {
                    connection.commit();
                    return true;
                } else {
                    throw new SQLException("can`t insert to book_author");
                }
            } catch (SQLException e) {
                connection.rollback();
                return false;
            }
        }
    }
}
