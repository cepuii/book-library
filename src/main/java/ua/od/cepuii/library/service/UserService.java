package ua.od.cepuii.library.service;

import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

public class UserService {

    RepositoryFactory factory = new JdbcRepositoryFactory();

    public long create(String email, String password, Role role) {
        return 0;
    }

    public boolean isExist(String email, String password) {
        return true;
    }

    public boolean delete(long id) {

        return false;
    }

    public boolean block(long id) {
        return false;
    }

    public boolean unblock(long id) {
        return false;
    }

}
