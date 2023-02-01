package ua.od.cepuii.library.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.jdbc.PSQLSavepoint;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;
import ua.od.cepuii.library.util.BookUtil;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.BookUtil.LIMIT;
import static ua.od.cepuii.library.util.BookUtil.OFFSET;
import static ua.od.cepuii.library.util.LoanUtil.*;

class JdbcLoanRepositoryTest {

    private final QueryExecutor<Loan> mockLoanExecutor = mock(QueryExecutor.class);
    private final ConnectionPool mockConnectionPool = mock(ConnectionPool.class);
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private JdbcLoanRepository loanRepository = new JdbcLoanRepository(mockLoanExecutor, mockConnectionPool);

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.setSavepoint()).thenReturn(new PSQLSavepoint(""));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
//        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void insert() throws SQLException {
        when(mockLoanExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertEquals(LOAN_ID, loanRepository.insert(NEW_LOAN));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockLoanExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void insertCatchException() throws SQLException {
        when(mockLoanExecutor.insert(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertEquals(-1, loanRepository.insert(NEW_LOAN));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockLoanExecutor, times(0)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback(any(Savepoint.class));
    }

    @Test
    void getById() throws SQLException {
        when(mockLoanExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(Optional.of(LOAN));
        assertEquals(LOAN, loanRepository.getById(LOAN_ID).get());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockLoanExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void getByIdCatchException() throws SQLException {
        when(mockLoanExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        assertEquals(Optional.empty(), loanRepository.getById(LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockLoanExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void update() throws SQLException {
        when(mockLoanExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertTrue(() -> loanRepository.update(LOAN));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void updateCatchException() throws SQLException {
        when(mockLoanExecutor.update(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);

        assertFalse(() -> loanRepository.update(LOAN));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void delete() throws SQLException {
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertTrue(() -> loanRepository.delete(BookUtil.LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void deleteCatchException() throws SQLException {
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenThrow(SQLException.class);

        assertFalse(() -> loanRepository.delete(BookUtil.LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void deleteNotFoundId() throws SQLException {
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(false);

        assertFalse(() -> loanRepository.delete(NOT_FOUND_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void getAll() throws SQLException {
        when(mockLoanExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(List.of(LOAN));
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(List.of(LOAN), loanRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockLoanExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getAllCatchException() throws SQLException {
        when(mockLoanExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(Collections.emptyList(), loanRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockLoanExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getAllByUserId() throws SQLException {
        when(mockLoanExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(List.of(LOAN));
        assertIterableEquals(List.of(LOAN), loanRepository.getAllByUserId(LOAN_ID, LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockLoanExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void getAllByUserIdCatchException() throws SQLException {
        when(mockLoanExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        assertIterableEquals(List.of(), loanRepository.getAllByUserId(LOAN_ID, LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockLoanExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void updateStatus() throws SQLException {

        when(mockLoanExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockLoanExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertEquals(LOAN_ID, loanRepository.insert(NEW_LOAN));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockLoanExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockLoanExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();

    }

    @Test
    void getBooksIdsByUserId() throws SQLException {
        doNothing().when(mockPreparedStmnt).setLong(anyInt(), anyLong());
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong(anyString())).thenReturn(BOOK_ID);
        assertIterableEquals(List.of(BOOK_ID), loanRepository.getBooksIdsByUserId(USER_ID));
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getLoanHistory() {
    }

    @Test
    void updateFine() {
    }
}