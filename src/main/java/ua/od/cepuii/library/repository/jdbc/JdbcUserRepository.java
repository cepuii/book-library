package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.executor.DbExecutor;
import ua.od.cepuii.library.repository.jdbc.executor.DbExecutorImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.prepareForLike;
import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.validateForLike;

public class JdbcUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final DbExecutor<User> dbExecutor;
    private final ConnectionPool connectionPool;
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


    public JdbcUserRepository(ConnectionPool connectionPool) {
        this.dbExecutor = new DbExecutorImpl<>();
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(User user) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                long insert = dbExecutor.insert(connection, INSERT_USER, List.of(user.getEmail(), user.getPassword(), user.getRole().ordinal()));
                connection.commit();
                return insert;
            } catch (Exception e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public Optional<User> getById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.selectById(connection, SELECT_BY_ID, id, RepositoryUtil::fillUser);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean update(User entity) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = dbExecutor.update(connection, UPDATE_USER_EMAIL, List.of(entity.getEmail(), entity.getId()));
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
    public boolean updatePassword(long userId, String password) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = dbExecutor.update(connection, UPDATE_USER_PASSWORD, List.of(password, userId));
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
    public boolean delete(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean b = dbExecutor.queryById(connection, DELETE_BY_ID, id);
                connection.commit();
                return b;
            } catch (Exception e) {
                log.error(e.getMessage());
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<User> getAll(FilterParams params, String orderBy, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {
            String firstParam = prepareForLike(validateForLike(params.getFirstParam()));
            String secondParam = prepareForLike(validateForLike(params.getSecondParam()));
            return dbExecutor.selectAllWithLimit(connection, SELECT_ALL_WITH_WHERE + orderBy + LIMIT_OFFSET, firstParam, secondParam, limit, offset, RepositoryUtil::fillUsers);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.selectAllByParam(connection, SELECT_BY_EMAIL, email, RepositoryUtil::fillUsers).stream().findAny();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int getCount(FilterParams filterParam) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_COUNT)) {
            String userForSearch = prepareForLike(validateForLike(filterParam.getFirstParam()));
            String userRoleForSearch = prepareForLike(validateForLike(filterParam.getSecondParam()));
            statement.setString(1, userForSearch);
            statement.setString(2, userRoleForSearch);
            log.info("{}", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean updateBlocked(long id, boolean isBlocked) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = dbExecutor.update(connection, UPDATE_USER_BLOCK, List.of(isBlocked, id));
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
