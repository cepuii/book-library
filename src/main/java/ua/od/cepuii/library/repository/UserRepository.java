package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.User;

import java.sql.SQLException;

public interface UserRepository extends AbstractEntityRepository<User> {
    boolean blockUser(int id) throws SQLException;
}
