package ua.od.cepuii.library.repository.jdbc;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.repository.AuthorRepository;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ConnectionPoolTestDb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static ua.od.cepuii.library.util.AuthorUtil.testAuthor;
import static ua.od.cepuii.library.util.AuthorUtil.testAuthorForUpdate;

class JdbcAuthorRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcAuthorRepositoryTest.class);
    private static AuthorRepository authorRepository;
    private static ConnectionPool connectionPool;

    private static final String INITIALIZE_DB_SCRIPT = "src/test/resources/testInitDatabase.sql";

    @BeforeAll
    public static void setUp() {
        connectionPool = new ConnectionPoolTestDb();
        authorRepository = new JdbcAuthorRepository(new DbExecutorImpl<>(), connectionPool);
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
        long insert = authorRepository.insert(testAuthor);
        System.out.println(insert);
        assertEquals(1000, insert);
    }

    @Test
    void getById() throws SQLException {
        long insert = authorRepository.insert(testAuthor);
        testAuthor.setId(insert);
        assertEquals(testAuthor, authorRepository.getById(insert).get());
    }

    @Test
    void getByIdNotExist() throws SQLException {
        assertThrows(NoSuchElementException.class, () -> authorRepository.getById(111).get());
    }

    @Test
    void update() throws SQLException {
        long insert = authorRepository.insert(testAuthor);
        testAuthorForUpdate.setId(insert);
        assertNotEquals(testAuthor, testAuthorForUpdate);
        assertTrue(authorRepository.update(testAuthorForUpdate));
        assertEquals(testAuthorForUpdate, authorRepository.getById(insert).get());
    }

    @Test
    void delete() throws SQLException {
        long insert = authorRepository.insert(testAuthor);
        assertTrue(authorRepository.delete(insert));
        assertThrows(NoSuchElementException.class, () -> authorRepository.getById(insert).get());
    }

    @Test
    void deleteNotExist() throws SQLException {
        assertFalse(authorRepository.delete(111));
    }

    @Test
    void getAll() throws SQLException {
        long insertFirst = authorRepository.insert(testAuthor);
        testAuthor.setId(insertFirst);
        long insertSecond = authorRepository.insert(testAuthorForUpdate);
        testAuthorForUpdate.setId(insertSecond);
//        assertIterableEquals(List.of(testAuthor, testAuthorForUpdate), authorRepository.getAll(orderBy, descending, , currentPage, ));
    }
}