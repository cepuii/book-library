package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.util.Collection;
import java.util.Optional;

public class UserService implements Service {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository = new JdbcRepositoryFactory().getUserRepository();

    public long create(User user) {
        return userRepository.insert(user);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        Optional<User> byEmail = userRepository.getByEmail(email);
        if (byEmail.isPresent() && byEmail.get().getPassword().equals(password)) {
            return byEmail.get();
        }
        return null;
    }

    public boolean delete(long id) {

        return false;
    }

    public boolean blockUnblock(long id, boolean isBlocked) {
        return userRepository.updateBlocked(id, isBlocked);
    }

    public int getPageAmount(Page page, FilterAndSortParams filterParam) {
        int recordsAmount = userRepository.getCount(filterParam);
        return (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
    }

    public Collection<UserTO> getAll(Page page, FilterAndSortParams params) {
        String orderBy = (params.getOrderBy().isBlank() ? "email" : params.getOrderBy()) + (params.isDescending() ? " DESC" : "");
        int limit = page.getNoOfRecords();
        int offset = page.getNoOfRecords() * (page.getCurrentPage() - 1);
        log.info("getAll books: {}; {} order {}, descending {}, limit {} , offset {}",
                params.getFirstParam(), params.getSecondParam(), params.getOrderBy(), params.isDescending(), limit, offset);
        Collection<User> users = userRepository.getAll(params, orderBy, limit, offset);
        return Mapper.mapToUserTO(users);
    }

    public boolean isExistEmail(String email) {
        return userRepository.getByEmail(email).isPresent();
    }
}
