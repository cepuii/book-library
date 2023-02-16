package ua.od.cepuii.library.entity;

import lombok.*;
import ua.od.cepuii.library.entity.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class that represents a user in the library system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "password")
public class User extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private String email;
    private String password;
    private LocalDateTime dateTime;
    private boolean blocked;
    private int fine;
    private Role role;

    @Builder
    public User(long id, String email, String password, LocalDateTime dateTime,
                boolean blocked, int fine, Role role) {
        super(id);
        this.email = email;
        this.password = password;
        this.dateTime = dateTime;
        this.blocked = blocked;
        this.fine = fine;
        this.role = role;
    }

}
