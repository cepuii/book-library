package ua.od.cepuii.library.db;

import java.sql.Connection;
/**
 * The {@code ConnectionPool} interface defines the methods for creating and closing database connections.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface ConnectionPool {
    Connection getConnection();

    void close();
}
