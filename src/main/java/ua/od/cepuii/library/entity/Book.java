package ua.od.cepuii.library.entity;

import ua.od.cepuii.library.entity.enums.PublicationType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Book extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String title;
    private PublicationType publicationType;
    private int datePublication;
    private List<Author> authorSet;

    public Book() {
    }

    public Book(long id, String title, PublicationType publicationType, int datePublication, List<Author> authorSet) {
        super(id);
        this.title = title;
        this.publicationType = publicationType;
        this.datePublication = datePublication;
        this.authorSet = authorSet;
    }

    public Book(String title, PublicationType publicationType, int datePublication, List<Author> authorSet) {
        this(0, title, publicationType, datePublication, authorSet);
    }

    public Book(Builder builder) {
        this(builder.id, builder.title, builder.type, builder.datePublication, builder.authorSet);
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

    public List<Author> getAuthorSet() {
        return authorSet;
    }

    public void setAuthorSet(List<Author> authorSet) {
        this.authorSet = authorSet;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Book book = (Book) o;

        if (datePublication != book.datePublication) return false;
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
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", title='" + title + '\'' +
                ", publication='" + publicationType + '\'' +
                ", date_publication=" + datePublication +
                ", authorSet=" + authorSet +
                '}';
    }

    public static class Builder {
        private long id;
        private String title;
        private PublicationType type;
        private int datePublication;
        private List<Author> authorSet;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(PublicationType type) {
            this.type = type;
            return this;
        }

        public Builder setDatePublication(int datePublication) {
            this.datePublication = datePublication;
            return this;
        }

        public Builder setAuthorSet(List<Author> authorSet) {
            this.authorSet = authorSet;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
