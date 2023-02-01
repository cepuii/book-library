package ua.od.cepuii.library.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.jdbc.PSQLSavepoint;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
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
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.BookUtil.*;

class JdbcBookRepositoryTest {

    private final QueryExecutor<Book> mockBookExecutor = mock(QueryExecutor.class);
    private final QueryExecutor<Author> mockAuthorExecutor = mock(QueryExecutor.class);
    private final ConnectionPool mockConnectionPool = mock(ConnectionPool.class);
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    private JdbcBookRepository bookRepository = new JdbcBookRepository(mockBookExecutor, mockAuthorExecutor, mockConnectionPool);

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.setSavepoint()).thenReturn(new PSQLSavepoint(""));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStmnt.toString()).thenReturn("test execute");
        when(mockResultSet.getLong("id")).thenReturn(LOAN_ID);
    }

    @Test
    void insert() throws SQLException {
        when(mockBookExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockBookExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);
        when(mockBookExecutor.insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertEquals(LOAN_ID, bookRepository.insert(NEW_BOOK));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(3)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void insertThrowSQLException() throws SQLException {
        when(mockBookExecutor.insert(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);
        when(mockBookExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);
        when(mockBookExecutor.insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertEquals(-1, bookRepository.insert(NEW_BOOK));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void insertAndUpdateAuthor() throws SQLException {
        when(mockBookExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockBookExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);
        when(mockBookExecutor.insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList())).thenReturn(true);
        Book newBook = NEW_BOOK;
        newBook.setAuthors(AUTHORS_WITH_OLD);
        assertEquals(LOAN_ID, bookRepository.insert(newBook));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(3)).insert(any(Connection.class), anyString(), anyList());
        verify(mockBookExecutor, times(2)).insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList());
        verify(mockBookExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void getById() throws SQLException {
        when(mockBookExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(Optional.of(BOOK));
        assertEquals(BOOK, bookRepository.getById(LOAN_ID).get());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockBookExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void getByIdCatchException() throws SQLException {
        when(mockBookExecutor.selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        assertEquals(Optional.empty(), bookRepository.getById(LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockBookExecutor, times(1)).selectByParams(any(Connection.class), anyString(), anyList(), any(Function.class));
    }

    @Test
    void update() throws SQLException {
        when(mockBookExecutor.update(any(Connection.class), anyString(), anyList())).thenReturn(true);
        when(mockBookExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockBookExecutor.insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertTrue(() -> bookRepository.update(BOOK));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockBookExecutor, times(2)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void updateCatchException() throws SQLException {
        when(mockBookExecutor.update(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);
        when(mockBookExecutor.insert(any(Connection.class), anyString(), anyList())).thenReturn(LOAN_ID);
        when(mockBookExecutor.insertWithoutGeneratedKey(any(Connection.class), anyString(), anyList())).thenReturn(true);

        assertFalse(() -> bookRepository.update(BOOK));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).update(any(Connection.class), anyString(), anyList());
        verify(mockBookExecutor, times(0)).insert(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void delete() throws SQLException {
        when(mockBookExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(true);

        assertTrue(() -> bookRepository.delete(LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void deleteCatchException() throws SQLException {
        when(mockBookExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenThrow(SQLException.class);

        assertFalse(() -> bookRepository.delete(LOAN_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(0)).commit();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    void deleteNotFoundId() throws SQLException {
        when(mockBookExecutor.queryById(any(Connection.class), anyString(), anyLong())).thenReturn(false);

        assertFalse(() -> bookRepository.delete(NOT_FOUND_ID));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).setSavepoint();
        verify(mockBookExecutor, times(1)).queryById(any(Connection.class), anyString(), anyLong());
        verify(mockConnection, times(1)).commit();
    }

    @Test
    void getAll() throws SQLException {
        when(mockBookExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenReturn(List.of(BOOK));
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(List.of(BOOK), bookRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockBookExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getAllCatchException() throws SQLException {
        when(mockBookExecutor.selectAll(any(Connection.class), anyString(), anyList(), any(Function.class))).thenThrow(SQLException.class);
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");
        assertIterableEquals(Collections.emptyList(), bookRepository.getAll(filterParams, "", LIMIT, OFFSET));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockBookExecutor, times(1)).selectAll(any(Connection.class), anyString(), anyList(), any(Function.class));
        verify(mockConnection, times(0)).commit();
    }

    @Test
    void getCount() throws SQLException {
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");

        when(mockBookExecutor.selectCount(any(Connection.class), anyString(), anyList())).thenReturn(COUNT_RESULT);
        FilterParams filter = mock(FilterParams.class);
        when(filter.getFirstParam()).thenReturn("");
        when(filter.getSecondParam()).thenReturn("");
        assertEquals(COUNT_RESULT, bookRepository.getCount(filter));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockBookExecutor, times(1)).selectCount(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();

    }

    @Test
    void getCountCatchException() throws SQLException {
        FilterParams filterParams = mock(FilterParams.class);
        when(filterParams.getFirstParam()).thenReturn("");
        when(filterParams.getSecondParam()).thenReturn("");

        when(mockBookExecutor.selectCount(any(Connection.class), anyString(), anyList())).thenThrow(SQLException.class);
        FilterParams filter = mock(FilterParams.class);
        when(filter.getFirstParam()).thenReturn("");
        when(filter.getSecondParam()).thenReturn("");
        assertEquals(0, bookRepository.getCount(filter));

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(0)).setSavepoint();
        verify(mockBookExecutor, times(1)).selectCount(any(Connection.class), anyString(), anyList());
        verify(mockConnection, times(0)).commit();

    }
}
