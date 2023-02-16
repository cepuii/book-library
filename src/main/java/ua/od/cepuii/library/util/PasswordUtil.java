package ua.od.cepuii.library.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
/**
 * This class provides utility methods for password hashing and verification using the Argon2 algorithm.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class PasswordUtil {

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    private static final int ITERATIONS = 4;
    private static final int MEMORY_MB = 1024 * 1024;

    private static final int PARALLELISM = 8;

    private PasswordUtil() {
    }

    /**
     * Generates a hash of the given password using the Argon2 algorithm.
     *
     * @param password the password to be hashed
     * @return the hashed password as a string
     */
    public static String getHash(byte[] password) {
        return argon2.hash(ITERATIONS, MEMORY_MB, PARALLELISM, password);
    }

    /**
     * Verifies if the given password matches the given hash using the Argon2 algorithm.
     *
     * @param hash     the hashed password as a string
     * @param password the password to be verified
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verify(String hash, byte[] password) {
        return argon2.verify(hash, password);
    }

}
