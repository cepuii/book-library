package ua.od.cepuii.library.entity;

import java.io.Serializable;
import java.util.Objects;

public class Author extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String name;

    public Author(long id, String name) {
        super(id);
        this.name = name;
    }

    public Author(String name) {
        this(0, name);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Author author = (Author) o;

        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", name='" + name + '\'' +
                '}';
    }
}
