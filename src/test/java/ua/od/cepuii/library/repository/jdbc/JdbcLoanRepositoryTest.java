package ua.od.cepuii.library.repository.jdbc;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ConnectionPoolTestDb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ua.od.cepuii.library.util.LoanUtil.*;

public class JdbcLoanRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(ua.od.cepuii.library.repository.jdbc.JdbcLoanRepositoryTest.class);
    private static LoanRepository loanRepository;

    private static ConnectionPool connectionPool;

    private static final String INITIALIZE_DB_SCRIPT = "src/test/resources/testInitDatabase.sql";
    private static final String POPULATE_DB_SCRIPT = "src/test/resources/populateForTests.sql";

    @BeforeAll
    public static void setUp() {
        connectionPool = new ConnectionPoolTestDb();
        loanRepository = new JdbcLoanRepository(new DbExecutorImpl<>(), connectionPool);
    }

    @BeforeEach
    public void initDb() {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connectionPool.getConnection());
            scriptRunner.runScript(new FileReader(INITIALIZE_DB_SCRIPT));
            scriptRunner.runScript(new FileReader(POPULATE_DB_SCRIPT));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void closeAll() {
        //maybe need to close connection, gotta figure out
    }

    @Test
    void insertOrdinaryBehavior() throws SQLException {
        long insert = loanRepository.insert(loan);
        assertEquals(1005, insert);
    }

    @Test
    void getById() throws SQLException {
        long insert = loanRepository.insert(loan);
        loan.setId(insert);
        assertEquals(loan, loanRepository.getById(insert).orElseThrow());
    }

    @Test
    void getByIdNotExist() throws SQLException {
        assertThrows(RepositoryException.class, () -> loanRepository.getById(FAIL_ID));
    }

    @Test
    void update() throws SQLException {
        long insert = loanRepository.insert(loan);
        loan.setId(insert);
        loan.setStatus(LoanStatus.COMPLETE);
        assertTrue(loanRepository.update(loan));
        assertEquals(loan, loanRepository.getById(insert).orElseThrow());
    }

    @Test
    void delete() throws SQLException {
        long insert = loanRepository.insert(loan);
        assertTrue(loanRepository.delete(insert));
        assertThrows(RepositoryException.class, () -> loanRepository.getById(insert));
    }

    @Test
    void deleteNotExist() throws SQLException {
        assertFalse(loanRepository.delete(FAIL_ID));
    }

    @Test
    void getAll() throws SQLException {
        long insert = loanRepository.insert(loan);
        loan.setId(insert);
        long insertSecond = loanRepository.insert(loanSecond);
        loanSecond.setId(insertSecond);
        assertIterableEquals(List.of(loan, loanSecond), loanRepository.getAll());
    }
}


