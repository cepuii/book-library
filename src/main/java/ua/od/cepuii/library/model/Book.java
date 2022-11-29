package ua.od.cepuii.library.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Book {

    private int id;
    private String title;
    private String publication;
    private LocalDate datePublication;
    private Set<Author> authorSet;
    private int noTotal;
    private int noActual;

    public Book(int id, String title, String publication, LocalDate datePublication, Set<Author> authorSet, int noTotal) {
        this.id = id;
        this.title = title;
        this.publication = publication;
        this.datePublication = datePublication;
        this.authorSet = authorSet;
        this.noTotal = noTotal;
        this.noActual = noTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public Set<Author> getAuthorSet() {
        return authorSet;
    }

    public void setAuthorSet(Set<Author> authorSet) {
        this.authorSet = authorSet;
    }

    public int getNoTotal() {
        return noTotal;
    }

    public void setNoTotal(int noTotal) {
        this.noTotal = noTotal;
    }

    public int getNoActual() {
        return noActual;
    }

    public void setNoActual(int noActual) {
        this.noActual = noActual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (noTotal != book.noTotal) return false;
        if (noActual != book.noActual) return false;
        if (!Objects.equals(title, book.title)) return false;
        if (!Objects.equals(publication, book.publication)) return false;
        if (!Objects.equals(datePublication, book.datePublication))
            return false;
        return Objects.equals(authorSet, book.authorSet);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (publication != null ? publication.hashCode() : 0);
        result = 31 * result + (datePublication != null ? datePublication.hashCode() : 0);
        result = 31 * result + (authorSet != null ? authorSet.hashCode() : 0);
        result = 31 * result + noTotal;
        result = 31 * result + noActual;
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publication='" + publication + '\'' +
                ", date_publication=" + datePublication +
                ", authorSet=" + authorSet +
                ", noTotal=" + noTotal +
                ", noActual=" + noActual +
                '}';
    }
}
