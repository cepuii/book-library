package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.AbstractRepository;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;
import ua.od.cepuii.library.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.fillBooks;

/**
 * A JDBC implementation of the {@link BookRepository} interface. This class provides methods
 * to interact with the database to perform CRUD operations
 * on Book entities. The class extends {@link AbstractRepository} to inherit the basic functionality of repositories.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class JdbcBookRepository extends AbstractRepository<Book> implements BookRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcBookRepository.class);
    private final QueryExecutor<Book> bookExecutor;
    private final QueryExecutor<Author> authorExecutor;
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
    private static final String COUNT_SELECT_ALL_FILTER = "SELECT count(DISTINCT title) FROM book " +
            "JOIN book_author ba on book.id = ba.book_id " + "JOIN author a on a.id = ba.author_id " +
            "WHERE (book.total - book.no_of_borrow) > 0 AND title LIKE ? AND a.name LIKE ? ";

    private static final String UPDATE_AUTHOR = "UPDATE author SET name=? WHERE id=?";
    private static final String SELECT_AUTHOR_BY_NAME = "SELECT id a_id, name a_name FROM author WHERE name=?";
    private static final String INSERT_AUTHOR = "INSERT INTO author(name) VALUES (?);";
    private static final String GET_BY_TITLE = "SELECT book.id b_id FROM book WHERE book.title = ?";

    public JdbcBookRepository(QueryExecutor<Book> bookExecutor, QueryExecutor<Author> authorExecutor, ConnectionPool connectionPool) {
        super(connectionPool);
        this.bookExecutor = bookExecutor;
        this.authorExecutor = authorExecutor;
    }

    /**
     * Inserts a book into the database and returns the book ID. Also updates the authors of the book.
     *
     * @param connection the database connection to use for the operation
     * @param book       the book object to insert into the database
     * @return the book ID of the inserted book, or -1 if an error occurred
     */
    @Override
    protected long insertAndGetId(Connection connection, Book book) {
        try {
            long bookId = bookExecutor.insert(connection, INSERT_BOOK, getBookFields(book));
            updateAuthors(book.getAuthors(), connection, bookId);
            return bookId;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    /**
     * Returns a list of book fields to be used in a SQL query.
     *
     * @param book the book object containing the fields to be returned
     * @return a list of book fields
     */
    private static List<Object> getBookFields(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal());
    }

    /**
     * Selects a book from the database with the given ID.
     *
     * @param connection the connection to the database
     * @param id         the ID of the book to select
     * @return an optional containing the selected book, or empty if no book was found
     * @throws SQLException if a database error occurs
     */
    @Override
    protected Optional<Book> selectById(Connection connection, long id) throws SQLException {
        return bookExecutor.selectByParams(connection, SELECT_BY_ID, List.of(id), resultSet -> fillBooks(resultSet).stream().findFirst());
    }

    /**
     * Returns a list of book fields to be used in an update SQL query.
     *
     * @param book the book object containing the fields to be returned
     * @return a list of book fields
     */
    private static List<Object> getBookFieldsForUpdate(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal(), book.getId());
    }

    /**
     * Updates a book in the database.
     *
     * @param connection the connection to the database
     * @param book       the book object containing the updated fields
     * @return true if the update was successful, false otherwise
     */
    @Override
    protected boolean update(Connection connection, Book book) {
        try {
            boolean executeUpdate = bookExecutor.update(connection, UPDATE, getBookFieldsForUpdate(book));
            if (executeUpdate) {
                updateAuthors(book.getAuthors(), connection, book.getId());
            }
            return executeUpdate;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Updates the authors of a book in the database.
     *
     * @param authors    the collection of authors to be updated
     * @param connection the connection to the database
     * @param bookId     the ID of the book whose authors are being updated
     * @throws SQLException if a database error occurs
     */
    private void updateAuthors(Collection<Author> authors, Connection connection, long bookId) throws SQLException {
        for (Author author : authors) {
            if (ValidationUtil.isNew(author)) {
                Optional<Author> optionalAuthor = authorExecutor.selectByParams(connection, SELECT_AUTHOR_BY_NAME, List.of(author.getName()), RepositoryUtil::fillAuthor);
                if (optionalAuthor.isEmpty()) {
                    long authorId = bookExecutor.insert(connection, INSERT_AUTHOR, List.of(author.getName()));
                    bookExecutor.insertWithoutGeneratedKey(connection, INSERT_BOOK_AUTHORS, List.of(bookId, authorId));
                } else {
                    bookExecutor.update(connection, UPDATE_AUTHOR, List.of(optionalAuthor.get().getName(), optionalAuthor.get().getId()));
                }
            } else {
                bookExecutor.update(connection, UPDATE_AUTHOR, List.of(author.getName(), author.getId()));
            }
        }
    }

    /**
     * Deletes a book from the database with the given ID.
     *
     * @param connection the connection to the database
     * @param id         the ID of the book to delete
     * @return true if the book was deleted, false otherwise
     */
    @Override
    public boolean delete(Connection connection, long id) {
        try {
            return bookExecutor.isExistResultById(connection, DELETE_BOOK, id);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Selects all books from the database with the given parameters and ordering.
     *
     * @param connection the connection to the database
     * @param params     the list of parameters to filter the books
     * @param orderBy    the order by clause of the SQL query
     * @return a collection of books matching the filter
     * @throws SQLException if a database error occurs
     */
    @Override
    protected Collection<Book> selectAll(Connection connection, List<Object> params, String orderBy) throws SQLException {
        return bookExecutor.selectAll(connection, SELECT_ALL + orderBy + SELECT_ALL_PART2, params, RepositoryUtil::fillBooks);
    }

    /**
     * Gets the count of books in the database with the given filter parameters.
     *
     * @param connection  the connection to the database
     * @param filterParam the filter parameters to count the books
     * @return the count of books matching the filter parameters
     * @throws SQLException if a database error occurs
     */
    @Override
    protected int getCount(Connection connection, FilterParams filterParam) throws SQLException {
        return bookExecutor.selectCount(connection, COUNT_SELECT_ALL_FILTER, List.of(filterParam.getFirstParamForQuery(), filterParam.getSecondParamForQuery()));
    }

    /**
     * Checks if a book with the given title exists in the database.
     *
     * @param title the title of the book to check
     * @return true if a book with the given title exists, false otherwise.
     */
    @Override
    public boolean isExistTitle(String title) {
        try (Connection connection = connectionPool.getConnection()) {
            return bookExecutor.isExistResultByString(connection, GET_BY_TITLE, title);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

}

