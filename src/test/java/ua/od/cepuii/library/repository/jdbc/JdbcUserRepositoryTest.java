package ua.od.cepuii.library.repository.jdbc;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;
import ua.od.cepuii.library.util.ConnectionPoolTestDb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ua.od.cepuii.library.util.UserUtil.testUser;
import static ua.od.cepuii.library.util.UserUtil.testUserForUpdate;

class JdbcUserRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ua.od.cepuii.library.repository.jdbc.JdbcUserRepositoryTest.class);
    private static UserRepository userRepository;
    private static ConnectionPool connectionPool;

    private static final String INITIALIZE_DB_SCRIPT = "src/test/resources/testInitDatabase.sql";

    @BeforeAll
    public static void setUp() {
        connectionPool = new ConnectionPoolTestDb();
        userRepository = new JdbcUserRepository(new DbExecutorImpl<>(), connectionPool);
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
        long insert = userRepository.insert(testUser);
        System.out.println(insert);
        assertEquals(1000, insert);
    }

    @Test
    void getById() throws SQLException {
        long insert = userRepository.insert(testUser);
        testUser.setId(insert);
        User actualUser = userRepository.getById(insert).orElseThrow();
        testUser.setDateTime(actualUser.getDateTime());
        assertEquals(testUser, actualUser);
    }

    @Test
    void getByIdNotExist() throws SQLException {
        assertThrows(RepositoryException.class, () -> userRepository.getById(111));
    }

    @Test
    void update() throws SQLException {
        long insert = userRepository.insert(testUser);
        testUserForUpdate.setId(insert);
        assertNotEquals(testUser, testUserForUpdate);
        assertTrue(userRepository.update(testUserForUpdate));
        User updatedUser = userRepository.getById(insert).orElseThrow();
        testUserForUpdate.setDateTime(updatedUser.getDateTime());
        assertEquals(testUserForUpdate, updatedUser);
    }

    @Test
    void delete() throws SQLException {
        long insert = userRepository.insert(testUser);
        assertTrue(userRepository.delete(insert));
        assertThrows(RepositoryException.class, () -> userRepository.getById(insert));
    }

    @Test
    void deleteNotExist() throws SQLException {
        assertFalse(userRepository.delete(111));
    }

    @Test
    void getAll() throws SQLException {
        long insertFirst = userRepository.insert(testUser);
        testUser.setId(insertFirst);
        long insertSecond = userRepository.insert(testUserForUpdate);
        testUserForUpdate.setId(insertSecond);
        assertIterableEquals(List.of(testUser, testUserForUpdate), userRepository.getAll());
    }
}
