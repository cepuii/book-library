package ua.od.cepuii.library.repository.jdbc;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ConnectionPoolTestDb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcBookRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcBookRepositoryTest.class);
    private static BookRepository bookRepository;
    private static ConnectionPool connectionPool;

    private static final String INITIALIZE_DB_SCRIPT = "src/test/resources/testInitDatabase.sql";

    @BeforeAll
    public static void setUp() {
        connectionPool = new ConnectionPoolTestDb();
        bookRepository = new JdbcBookRepository(new DbExecutorImpl<>(), connectionPool);
    }

    @BeforeEach
    public void initDb() {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connectionPool.getConnection());
            scriptRunner.runScript(new FileReader(INITIALIZE_DB_SCRIPT));
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

    }

    @Test
    void insertExpectException() throws SQLException {
    }

    @Test
    void getById() throws SQLException {
    }

    @Test
    void getByIdNotExist() throws SQLException {
        assertThrows(NoSuchElementException.class, () -> bookRepository.getById(111).get());
    }

    @Test
    void update() throws SQLException {
    }

    @Test
    void delete() throws SQLException {
    }

    @Test
    void deleteNotExist() throws SQLException {
        assertFalse(bookRepository.delete(111));
    }

    @Test
    void getAll() throws SQLException {
    }

    @Test
    void getByTitle() {
    }

    @Test
    void getByAuthor() {
    }
}