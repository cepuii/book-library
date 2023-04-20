package ua.od.cepuii.library.repository.jdbc.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.od.cepuii.library.entity.AbstractEntity;

class QueryExecutorImplTest {

  private final List<Object> params = List.of("John Doe", 1, 1999, 3);
  private final String sql = "INSERT INTO book (title, publication_id, date_publication, total)  VALUES (?,?,?,?)";
  private final long ID = 123L;
  @Mock
  private Connection connectionMock;
  @Mock
  private PreparedStatement preparedStatementMock;
  @Mock
  private ResultSet resultSetMock;
  @InjectMocks
  private QueryExecutorImpl<AbstractEntity> executor = new QueryExecutorImpl<>();

  @BeforeEach
  public void setup() throws SQLException {
    MockitoAnnotations.openMocks(this);
    when(connectionMock.prepareStatement(anyString(), anyInt()))
        .thenReturn(preparedStatementMock);
    when(preparedStatementMock.getGeneratedKeys()).thenReturn(resultSetMock);
    doNothing().when(preparedStatementMock).setObject(anyInt(), any());
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getLong("id")).thenReturn(ID);
  }

  @Test
  void testInsertReturnsGeneratedId() throws SQLException {
    long result = executor.insert(connectionMock, sql, params);

    // Assert
    assertEquals(ID, result);
    verify(connectionMock, times(1)).prepareStatement(anyString(), anyInt());
    verify(preparedStatementMock, times(1)).executeUpdate();
    verify(preparedStatementMock, times(1)).getGeneratedKeys();
    verify(resultSetMock, times(1)).next();
    verify(resultSetMock, times(1)).getLong("id");
  }

  @Test()
  void testInsertThrowsExceptionOnSqlError() throws SQLException {
    // Arrange
    doThrow(new SQLException()).when(preparedStatementMock).executeUpdate();

    // Act
    assertThrows(SQLException.class, () -> executor.insert(connectionMock, sql, params));

  }

}