package ua.od.cepuii.library.context;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.service.ServiceFactory;
import ua.od.cepuii.library.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * AppContext: A singleton class that manages the context of the application, including creating and holding
 * instances of RepositoryFactory, ServiceFactory, UserService, BookService, LoanService, and ConnectionPool.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Getter
public class AppContext {
    private static final Logger log = LoggerFactory.getLogger(AppContext.class);
    private static AppContext appContext;
    private RepositoryFactory repositoryFactory;
    private ServiceFactory serviceFactory;
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    private ConnectionPool connectionPool;


    private final String clientIdForGoogle;

    private AppContext(Properties properties) {

        try {
            connectionPool = createConnectionPool(properties);
            repositoryFactory = createRepositoryFactory(properties);
            serviceFactory = createServiceFactory(properties);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
        userService = serviceFactory.getUserService();
        bookService = serviceFactory.getBookService();
        loanService = serviceFactory.getLoanService();
        log.info("context initialize finish");
        clientIdForGoogle = properties.getProperty("google.client.id");
    }

    private ServiceFactory createServiceFactory(Properties properties) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(properties.getProperty("service.factory"));
        Constructor<?> constructor = aClass.getConstructor(RepositoryFactory.class);
        return (ServiceFactory) constructor.newInstance(repositoryFactory);
    }

    private RepositoryFactory createRepositoryFactory(Properties properties) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Class<?> aClass = Class.forName(properties.getProperty("repository.factory"));
        Constructor<?> constructor = aClass.getConstructor(ConnectionPool.class);
        return (RepositoryFactory) constructor.newInstance(connectionPool);
    }

    private ConnectionPool createConnectionPool(Properties properties) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(properties.getProperty("connectionPool.className"));
        Constructor<?> constructor = aClass.getConstructor(String.class);
        return (ConnectionPool) constructor.newInstance(properties.getProperty("connectionPool.prop"));

    }

    /**
     * Get the client id to get credential from Google service.
     *
     * @return The client id.
     */
    public String getClientId() {
        return clientIdForGoogle;
    }

    /**
     * Get instance of the AppContext class.
     *
     * @return The singleton instance of the AppContext.
     */
    public static AppContext getInstance() {
        return appContext;
    }


    /**
     * Create an instance of the AppContext with the properties from the specified file.
     *
     * @param contextProperties The name of the file containing the context properties.
     */
    public static void createAppContext(String contextProperties) {
        Properties properties = loadProperties(contextProperties);
        appContext = new AppContext(properties);
    }

    private static Properties loadProperties(String contextProperties) {
        try (InputStream inputStream = AppContext.class.getResourceAsStream(contextProperties)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
        return new Properties();
    }

    /**
     * Destroy the context, releasing resources held by the ConnectionPool.
     */
    public void destroyContext() {
        connectionPool.close();
    }
}
