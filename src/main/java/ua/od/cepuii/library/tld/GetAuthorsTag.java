package ua.od.cepuii.library.tld;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import ua.od.cepuii.library.entity.Author;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class GetAuthorsTag extends TagSupport {
    private Collection<Author> authorSet;

    public void setAuthorSet(Collection<Author> authorSet) {
        this.authorSet = authorSet;
    }

    @Override
    public int doStartTag() throws JspException {

        String authors = authorSet.stream().map(Author::getName).collect(Collectors.joining(", "));

        try {
            pageContext.getOut().write(authors);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return SKIP_BODY;
    }
}
