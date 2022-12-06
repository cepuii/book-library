package ua.od.cepuii.library;

import ua.od.cepuii.library.model.Book;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.JdbcBookRepository;

import java.time.LocalDate;

public class Demo {

    public static void main(String[] args) {
        BookRepository bookRepository = new JdbcBookRepository();
        Book book = new Book(0, "title", "Четыре четверти", LocalDate.of(2013, 1, 1), null, 10);
        bookRepository.create(book);
        System.out.println(book);
    }

}
