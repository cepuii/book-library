package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.AbstractRepository;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Override
    protected long insertAndGetId(Connection connection, User user) {
        try {
            return queryExecutor.insert(connection, INSERT_USER, List.of(user.getEmail(), user.getPassword(), user.getRole().ordinal()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    @Override
    protected Optional<User> selectById(Connection connection, long id) throws SQLException {
        return queryExecutor.selectByParams(connection, SELECT_BY_ID, List.of(id), RepositoryUtil::fillUser);
    }

    @Override
    protected boolean update(Connection connection, User entity) throws SQLException {
        try {
            return queryExecutor.update(connection, UPDATE_USER_EMAIL, List.of(entity.getEmail(), entity.getId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

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

    @Override
    protected boolean delete(Connection connection, long id) {
        try {
            return queryExecutor.isExistResultById(connection, DELETE_BY_ID, id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected Collection<User> selectAll(Connection connection, List<Object> params, String orderBy) throws SQLException {
        return queryExecutor.selectAll(connection, SELECT_ALL_WITH_WHERE + orderBy + LIMIT_OFFSET, params, RepositoryUtil::fillUsers);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection connection = connectionPool.getConnection()) {
            return queryExecutor.selectAll(connection, SELECT_BY_EMAIL, List.of(email), RepositoryUtil::fillUsers).stream().findAny();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    protected int getCount(Connection connection, FilterParams filterParams) throws SQLException {
        return queryExecutor.selectCount(connection, GET_COUNT,
                List.of(filterParams.getFirstParamForQuery(), filterParams.getSecondParamForQuery()));
    }

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
