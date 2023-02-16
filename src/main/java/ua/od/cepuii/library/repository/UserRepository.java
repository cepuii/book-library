package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.User;

import java.util.Optional;

/**
 * Interface for a repository of {@link User} entities.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface UserRepository extends EntityRepository<User> {
    /**
     * Updates the password of the user with the given id.
     *
     * @param userId   the id of the user whose password is to be updated.
     * @param password the new password for the user.
     * @return {@code true} if the password is updated successfully, {@code false} otherwise.
     */
    boolean updatePassword(long userId, String password);

    /**
     * Retrieves the user with the given email.
     *
     * @param email the email of the user to be retrieved.
     * @return an {@link Optional} containing the user if it exists, an empty {@link Optional} otherwise.
     */
    Optional<User> getByEmail(String email);

    /**
     * Updates the blocked status of the user with the given id.
     *
     * @param userId    the id of the user whose blocked status is to be updated.
     * @param isBlocked the new blocked status for the user.
     * @return {@code true} if the blocked status is updated successfully, {@code false} otherwise.
     */
    boolean updateBlocked(long userId, boolean isBlocked);
}
