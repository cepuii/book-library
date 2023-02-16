package ua.od.cepuii.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class that represents an author in the library system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Author extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String name;

    public Author(long id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", name='" + name + '\'' +
                '}';
    }
}
