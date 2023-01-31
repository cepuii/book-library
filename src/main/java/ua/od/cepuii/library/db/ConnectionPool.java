package ua.od.cepuii.library.db;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();

    void close();
}
