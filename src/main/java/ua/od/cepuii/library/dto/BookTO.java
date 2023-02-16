package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.enums.PublicationType;

import java.io.Serializable;
import java.util.Collection;

/**
 * The BookTO class is a Transfer Object (DTO) class that represents a book in the library system.
 * It includes the book's title, publication type, publication date, authors, total number of copies, and fine amount.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTO implements Serializable {
    private static final long serialVersionUID = 1;
    private long id;
    private String title;
    private PublicationType publicationType;
    private int datePublication;
    private Collection<Author> authors;
    private int total;
    private int fine;

}
