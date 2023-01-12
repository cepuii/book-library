package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.AuthorRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcAuthorRepository implements AuthorRepository {
    private final DbExecutor<Author> dbExecutor;
    private final ConnectionPool connectionPool;

    private static final String SELECT_ALL = "SELECT id,name FROM author";
    private static final String GET_BY_ID = "SELECT id, name FROM author WHERE id=? ";
    private static final String UPDATE_AUTHOR = "UPDATE author SET name=? WHERE id=?";
    private static final String DELETE_AUTHOR = "DELETE FROM author WHERE id=?";
    private static final String INSERT_AUTHOR_IF_NOT_EXIST = "WITH val AS (SELECT id, \"name\" " +
            "FROM author WHERE name = ?), " +
            "ins AS ( INSERT INTO author (\"name\") " +
            "SELECT ? WHERE NOT exists(SELECT 1 FROM val) RETURNING id) " +
            "SELECT id FROM ins UNION ALL SELECT id FROM val;";

    public JdbcAuthorRepository(DbExecutor<Author> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(Author author) throws SQLException {
        long id = 0;
        try (Connection connection = connectionPool.getConnection()) {
            Savepoint savepoint = connection.setSavepoint();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR_IF_NOT_EXIST)) {
                preparedStatement.setString(1, author.getName());
                preparedStatement.setString(2, author.getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                id = resultSet.getLong("id");
                connection.commit();

            } catch (SQLException e) {
                connection.rollback(savepoint);
            }
        }
        return id;
    }

    @Override
    public Optional<Author> getById(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, GET_BY_ID, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return Optional.of(new Author(resultSet.getInt("id"), resultSet.getString("name")));
                    }
                    return Optional.empty();
                } catch (SQLException e) {
                    throw new RepositoryException("Cant get author with id: " + id, e);
                }
            });
        }
    }


    @Override
    public boolean update(Author author) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeUpdate(connection, UPDATE_AUTHOR, List.of(author.getName(), author.getId()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeById(connection, DELETE_AUTHOR, id);
        }
    }

    @Override
    public Collection<Author> getAll(String orderBy, boolean descending, int limit, int offset) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, orderBy, limit, offset, RepositoryUtil::fillAuthors);
        }
    }
}
