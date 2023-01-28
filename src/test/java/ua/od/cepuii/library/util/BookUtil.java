package ua.od.cepuii.library.util;

import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.enums.PublicationType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BookUtil {

    public static final long LOAN_ID = 1000;
    public static final long NEW_BOOK_ID = 0;
    public static final long CREATE_BOOK_ID = 999;
    public static final long NOT_FOUND_ID = 999;
    public static final String TITLE = "Test book";
    public static final String AUTHOR_SEARCH = "Author";
    public static final int NO_OF_RECORDS = 5;
    public static final PublicationType TYPE = PublicationType.BOOK;
    public static final int DATE = 2022;
    public static final int TOTAL = 2;
    public static final int FINE = 120;
    public static final Author AUTHOR_1 = new Author("Author1");
    public static final Author AUTHOR_2 = new Author("Author2");
    public static final Author OLD_AUTHOR = new Author(923, "AAUTHOR");
    public static final Collection<Author> NEW_AUTHORS = List.of(AUTHOR_1, AUTHOR_2);
    public static final Collection<Author> AUTHORS_WITH_OLD = List.of(AUTHOR_1, AUTHOR_2, OLD_AUTHOR);

    public static final int COUNT_RESULT = 3;

    public static final int LIMIT = 5;
    public static final int OFFSET = 0;

    public static final Book NEW_BOOK = Book.builder()
            .id(NEW_BOOK_ID)
            .title(TITLE)
            .publicationType(TYPE)
            .datePublication(DATE)
            .total(TOTAL)
            .fine(FINE)
            .authors(NEW_AUTHORS)
            .build();
    public static final Book EMPTY_BOOK = Book.builder()
            .id(0)
            .title("")
            .publicationType(PublicationType.BOOK)
            .datePublication(0)
            .total(0)
            .fine(0)
            .authors(Collections.emptyList())
            .build();

    public static final Book BOOK = Book.builder()
            .id(LOAN_ID)
            .title(TITLE)
            .publicationType(TYPE)
            .datePublication(DATE)
            .total(TOTAL)
            .fine(FINE)
            .authors(NEW_AUTHORS)
            .build();
    public static final Book BOOK_NOT_EXIST = Book.builder()
            .id(NOT_FOUND_ID)
            .title(TITLE)
            .publicationType(TYPE)
            .datePublication(DATE)
            .total(TOTAL)
            .fine(FINE)
            .authors(NEW_AUTHORS)
            .build();
    public static final BookTO BOOK_TO = BookTO.builder()
            .id(LOAN_ID)
            .title(TITLE)
            .publicationType(TYPE)
            .datePublication(DATE)
            .total(TOTAL)
            .fine(FINE)
            .authors(NEW_AUTHORS)
            .build();

    public static List<Object> getBookFields(Book book) {
        return List.of(book.getTitle(), book.getPublicationType().ordinal(), book.getDatePublication(), book.getFine(), book.getTotal());
    }

}
