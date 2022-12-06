package ua.od.cepuii.library.util;

import ua.od.cepuii.library.model.Author;
import ua.od.cepuii.library.model.Book;
import ua.od.cepuii.library.model.PublicationType;

import java.util.Set;

public class BookUtil {

    public static Book testBook = new Book("test title", PublicationType.BOOK, 1999,
            Set.of(new Author(1001, "sheva")), 2);

    public static Book newBook = new Book("new title", PublicationType.NEWSPAPER, 1999,
            Set.of(new Author(1001, "sheva"), new Author(1003, "miron")), 5);

    public static Book forUpdateTestBook = new Book("update test title v2", PublicationType.BOOK, 1999, Set.of(new Author(1001, "sheva")), 2);

}
