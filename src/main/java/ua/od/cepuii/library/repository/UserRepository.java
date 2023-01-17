package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractEntityRepository<User> {

    Optional<User> getByEmail(String email);

    int getCount(FilterAndSortParams filterParam);

    boolean updateBlocked(long id, boolean isBlocked);
}
