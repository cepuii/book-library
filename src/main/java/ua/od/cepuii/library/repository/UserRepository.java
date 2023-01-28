package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractEntityRepository<User> {

    boolean updatePassword(long userId, String password);

    Optional<User> getByEmail(String email);

    int getCount(FilterParams filterParam);

    boolean updateBlocked(long id, boolean isBlocked);
}
