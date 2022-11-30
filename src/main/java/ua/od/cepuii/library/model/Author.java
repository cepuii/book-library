package ua.od.cepuii.library.model;

import java.util.Objects;

public class Author extends AbstractEntity {
    private String name;

    public Author(int id, String name) {
        super(id);
        this.name = name;
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
