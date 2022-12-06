package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.exception.RepositoryException;
import ua.od.cepuii.library.model.Author;
import ua.od.cepuii.library.model.Book;
import ua.od.cepuii.library.model.PublicationType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepositoryUtil {

    private RepositoryUtil() {
    }


    public static Collection<Book> fillBooks(ResultSet resultSet) {
        Map<Long, Book> bookMap = new HashMap<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                long id = resultSet.getLong("b_id");
                if (!bookMap.containsKey(id)) {
                    Book book = new Book(id,
                            resultSet.getString("b_title"),
                            PublicationType.valueOf(resultSet.getString("pt_name")),
                            resultSet.getInt("b_date"),
                            new HashSet<>(List.of(fillAuthor(resultSet).get())),
                            resultSet.getInt("b_total"));
                    bookMap.put(id, book);
                } else {
                    bookMap.get(id).getAuthorSet().add(fillAuthor(resultSet).get());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bookMap.values();
    }

    public static Collection<Author> fillAuthors(ResultSet resultSet) {
        try {
            Collection<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                Author author = new Author(resultSet.getInt("id"), resultSet.getString("name"));
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate authors from result set", e);
        }
    }

    public static Optional<Author> fillAuthor(ResultSet resultSet) {
        try {
            return Optional.of(new Author(resultSet.getInt("a_id"), resultSet.getString("a_name")));
        } catch (SQLException e) {
            throw new RepositoryException("Can`t populate author from result set", e);
        }
    }

}
