package ua.od.cepuii.library.entity;

import ua.od.cepuii.library.entity.enums.PublicationType;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Book extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String title;
    private PublicationType publicationType;
    private int datePublication;
    private Set<Author> authorSet;
    private int total;

    public Book() {
    }

    public Book(long id, String title, PublicationType publicationType, int datePublication, Set<Author> authorSet, int total) {
        super(id);
        this.title = title;
        this.publicationType = publicationType;
        this.datePublication = datePublication;
        this.authorSet = authorSet;
        this.total = total;
    }

    public Book(String title, PublicationType publicationType, int datePublication, Set<Author> authorSet, int total) {
        this(0, title, publicationType, datePublication, authorSet, total);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PublicationType getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
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

        if (datePublication != book.datePublication) return false;
        if (total != book.total) return false;
        if (!Objects.equals(title, book.title)) return false;
        if (publicationType != book.publicationType) return false;
        return Objects.equals(authorSet, book.authorSet);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (publicationType != null ? publicationType.hashCode() : 0);
        result = 31 * result + datePublication;
        result = 31 * result + (authorSet != null ? authorSet.hashCode() : 0);
        result = 31 * result + total;
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", title='" + title + '\'' +
                ", publication='" + publicationType + '\'' +
                ", date_publication=" + datePublication +
                ", authorSet=" + authorSet +
                ", noTotal=" + total +
                '}';
    }
}
