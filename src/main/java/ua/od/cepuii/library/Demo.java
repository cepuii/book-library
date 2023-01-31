package ua.od.cepuii.library;

import ua.od.cepuii.library.db.ConnectionPool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Demo {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Class<?> aClass = Class.forName("ua.od.cepuii.library.db.HikariConnectionPool");
        Constructor<?> constructor = aClass.getConstructor(String.class);
        ConnectionPool connectionPool = (ConnectionPool) constructor.newInstance("/hikaridatasource.properties");
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("select email from users");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("email"));
        }
    }

}
