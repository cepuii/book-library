package ua.od.cepuii.library.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.od.cepuii.library.exception.DbConfigurationException;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool implements ConnectionPool {
    private final HikariDataSource ds;


    public HikariConnectionPool(String hikariProperties) {
        HikariConfig config = new HikariConfig(hikariProperties);
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

    @Override
    public void close() {
        ds.close();
    }
}

