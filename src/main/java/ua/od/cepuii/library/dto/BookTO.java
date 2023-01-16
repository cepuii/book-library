package ua.od.cepuii.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.enums.PublicationType;

import java.io.Serializable;
import java.util.Collection;

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
