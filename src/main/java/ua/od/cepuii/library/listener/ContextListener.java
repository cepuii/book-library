package ua.od.cepuii.library.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.service.LoanService;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);
    private static final int UPDATE_FINE_PERIOD_HOURS = 24;
    private AppContext appContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("initialise context");

        String contextProperties = sce.getServletContext().getInitParameter("contextProperties");
        AppContext.createAppContext(contextProperties);
        appContext = AppContext.getInstance();
        //TODO run once in a day at the night
        //TODO add email sender before last day," attention please return a book"


        ScheduledExecutorService executorService = Executors
                .newSingleThreadScheduledExecutor();

        int delayHours = UPDATE_FINE_PERIOD_HOURS - LocalTime.now().getHour();
        log.info("task start after {} hours", delayHours);
        LoanService loanService = appContext.getServiceFactory().getLoanService();
        executorService.scheduleAtFixedRate(loanService::updateFine, delayHours, UPDATE_FINE_PERIOD_HOURS, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        appContext.destroyContext();
    }
}
