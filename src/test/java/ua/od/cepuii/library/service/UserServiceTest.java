package ua.od.cepuii.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.BookUtil.NOT_FOUND_ID;
import static ua.od.cepuii.library.util.BookUtil.NO_OF_RECORDS;
import static ua.od.cepuii.library.util.UserUtil.*;

 class UserServiceTest {

     @Mock
     private UserRepository userRepository;
     @InjectMocks
     private UserService userService;

     @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this);
     }
//
//    ublic long createOrUpdate(User user) {
//        if (ValidationUtil.isNew(user)) {
//            long insert = userRepository.insert(user);
//            log.info("user create and save, userId: {}", insert);
//            return insert;
//        }
//        userRepository.update(user);
//        log.info("user update, userId: {}", user.getId());
//        return user.getId();
//    }

    @Test
    void insert() {
        when(userRepository.insert(NEW_USER)).thenReturn(USER_ID);
        assertEquals(USER_ID, userService.createOrUpdate(NEW_USER));
        verify(userRepository, times(1)).insert(NEW_USER);
        verify(userRepository, times(0)).update(any(User.class));

    }

    @Test
    void update() {
        when(userRepository.update(USER)).thenReturn(true);
        assertEquals(USER_ID, userService.createOrUpdate(USER));
        verify(userRepository, times(0)).insert(any(User.class));
        verify(userRepository, times(1)).update(USER);

    }

    @Test
    void getUserByEmailAndPassword() {
        when(userRepository.getByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        assertEquals(USER, userService.getUserByEmailAndPassword(USER.getEmail(), USER.getPassword()));
    }

    @Test
    void getUserByEmailAndPasswordNotFound() {
        when(userRepository.getByEmail(NOT_FOUND_USER.getEmail())).thenReturn(Optional.empty());
        assertNull(userService.getUserByEmailAndPassword(NOT_FOUND_USER.getEmail(), NOT_FOUND_USER.getPassword()));
    }

    @Test
    void blockUser() {
        when(userRepository.updateBlocked(USER_ID, true)).thenReturn(true);
        assertTrue(() -> userService.blockUnblock(USER_ID, true));

    }


    @Test
    void getPageAmount() {
        Page page = mock(Page.class);
        when(page.getNoOfRecords()).thenReturn(NO_OF_RECORDS);
        FilterParams filter = mock(FilterParams.class);
        when(userRepository.getCount(filter)).thenReturn(3);
        assertEquals(1, userService.getPageAmount(page, filter));
    }

    @Test
    void getAll() {
        Page page = mock(Page.class);
        FilterParams filter = mock(FilterParams.class);
        when(filter.getOrderBy()).thenReturn("email");
        when(filter.isDescending()).thenReturn(false);
        when(page.getLimit()).thenReturn(5);
        when(page.getOffset()).thenReturn(0);
        when(userRepository.getAll(filter, "email", page.getLimit(), page.getOffset())).thenReturn(List.of(USER));
        assertIterableEquals(List.of(USER_TO), userService.getAll(page, filter));
        verify(userRepository, times(1)).getAll(any(FilterParams.class), anyString(), anyInt(), anyInt());
    }


    @Test
    void isExist() {
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(USER));
        assertTrue(() -> userService.isExistEmail(USER_EMAIL));
    }

    @Test
    void getById() {
        when(userRepository.getById(USER_ID)).thenReturn(Optional.ofNullable(USER));
        assertEquals(USER_TO, userService.getById(USER_ID));
    }

    @Test
    void getByIdNotFound() {
        when(userRepository.getById(NOT_FOUND_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getById(USER_ID));
    }

    @Test
    void updatePassword() {
        when(userRepository.updatePassword(USER_ID, NEW_PASSWORD)).thenReturn(true);
        assertTrue(() -> userService.updatePassword(USER_ID, NEW_PASSWORD));
    }

    @Test
    void checkPassword() {
        when(userRepository.getById(USER_ID)).thenReturn(Optional.ofNullable(USER));
        assertTrue(() -> userService.checkPassword(USER_ID, USER.getPassword()));
    }


}
