package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.Book;

import java.util.List;

public interface BookRepository {

    Book create(Book book);

    Book getById(int id);

    Book update(Book book);

    boolean delete(int id);

    List<Book> getAll();

    Book getByTitle(String title);

    Book getByAuthor(String author);
}
