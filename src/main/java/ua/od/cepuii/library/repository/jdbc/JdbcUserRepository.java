package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.AbstractRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A JDBC implementation of the {@link LoanRepository} interface. This class provides methods
 * to interact with the database to perform CRUD operations
 * on Loan entities. The class extends {@link AbstractRepository} to inherit the basic functionality of repositories.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class JdbcUserRepository extends AbstractRepository<User> implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final QueryExecutor<User> queryExecutor;
    private static final String INSERT_USER = "INSERT INTO users (email, password, role_id) VALUES (?,?,?)";
    private static final String UPDATE_USER_EMAIL = "UPDATE users SET email = ? WHERE id=?";
    private static final String UPDATE_USER_PASSWORD = "UPDATE users SET password = ? WHERE id=?";
    private static final String UPDATE_USER_BLOCK = "UPDATE users SET blocked=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id=?";
    private static final String SELECT_ALL = "SELECT users.id users_id, email, password, registered, fine, blocked, ur.role role " +
            "FROM users JOIN user_role ur on ur.id = users.role_id ";

    private static final String SELECT_ALL_WITH_WHERE = SELECT_ALL + "WHERE email LIKE ? AND role LIKE ? " +
            "ORDER BY ";
    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";
    private static final String GET_COUNT = "SELECT count(*) " +
            "FROM users JOIN user_role ur on ur.id = users.role_id " +
            "WHERE users.email LIKE ? AND role LIKE ? ";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE users.id=?";
    private static final String SELECT_BY_EMAIL = SELECT_ALL + " WHERE email = ?";


    public JdbcUserRepository(QueryExecutor<User> userExecutor, ConnectionPool connectionPool) {
        super(connectionPool);
        this.queryExecutor = userExecutor;
    }

    /**
     * Inserts a new User object into the database and returns the generated ID of the new record.
     *
     * @param connection the database connection to use
     * @param user       the User object to insert
     * @return the generated ID of the new record, or -1 if the insert fails
     */
    @Override
    protected long insertAndGetId(Connection connection, User user) {
        try {
            return queryExecutor.insert(connection, INSERT_USER, List.of(user.getEmail(), user.getPassword(), user.getRole().ordinal()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves a User object from the database by its ID.
     *
     * @param connection the database connection to use
     * @param id         the ID of the User to retrieve
     * @return an Optional containing the retrieved User object, or an empty Optional if no User was found with the given ID
     * @throws SQLException if there is an error executing the query
     */
    @Override
    protected Optional<User> selectById(Connection connection, long id) throws SQLException {
        return queryExecutor.selectByParams(connection, SELECT_BY_ID, List.of(id), RepositoryUtil::fillUser);
    }

    /**
     * Updates a User object in the database.
     *
     * @param connection the database connection to use
     * @param entity     the User object to update
     * @return true if the update succeeds, false otherwise
     * @throws SQLException if there is an error executing the update
     */
    @Override
    protected boolean update(Connection connection, User entity) throws SQLException {
        try {
            return queryExecutor.update(connection, UPDATE_USER_EMAIL, List.of(entity.getEmail(), entity.getId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Updates the password for a User with the given ID.
     *
     * @param userId   the ID of the User to update
     * @param password the new password to set
     * @return true if the update succeeds, false otherwise
     */
    @Override
    public boolean updatePassword(long userId, String password) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = queryExecutor.update(connection, UPDATE_USER_PASSWORD, List.of(password, userId));
                connection.commit();
                return update;
            } catch (Exception e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a User record from the database by its ID.
     *
     * @param connection the database connection to use
     * @param id         the ID of the User to delete
     * @return true if the delete succeeds, false otherwise
     */
    @Override
    protected boolean delete(Connection connection, long id) {
        try {
            return queryExecutor.isExistResultById(connection, DELETE_BY_ID, id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all User objects from the database that match the specified filter parameters.
     *
     * @param connection the database connection to use
     * @param params     a List of parameter values to use in the query
     * @param orderBy    the column to order the results by
     * @return a Collection of User objects that match the filter parameters
     * @throws SQLException if there is an error executing the query
     */
    @Override
    protected Collection<User> selectAll(Connection connection, List<Object> params, String orderBy) throws SQLException {
        return queryExecutor.selectAll(connection, SELECT_ALL_WITH_WHERE + orderBy + LIMIT_OFFSET, params, RepositoryUtil::fillUsers);
    }

    /**
     * Retrieves a User object from the database by its email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the retrieved User object, or an empty Optional if no User was found with the given email
     */
    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection connection = connectionPool.getConnection()) {
            return queryExecutor.selectAll(connection, SELECT_BY_EMAIL, List.of(email), RepositoryUtil::fillUsers).stream().findAny();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Retrieves the number of User objects that match the specified filter parameters.
     *
     * @param connection   the database connection to use
     * @param filterParams a FilterParams object that specifies the filter parameters to use in the query
     * @return the number of User objects that match the filter parameters
     * @throws SQLException if there is an error executing the query
     */
    @Override
    protected int getCount(Connection connection, FilterParams filterParams) throws SQLException {
        return queryExecutor.selectCount(connection, GET_COUNT,
                List.of(filterParams.getFirstParamForQuery(), filterParams.getSecondParamForQuery()));
    }

    /**
     * Updates the blocked status for a User with the given ID.
     *
     * @param userId    the ID of the User to update
     * @param isBlocked the new blocked status to set
     * @return true if the update succeeds, false otherwise
     */
    @Override
    public boolean updateBlocked(long userId, boolean isBlocked) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = queryExecutor.update(connection, UPDATE_USER_BLOCK, List.of(isBlocked, userId));
                connection.commit();
                return update;

            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
