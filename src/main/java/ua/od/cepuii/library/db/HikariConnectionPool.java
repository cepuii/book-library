package ua.od.cepuii.library.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.od.cepuii.library.exception.DbConfigurationException;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool implements ConnectionPool {
    private static final HikariConfig config = new HikariConfig("/hikaridatasource.properties");
    private static final HikariDataSource ds;

    static {
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            Connection connection = ds.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return connection;
        } catch (SQLException exception) {
            throw new DbConfigurationException("Can`t get connection", exception);
        }
    }
}

