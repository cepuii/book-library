package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.User;

public interface UserRepository {

    User create(User user);

    User getById(int id);

    User update(User user);

    boolean delete(int id);

    boolean blockUser(int id);
}
