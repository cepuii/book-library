package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final DbExecutor<User> dbExecutor;
    private final ConnectionPool connectionPool;
    private static final String INSERT_USER = "INSERT INTO users (email, password, role_id) VALUES (?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id, email, password, registered, blocked, role_id FROM users WHERE id=?";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, password = ?, blocked = ?, role_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id=?";
    private static final String SELECT_ALL = "SELECT id, email, password, registered, blocked, role_id FROM users";
    private static final String SELECT_BY_EMAIL = "SELECT id, email, password, registered, blocked, role_id FROM users WHERE email = ?";


    public JdbcUserRepository(DbExecutor<User> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(User user) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeInsert(connection, INSERT_USER, List.of(user.getEmail(), user.getPassword(), user.getRole().ordinal()));
        }
    }

    @Override
    public Optional<User> getById(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, SELECT_BY_ID, id, RepositoryUtil::fillUser);
        }
    }

    @Override
    public boolean update(User entity) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeUpdate(connection, UPDATE_USER, List.of(entity.getEmail(), entity.getPassword(),
                    entity.isEnabled(), entity.getRole().ordinal()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeDelete(connection, DELETE_BY_ID, id);
        }
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, RepositoryUtil::fillUsers);
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAllByParam(connection, SELECT_BY_EMAIL, email, RepositoryUtil::fillUsers).stream().findAny();
        } catch (SQLException e) {
            throw new RepositoryException("Can`t find user with this email: " + email, e);
        }
    }
}
