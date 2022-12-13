package ua.od.cepuii.library.entity;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private long id;

    protected AbstractEntity() {
    }

    protected AbstractEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {id = " + id;
    }
}
