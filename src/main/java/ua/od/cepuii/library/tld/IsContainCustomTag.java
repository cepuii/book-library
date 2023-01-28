package ua.od.cepuii.library.tld;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Set;

public class IsContainCustomTag extends TagSupport {

    private long bookId;


    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();

        Set<Long> loanItems = (Set<Long>) session.getAttribute("loanItems");
        if (loanItems != null) {
            boolean contains = loanItems.stream().anyMatch(aLong -> aLong.equals(bookId));
            try {
                pageContext.getOut().write(String.valueOf(contains));
            } catch (IOException e) {
                throw new JspException(e.getMessage());
            }
        }
        return SKIP_BODY;
    }
}
