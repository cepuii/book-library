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

/**
 * A service class that provides operations related to users.
 * Implements the Pageable interface for pagination support.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class UserService implements Pageable {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates or updates a user in the repository.
     * If the user is new, sets the password hash and inserts it into the repository.
     * If the user already exists, updates it in the repository.
     *
     * @param user the user to create or update
     * @return a report containing any errors or success messages
     */
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

    /**
     * Retrieves a user from the repository by email and password.
     *
     * @param email    the email of the user to retrieve
     * @param password the password of the user to retrieve
     * @return the retrieved user, or null if no matching user was found
     */
    public User getUserByEmailAndPassword(String email, String password) {
        Optional<User> byEmail = userRepository.getByEmail(email);
        if (byEmail.isPresent() && PasswordUtil.verify(byEmail.get().getPassword(), password.getBytes())) {
            return byEmail.get();
        }
        return null;
    }

    /**
     * Blocks or unblocks a user by ID.
     *
     * @param userId      the ID of the user who is blocking or unblocking another user
     * @param blockUserId the ID of the user who is being blocked or unblocked
     * @param isBlocked   true to block the user, false to unblock the user
     * @return a report containing any errors or success messages
     */
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

    /**
     * Calculates the number of pages needed to display all records in the repository with the given filter.
     *
     * @param page        the page settings, including the number of records per page
     * @param filterParam the filter parameters to apply to the query
     * @return the number of pages needed to display all records with the given filter
     */
    public int getPageAmount(Page page, FilterParams filterParam) {
        int recordsAmount = userRepository.getCount(filterParam);
        return (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
    }

    /**
     * Retrieves a collection of users from the repository, paginated and filtered as specified.
     *
     * @param page   the page settings, including the limit and offset
     * @param params the filter parameters to apply to the query
     * @return a collection of UserTO objects representing the retrieved users
     */
    public Collection<UserTO> getAll(Page page, FilterParams params) {
        String orderBy = (params.getOrderBy().isBlank() ? "email" : params.getOrderBy()) + (params.isDescending() ? " DESC" : "");
        log.info("getAll books: {}; {} order {}, descending {}, limit {} , offset {}",
                params.getFirstParam(), params.getSecondParam(), params.getOrderBy(), params.isDescending(), page.getLimit(), page.getOffset());
        Collection<User> users = userRepository.getAll(params, orderBy, page.getLimit(), page.getOffset());
        return Mapper.mapToUserTO(users);
    }

    /**
     * Checks whether a given email address is already in use in the repository.
     *
     * @param email the email address to check
     * @return a report containing any errors or success messages
     */
    public Report isExistEmail(String email) {
        Report report = Report.newInstance();
        if (userRepository.getByEmail(email).isPresent()) {
            report.addError("wrongAction", "message.signUp.email.exist");
        }
        return report;
    }

    /**
     * Retrieves a UserTO object representing the user with the given ID.
     * Throws a NoSuchElementException if no user with the given ID was found.
     *
     * @param userId the ID of the user to retrieve
     * @return a UserTO object representing the retrieved user
     * @throws NoSuchElementException if no user with the given ID was found
     */
    public UserTO getById(long userId) {
        Optional<User> byId = userRepository.getById(userId);
        return Mapper.getUserTO(byId.orElseThrow(NoSuchElementException::new));
    }

    /**
     * Updates a user's password in the repository.
     * Validates that the old password matches the current password before updating.
     *
     * @param userId      the ID of the user whose password to update
     * @param oldPassword the user's current password
     * @param newPassword the new password to set
     * @return a report containing any errors or success messages
     */
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

    /**
     * Checks whether a given password matches the current password of the user with
     * the specified user ID.
     *
     * @param userId      the ID of the user to check the password for
     * @param oldPassword the password to check
     * @return true if the password matches the current password of the user with
     */
    public boolean checkPassword(long userId, String oldPassword) {
        Optional<User> user = userRepository.getById(userId);
        return user.isPresent() && PasswordUtil.verify(user.get().getPassword(), oldPassword.getBytes());
    }
}
