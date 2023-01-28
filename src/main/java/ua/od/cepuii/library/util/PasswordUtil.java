package ua.od.cepuii.library.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil {

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    private static final int ITERATIONS = 4;
    private static final int MEMORY_MB = 1024 * 1024;

    private static final int PARALLELISM = 8;

    private PasswordUtil() {
    }

    public static String getHash(byte[] password) {
        return argon2.hash(ITERATIONS, MEMORY_MB, PARALLELISM, password);
    }

    public static boolean verify(String hash, byte[] password) {
        return argon2.verify(hash, password);
    }

}
