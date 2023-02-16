package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.constants.AttributesName;
import ua.od.cepuii.library.dto.*;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.util.PasswordUtil;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ua.od.cepuii.library.constants.AttributesName.USER_ID;

public class UserService implements Service {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Report createOrUpdate(User user) {
        Report report = isExistEmail(user.getEmail());
        if (!report.hasErrors()) {
            if (ValidationUtil.isNew(user)) {
                user.setPassword(PasswordUtil.getHash(user.getPassword().getBytes()));
                long insert = userRepository.insert(user);
                log.info("user create , userId: {}", insert);
                report.addReport(USER_ID, String.valueOf(insert));
                report.addReport(AttributesName.SUCCESS, "message.user.create");
                return report;
            } else if (userRepository.update(user)) {
                log.info("user update, userId: {}", user.getId());
                report.addReport(AttributesName.SUCCESS, "message.user.update");
            } else {
                report.addError(AttributesName.WRONG_ACTION, "message.wrongAction.add");
            }
        }
        return report;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        Optional<User> byEmail = userRepository.getByEmail(email);
        if (byEmail.isPresent() && PasswordUtil.verify(byEmail.get().getPassword(), password.getBytes())) {
            return byEmail.get();
        }
        return null;
    }

    public Report blockUnblock(long userId, long blockUserId, boolean isBlocked) {
        Report report = Report.newInstance();
        if (userId == blockUserId) {
            report.addError(AttributesName.WRONG_ACTION, "message.block.yourself");
        } else if (userRepository.updateBlocked(blockUserId, isBlocked)) {
            report.addReport(AttributesName.SUCCESS, isBlocked ? "message.block.success" : "message.unblock.success");
        } else {
            report.addError(AttributesName.WRONG_ACTION, "message.somethingWrong.pass");
        }
        return report;
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

    public Report isExistEmail(String email) {
        Report report = Report.newInstance();
        if (userRepository.getByEmail(email).isPresent()) {
            report.addError("wrongAction", "message.signUp.email.exist");
        }
        return report;
    }

    public UserTO getById(long userId) {
        Optional<User> byId = userRepository.getById(userId);
        return Mapper.getUserTO(byId.orElseThrow(NoSuchElementException::new));
    }

    public Report updatePassword(long userId, String oldPassword, String newPassword) {
        Report report = ValidationUtil.validatePasswords(oldPassword, newPassword);
        if (!checkPassword(userId, oldPassword)) {
            report.addError(AttributesName.BAD_OLD_PASS, "message.change.password");
        }
        if (!report.hasErrors()) {
            String hash = PasswordUtil.getHash(newPassword.getBytes());
            if (userRepository.updatePassword(userId, hash)) {
                report.addReport(AttributesName.SUCCESS, "message.password.change");
            } else {
                report.addError(AttributesName.WRONG_ACTION, "message.somethingWrong.pass");
            }
        }
        return report;
    }

    public boolean checkPassword(long userId, String oldPassword) {
        Optional<User> user = userRepository.getById(userId);
        return user.isPresent() && PasswordUtil.verify(user.get().getPassword(), oldPassword.getBytes());
    }
}
