package ua.od.cepuii.library.entity;

import lombok.*;
import ua.od.cepuii.library.entity.enums.PublicationType;

import java.io.Serializable;
import java.util.Collection;
/**
 * Class that represents a book in the library system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String title;
    private PublicationType publicationType;
    private int datePublication;
    private Collection<Author> authors;

    private int total;
    private int fine;

    @Builder
    public Book(long id, String title, PublicationType publicationType, int datePublication, Collection<Author> authors, int total, int fine) {
        super(id);
        this.title = title;
        this.publicationType = publicationType;
        this.datePublication = datePublication;
        this.authors = authors;
        this.total = total;
        this.fine = fine;
    }
}
