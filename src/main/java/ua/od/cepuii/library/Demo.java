package ua.od.cepuii.library;


import ua.od.cepuii.library.service.UserService;

import java.sql.SQLException;

public class Demo {

    public static void main(String[] args) throws SQLException {
        UserService service = new UserService();

        System.out.println(service.isExist("ivan@email", "qwerty"));

    }

}
