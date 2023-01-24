package ua.od.cepuii.library.util;

import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.Role;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class UserUtil {

    public static final long USER_ID = 1000;
    public static final long NOT_FOUND_USER_ID = 999;
    public static final long NEW_ID = 0;

    public static final String USER_EMAIL = "test@test.test";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";
    public static final Role ROLE = Role.READER;


    public static final User USER = User.builder()
            .id(USER_ID)
            .email(USER_EMAIL)
            .password(PASSWORD)
            .dateTime(LocalDateTime.of(2000, 1, 1, 1, 1))
            .role(Role.READER)
            .build();

    public static final User USER_WITH_NEW_PASSWORD = User.builder()
            .id(USER_ID)
            .email(USER_EMAIL)
            .password(NEW_PASSWORD)
            .dateTime(LocalDateTime.now())
            .role(Role.READER)
            .build();
    public static final UserTO USER_TO = UserTO.builder()
            .id(USER_ID)
            .email(USER_EMAIL)
            .registered(Date.from(LocalDateTime.of(2000, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)))
            .role(Role.READER)
            .build();
    public static final User NEW_USER = User.builder()
            .id(NEW_ID)
            .email(USER_EMAIL)
            .password(PASSWORD)
            .role(Role.READER)
            .build();
    public static final User NOT_FOUND_USER = User.builder()
            .id(NOT_FOUND_USER_ID)
            .email(USER_EMAIL)
            .password(PASSWORD)
            .role(Role.READER)
            .build();

}
