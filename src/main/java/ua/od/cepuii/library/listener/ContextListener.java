package ua.od.cepuii.library.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.db.HikariConnectionPool;
import ua.od.cepuii.library.service.LoanService;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

    private static final int UPDATE_FINE_PERIOD_HOURS = 24;
    private static final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool connectionPool = new HikariConnectionPool();
        connectionPool.getConnection();
        log.info("initialise context");

        ScheduledExecutorService executorService = Executors
                .newSingleThreadScheduledExecutor();
        //TODO run once in a day at the night
        //TODO add email sender before last day," attention please return a book"


        int delayHours = UPDATE_FINE_PERIOD_HOURS - LocalTime.now().getHour();

        executorService.scheduleAtFixedRate(loanService::updateFine, delayHours, UPDATE_FINE_PERIOD_HOURS, TimeUnit.HOURS);
    }
}
