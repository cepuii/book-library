package ua.od.cepuii.library.util;

import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.Role;

import java.time.LocalDateTime;

public class UserUtil {

    public final static User testUser = new User(0, "test@email", "qwqwrewsdfec", LocalDateTime.MAX, false, Role.READER);
    public final static User testUserForUpdate = new User(0, "update@email", "qwqwrewsdfec", LocalDateTime.MIN, true, Role.LIBRARIAN);


}
