package ua.od.cepuii.library.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.jdbc.PSQLSavepoint;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.BookUtil.LIMIT;
import static ua.od.cepuii.library.util.BookUtil.OFFSET;
import static ua.od.cepuii.library.util.LoanUtil.NOT_FOUND_ID;
import static ua.od.cepuii.library.util.UserUtil.*;

class JdbcUserRepositoryTest {

    private final QueryExecutor<User> mockUserExecutor = mock(QueryExecutor.class);
    private final ConnectionPool mockConnectionPool = mock(ConnectionPool.class);
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private JdbcUserRepository userRepository = new JdbcUserRepository(mockUserExecutor, mockConnectionPool);

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.setSavepoint()).thenReturn(new PSQLSavepoint(""));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
    }


    @Test
    void insert() throws SQLException {
        when(mockUserExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(USER_ID);
        when(mockUserExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertEquals(USER_ID, userRepository.insert(NEW_USER));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();

    }

    @Test
    void insertCatchException() throws SQLException {
        when(mockUserExecutor.insert(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);

        assertEquals(-1, userRepository.insert(NEW_USER));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void getById() throws SQLException {
        when(mockUserExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(Optional.of(USER));
        assertEquals(USER, userRepository.getById(USER_ID).get());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockUserExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void getByIdCatchException() throws SQLException {
        when(mockUserExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        assertEquals(Optional.empty(), userRepository.getById(USER_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockUserExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void update() throws SQLException {
        when(mockUserExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertTrue(() -> userRepository.update(USER));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void updateCatchException() throws SQLException {
        when(mockUserExecutor.update(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);

        assertFalse(() -> userRepository.update(USER));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void delete() throws SQLException {
        when(mockUserExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertTrue(() -> userRepository.delete(USER_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void deleteCatchException() throws SQLException {
        when(mockUserExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenThrow(SQLException.class);

        assertFalse(() -> userRepository.delete(USER_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void deleteNotFoundId() throws SQLException {
        when(mockUserExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(false);

        assertFalse(() -> userRepository.delete(NOT_FOUND_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void getAll() throws SQLException {
        when(mockUserExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(List.of(USER));
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(List.of(USER), userRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockUserExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getAllCatchException() throws SQLException {
        when(mockUserExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(Collections.emptyList(), userRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockUserExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void updatePassword() throws SQLException {

        when(mockUserExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertTrue(() -> userRepository.updatePassword(USER_ID, PASSWORD));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();

    }

    @Test
    void updatePasswordCatchException() throws SQLException {

        when(mockUserExecutor.update(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);

        assertFalse(() -> userRepository.updatePassword(USER_ID, PASSWORD));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockUserExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();

    }


}