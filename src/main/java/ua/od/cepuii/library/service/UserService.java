package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.util.PasswordUtil;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService implements Service {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        if (byEmail.isPresent() && PasswordUtil.verify(byEmail.get().getPassword(), password.getBytes())) {
            return byEmail.get();
        }
        return null;
    }

    public boolean blockUnblock(long id, boolean isBlocked) {
        return userRepository.updateBlocked(id, isBlocked);
    }

    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = userRepository.getCount(filterParam);
        return (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
    }

    public Collection<UserTO> getAll(Page page, FilterParams params) {
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
        String hash = PasswordUtil.getHash(newPassword.getBytes());
        return userRepository.updatePassword(userId, hash);
    }

    public boolean checkPassword(long userId, String oldPassword) {
        Optional<User> user = userRepository.getById(userId);
        return user.isPresent() && PasswordUtil.verify(user.get().getPassword(), oldPassword.getBytes());
    }
}
