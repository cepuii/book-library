package ua.od.cepuii.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Abstract class that represents an entity in the system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private long id;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {id = " + id;
    }
}
