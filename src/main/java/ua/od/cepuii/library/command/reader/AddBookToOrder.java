package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.service.LoanService;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

/**
 * This class is responsible for adding a book to a loan (order)
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class AddBookToOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(AddBookToOrder.class);

    private final LoanService loanService = AppContext.getInstance().getLoanService();

    /**
     * The execute method takes in HttpServletRequest and HttpServletResponse objects as arguments,
     * retrieves a Loan object from RequestParser.getLoan(request) method,
     * creates a loan by calling the LoanService.create(loan) method and
     * sets the resulting report in the session attribute REPORTS.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Path.SHOW_BOOKS a string constant representing the path to show the books.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Loan loan = RequestParser.getLoan(request);
        Report report = loanService.create(loan);
        request.getSession().setAttribute(REPORTS, report.getReports());
        log.info("create loan: {}", loan);
        return Path.SHOW_BOOKS;
    }
}
