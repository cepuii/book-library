package ua.od.cepuii.library.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents a HikariConnectionPool which is an implementation of {@link ConnectionPool}.
 * It is used to get a connection to a database using the HikariCP library.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class HikariConnectionPool implements ConnectionPool {
    private final HikariDataSource ds;

    /**
     * Constructor for creating a new HikariConnectionPool object.
     *
     * @param hikariProperties the properties file name for initializing the HikariCP library.
     */
    public HikariConnectionPool(String hikariProperties) {
        HikariConfig config = new HikariConfig(hikariProperties);
        ds = new HikariDataSource(config);
    }

    /**
     * Method to get a connection to the database.
     *
     * @return a connection to the database.
     * @throws IllegalArgumentException if there is an error getting a connection.
     */
    public Connection getConnection() {
        try {
            Connection connection = ds.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return connection;
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Can`t get connection", exception);
        }
    }

    @Override
    public void close() {
        ds.close();
    }
}

