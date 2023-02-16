package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.entity.Book;

/**
 * Interface for a repository of {@link Book} entities.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface BookRepository extends EntityRepository<Book> {

    /**
     * Check if the book with the specified title already exists.
     *
     * @param title the title of the book to check
     * @return {@code true} if the book with the specified title exists, {@code false} otherwise
     */
    boolean isExistTitle(String title);
}
