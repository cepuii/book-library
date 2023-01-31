package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.jdbc.executor.DbExecutor;
import ua.od.cepuii.library.repository.jdbc.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.*;

public class JdbcBookRepository implements BookRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcBookRepository.class);
    private final DbExecutor<Book> executor;
    private final DbExecutor<Author> authorExecutor;
    private final ConnectionPool connectionPool;
    private static final String INSERT_BOOK = "INSERT INTO book (title, publication_id, date_publication, fine, total) VALUES (?,?,?,?,?);";
    private static final String INSERT_BOOK_AUTHORS = "INSERT INTO book_author (book_id, author_id) VALUES (?,?);";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?;";
    private static final String UPDATE = "UPDATE book SET title=?, publication_id=?, date_publication=?, fine =?,  total=? WHERE id=?";
    private static final String SELECT_ALL = "SELECT * " +
            "FROM (SELECT book.id b_id, " +
            "book.title b_title, " +
            "pt.type pt_name, " +
            "book.date_publication b_date, " +
            "book.total b_total, " +
            "string_agg(cast(a.id as VARCHAR), ', ') as authors_id, " +
            "string_agg(a.name, ', ') as authors " +
            "FROM book " +
            "JOIN book_author ba ON book.id = ba.book_id " +
            "JOIN author a ON a.id = ba.author_id " +
            "JOIN publication_type pt ON pt.id = book.publication_id " +
            "WHERE (book.total - book.no_of_borrow) > 0 AND book.title LIKE ? " +
            "GROUP BY book.id, book.title, pt.type " +
            "ORDER BY ";
    private static final String SELECT_ALL_PART2 = ", b_title) as bbap " +
            "WHERE authors LIKE ? " + "LIMIT ? OFFSET ?;";
    private static final String SELECT_BY_ID = "SELECT * " +
            "FROM (SELECT book.id b_id, book.title b_title, pt.type pt_name, book.date_publication b_date, " +
            "book.total b_total, string_agg(cast(a.id as VARCHAR), ', ') as authors_id, string_agg(a.name, ', ') as authors " +
            "FROM book JOIN book_author ba ON book.id = ba.book_id " +
            "JOIN author a ON a.id = ba.author_id " +
            "JOIN publication_type pt ON pt.id = book.publication_id " +
            "WHERE (book.total - book.no_of_borrow) > 0 " +
            "GROUP BY book.id, book.title, pt.type) as bbap WHERE b_id=?";
    private static final String SELECT_BY_TITLE = SELECT_ALL + " WHERE book.title=? ;";
    private static final String SELECT_ALL_BY_AUTHOR = SELECT_ALL + "WHERE a.name LIKE ? ;";
    private static final String COUNT_SELECT_ALL_FILTER = "SELECT count(DISTINCT title) FROM book " +
            "JOIN book_author ba on book.id = ba.book_id " + "JOIN author a on a.id = ba.author_id " +
            "WHERE (book.total - book.no_of_borrow) > 0 AND title LIKE ? AND a.name LIKE ? ";

    private static final String UPDATE_AUTHOR = "UPDATE author SET name=? WHERE id=?";
    private static final String SELECT_AUTHOR_BY_NAME = "SELECT id a_id, name a_name FROM author WHERE name=?";
    private static final String INSERT_AUTHOR = "INSERT INTO author(name) VALUES (?);";
    private static final String GET_BY_TITLE = "SELECT book.id b_id FROM book WHERE book.title = ?";

    public JdbcBookRepository(ConnectionPool connectionPool) {
        this.executor = new DbExecutorImpl<>();
        this.authorExecutor = new DbExecutorImpl<>();
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                long bookId = executor.insert(connection, INSERT_BOOK, getBookFields(book));
                updateAuthors(book.getAuthors(), connection, bookId);
                connection.commit();
                return bookId;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    private static List<Object> getBookFields(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal());
    }

    @Override
    public Optional<Book> getById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            return executor.selectById(connection, SELECT_BY_ID, id, resultSet -> fillBooks(resultSet).stream().findFirst());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    private static List<Object> getBookFieldsForUpdate(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal(), book.getId());
    }

    @Override
    public boolean update(Book book) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean executeUpdate = executor.update(connection, UPDATE, getBookFieldsForUpdate(book));
                if (executeUpdate) {
                    updateAuthors(book.getAuthors(), connection, book.getId());
                }
                connection.commit();
                return executeUpdate;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private void updateAuthors(Collection<Author> authors, Connection connection, long bookId) throws SQLException {
        for (Author author : authors) {
            if (ValidationUtil.isNew(author)) {
                Optional<Author> optionalAuthor = authorExecutor.selectByParams(connection, SELECT_AUTHOR_BY_NAME, List.of(author.getName()), RepositoryUtil::fillAuthor);
                if (optionalAuthor.isEmpty()) {
                    long authorId = executor.insert(connection, INSERT_AUTHOR, List.of(author.getName()));
                    executor.insertWithoutGeneratedKey(connection, INSERT_BOOK_AUTHORS, List.of(bookId, authorId));
                } else {
                    executor.update(connection, UPDATE_AUTHOR, List.of(optionalAuthor.get().getName(), optionalAuthor.get().getId()));
                }
            } else {
                executor.update(connection, UPDATE_AUTHOR, List.of(author.getName(), author.getId()));
            }
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean b = executor.queryById(connection, DELETE_BOOK, id);
                connection.commit();
                return b;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Book> getAll(FilterParams filterParam, String orderBy, int limit, int offset) {
        String titleFilter = prepareForLike(validateForLike(filterParam.getFirstParam()));
        String authorFilter = prepareForLike(validateForLike(filterParam.getSecondParam()));
        try (Connection connection = connectionPool.getConnection()) {
            return executor.selectAllWithLimit(connection, SELECT_ALL + orderBy + SELECT_ALL_PART2, titleFilter, authorFilter, limit, offset, RepositoryUtil::fillBooks);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public int getCount(FilterParams filterParam) {
        String titleForSearch = prepareForLike(validateForLike(filterParam.getFirstParam()));
        String authorForSearch = prepareForLike(validateForLike(filterParam.getSecondParam()));
        try (Connection connection = connectionPool.getConnection()) {
            return executor.selectCount(connection, COUNT_SELECT_ALL_FILTER, List.of(titleForSearch, authorForSearch));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean isExistTitle(String title) {
        try (Connection connection = connectionPool.getConnection()) {
            return executor.queryByString(connection, GET_BY_TITLE, title);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}

