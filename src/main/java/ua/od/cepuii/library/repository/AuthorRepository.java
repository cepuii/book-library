package ua.od.cepuii.library.repository;

import ua.od.cepuii.library.model.Author;

import java.util.Set;

public interface AuthorRepository extends AbstractEntityRepository<Author> {

    Set<Author> getAuthorsByBook();
}
