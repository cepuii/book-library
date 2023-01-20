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
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService implements Service {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository = new JdbcRepositoryFactory().getUserRepository();

    public long createOrUpdate(User user) {
        if (ValidationUtil.isNew(user)) {
            long insert = userRepository.insert(user);
            log.info("user create and save, userId: {}", insert);
            return insert;
        }
        userRepository.update(user);
        log.info("user update, userId: {}", user.getId());
        return user.getId();
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
        log.info("getAll books: {}; {} order {}, descending {}, limit {} , offset {}",
                params.getFirstParam(), params.getSecondParam(), params.getOrderBy(), params.isDescending(), page.getLimit(), page.getOffset());
        Collection<User> users = userRepository.getAll(params, orderBy, page.getLimit(), page.getOffset());
        return Mapper.mapToUserTO(users);
    }

    public boolean isExistEmail(String email) {
        return userRepository.getByEmail(email).isPresent();
    }

    public UserTO getById(long userId) {
        Optional<User> byId = userRepository.getById(userId);
        return Mapper.getUserTO(byId.orElseThrow(NoSuchElementException::new));
    }

    public boolean updatePassword(long userId, String newPassword) {
        return userRepository.updatePassword(userId, newPassword);
    }

    public boolean checkPassword(long userId, String oldPassword) {
        Optional<User> byId = userRepository.getById(userId);
        return byId.orElse(new User()).getPassword().equals(oldPassword);
    }
}
