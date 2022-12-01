package ua.od.cepuii.library.model;

import java.util.Objects;
import java.util.Set;

public class Book extends AbstractEntity {

    private String title;
    private String publication;
    private int datePublication;
    private Set<Author> authorSet;
    private int total;

    public Book(long id, String title, String publication, int datePublication, Set<Author> authorSet, int total) {
        super(id);
        this.title = title;
        this.publication = publication;
        this.datePublication = datePublication;
        this.authorSet = authorSet;
        this.total = total;
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

    public int getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(int datePublication) {
        this.datePublication = datePublication;
    }

    public Set<Author> getAuthorSet() {
        return authorSet;
    }

    public void setAuthorSet(Set<Author> authorSet) {
        this.authorSet = authorSet;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Book book = (Book) o;

        if (total != book.total) return false;
        if (datePublication != book.datePublication) return false;
        if (!Objects.equals(title, book.title)) return false;
        if (!Objects.equals(publication, book.publication)) return false;
        return Objects.equals(authorSet, book.authorSet);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (publication != null ? publication.hashCode() : 0);
        result = 31 * result + datePublication;
        result = 31 * result + (authorSet != null ? authorSet.hashCode() : 0);
        result = 31 * result + total;
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", title='" + title + '\'' +
                ", publication='" + publication + '\'' +
                ", date_publication=" + datePublication +
                ", authorSet=" + authorSet +
                ", noTotal=" + total +
                '}';
    }
}
